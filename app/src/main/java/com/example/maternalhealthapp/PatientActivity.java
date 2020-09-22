package com.example.maternalhealthapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.maternalhealthapp.Data.Patient;
import com.example.maternalhealthapp.Data.PatientAdoptor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PatientActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    List<Patient> patients = new ArrayList<>();
    private  PatientAdoptor patientAdoptor;
    private static final String TAG = "PatientList";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(PatientActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPatient=new Intent(PatientActivity.this,AddPatient.class);
                startActivity(addPatient);
            }
        });
        getPatients();


    }
    public  void getPatients() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("patients");
        myRef.keepSynced(true);

        RecyclerView patient_recyclerview = (RecyclerView) findViewById(R.id.patient_recycler_view);
        patientAdoptor = new PatientAdoptor(this,patients);
        patient_recyclerview.setAdapter(patientAdoptor);
        patient_recyclerview.setLayoutManager(new GridLayoutManager(PatientActivity.this, 1));

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                patients.clear();
                for (DataSnapshot single : dataSnapshot.getChildren()) {

                    Patient patient =  single.getValue(Patient.class);
                    patients.add(patient);
                }                // ...
                patientAdoptor.notifyDataSetChanged();
                Log.e(TAG, "Data received:" + patients.size());
                progressDialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
              //  Log.w(TAG, "fetchData onCancelled", databaseError.toException());
                // ...
            }
        };
        myRef.addListenerForSingleValueEvent(postListener);



    }

}
