package com.example.maternalhealthapp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.maternalhealthapp.Data.Appointment;
import com.example.maternalhealthapp.Data.AppointsAdapter;
import com.example.maternalhealthapp.Data.User;
import com.example.maternalhealthapp.Data.UserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class AddAppointments extends AppCompatActivity {

    private SearchView searchView;
    private MenuItem searchMenuItem;
    private UserAdapter userAdapter;
    private DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    ArrayList<User> doctors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointments);
        // Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(AddAppointments.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        mDatabase = FirebaseDatabase.getInstance().getReference();
         getDoctors();
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
            // Toast.makeText(getApplicationContext(), "Search from hwew!", Toast.LENGTH_LONG).show();
            // listening to search query text change
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // filter recycler view when query submitted
                    userAdapter.getFilter().filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    // filter recycler view when text is changed
                    userAdapter.getFilter().filter(query);
                    return false;
                }
            });
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        // searchView.setSearchableInfo(
        //     searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
    public  void getDoctors() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("profiles");
        myRef.keepSynced(true);

        RecyclerView doctors_recyclerview = (RecyclerView) findViewById(R.id.appointment_doctors_recycler_view);
        userAdapter = new UserAdapter(this,doctors);
        doctors_recyclerview.setAdapter(userAdapter);
        doctors_recyclerview.setLayoutManager(new GridLayoutManager(AddAppointments.this, 1));
        Query myDoctorsQuery = myRef
                .orderByChild("role").equalTo("doctor");

        myDoctorsQuery.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                doctors.clear();
                for (DataSnapshot single : dataSnapshot.getChildren()) {

                    User doc =  single.getValue(User.class);
                    doctors.add(doc);
                }                // ...
                userAdapter.notifyDataSetChanged();
                Log.e(TAG, "Data received:" + doctors.size());
                progressDialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //  Log.w(TAG, "fetchData onCancelled", databaseError.toException());
                // ...
            }
        });
        //myRef.addListenerForSingleValueEvent(postListener);

    }

}
