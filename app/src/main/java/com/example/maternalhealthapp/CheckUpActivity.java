package com.example.maternalhealthapp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.maternalhealthapp.Data.Appointment;
import com.example.maternalhealthapp.Data.AppointsAdapter;
import com.example.maternalhealthapp.Data.CheckupAppointmentAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CheckUpActivity extends AppCompatActivity {

    private SearchView searchView;
    private MenuItem searchMenuItem;
    private CheckupAppointmentAdapter appointsAdapter;
    private DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    ArrayList<Appointment> appointments = new ArrayList<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId = user.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_up);
        // Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(CheckUpActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        getAppointments();
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
                    //  appointsAdapter.getFilter().filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    // filter recycler view when text is changed
                    //  userAdapter.getFilter().filter(query);
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

    public void getAppointments() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("appointments");
        myRef.keepSynced(true);

        RecyclerView appointmentRecycler = (RecyclerView) findViewById(R.id.checkup_appointments_recycler_view);
        appointsAdapter = new CheckupAppointmentAdapter(this, appointments);
        appointmentRecycler.setAdapter(appointsAdapter);
        appointmentRecycler.setLayoutManager(new GridLayoutManager(CheckUpActivity.this, 1));
        Query myDoctorsQuery = myRef.orderByChild("doctorAppointments").equalTo(userId + "Approved");
        //.orderByChild("name").equalTo("name");

        myDoctorsQuery.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                appointments.clear();
                for (DataSnapshot single : dataSnapshot.getChildren()) {

                    Appointment appointment = single.getValue(Appointment.class);
                    appointment.appointmentId = single.getKey();
                    appointments.add(appointment);
                    String key = single.getKey();
                    Log.e(TAG, "database key received:" + key);
                }                // ...
                appointsAdapter.notifyDataSetChanged();
                Log.e(TAG, "Data received:" + appointments.size());
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
