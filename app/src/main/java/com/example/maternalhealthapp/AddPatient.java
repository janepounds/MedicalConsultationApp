package com.example.maternalhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.maternalhealthapp.Data.Patient;
import com.example.maternalhealthapp.Data.PatientAdoptor;
import com.example.maternalhealthapp.Data.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AddPatient extends AppCompatActivity {
    private static final String TAG = "AddPatientActivity";
    private static final String REQUIRED = "Required";
    Spinner user_spinner;
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    EditText ed_firstName,ed_lastName,ed_phone,ed_dob,ed_next_of_kin,ed_next_of_kin_contact;
    DatePickerDialog datePickerDialog;
    Button submitpatient;
    List<Patient> users = new ArrayList<>();
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]
        ed_firstName=(EditText)findViewById(R.id.fName);
        ed_lastName=(EditText)findViewById(R.id.lName);
        progressBar=(ProgressBar)findViewById(R.id.add_patient_progress_bar);
        ed_phone=(EditText)findViewById(R.id.phone);
        ed_dob=(EditText)findViewById(R.id.input_date);
        ed_next_of_kin =(EditText)findViewById(R.id.input_next_of_keen);
        // user_spinner=(Spinner)findViewById(R.id.user_spinner);
        ed_next_of_kin_contact=(EditText)findViewById(R.id.input_next_of_keen_contact);
        ed_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the calender instance
                final Calendar calendar = Calendar.getInstance();
                int my_year = calendar.get(Calendar.YEAR);
                int my_month = calendar.get(Calendar.MONTH);
                int my_day = calendar.get(Calendar.DAY_OF_MONTH);
                //picking date from the date picker dialogue
                datePickerDialog = new DatePickerDialog(AddPatient.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                ed_dob.setText(dayOfMonth+"/"+(month)+1+"/"+year);
                            }
                        },my_year,my_month,my_day);
                datePickerDialog.show();
            }
        });
        submitpatient =(Button)findViewById(R.id.btn_post_patient);
        submitpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPatients();
                submitPatient();
            }
        });

    }


    private void submitPatient() {

        final String firstName = ed_firstName.getText().toString();
        final String lastName = ed_lastName.getText().toString();
        final String phone = ed_phone.getText().toString();
        final String dob = ed_dob.getText().toString();
        final String nextOfKin = ed_next_of_kin.getText().toString();
        final String nextOfKinContact = ed_next_of_kin_contact.getText().toString();

        // first name is required
        if (TextUtils.isEmpty(firstName)) {
            ed_firstName.setError(REQUIRED);
            return;
        }

        // lastname is required
        if (TextUtils.isEmpty(lastName)) {
            ed_lastName.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            ed_phone.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(dob)) {
            ed_dob.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(nextOfKin)) {
            ed_next_of_kin.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(nextOfKinContact)) {
            ed_next_of_kin_contact.setError(REQUIRED);
            return;
        }
        Patient patient = new Patient(firstName,lastName,phone,dob,nextOfKin,nextOfKinContact);
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.child("patients").push().setValue(patient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(getApplicationContext(), "Patient added  successful!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(),PatientActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(getApplicationContext(), "Failed to add patient!", Toast.LENGTH_LONG).show();
                    }
                });

    }
    public  void getPatients() {
        mDatabase.child("patients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // initialize the array
                final List<String> users = new ArrayList<String>();

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    String username = userSnapshot.child("firstName").getValue(String.class);
                    users.add(username);
                }


                ArrayAdapter<String> usersAdapter = new ArrayAdapter<String>(AddPatient.this, android.R.layout.simple_spinner_item, users);
                usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                user_spinner.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
