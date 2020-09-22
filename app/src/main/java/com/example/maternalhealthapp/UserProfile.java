package com.example.maternalhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.maternalhealthapp.Data.Patient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class UserProfile extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ProgressBar progressBar;
    private static final String REQUIRED = "Required";
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    EditText ed_firstName,ed_lastName,ed_phone,ed_dob,ed_next_of_kin,ed_next_of_kin_contact;
    DatePickerDialog datePickerDialog;
    Button submitpatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        ed_firstName=(EditText)findViewById(R.id.p_fName);
        ed_lastName=(EditText)findViewById(R.id.p_lName);
        progressBar=(ProgressBar)findViewById(R.id.add_patient_progress_bar1);
        ed_phone=(EditText)findViewById(R.id.p_phone);
        ed_dob=(EditText)findViewById(R.id.p_input_date);
        ed_next_of_kin =(EditText)findViewById(R.id.p_input_next_of_keen);
        ed_next_of_kin_contact=(EditText)findViewById(R.id.p_input_next_of_keen_contact);
        ed_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the calender instance
                final Calendar calendar = Calendar.getInstance();
                int my_year = calendar.get(Calendar.YEAR);
                int my_month = calendar.get(Calendar.MONTH);
                int my_day = calendar.get(Calendar.DAY_OF_MONTH);
                //picking date from the date picker dialogue
                datePickerDialog = new DatePickerDialog(UserProfile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                ed_dob.setText(dayOfMonth+"/"+(month)+1+"/"+year);
                            }
                        },my_year,my_month,my_day);
                datePickerDialog.show();
            }
        });
        submitpatient =(Button)findViewById(R.id.btn_post_patient_p);
        submitpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        final String userId = getUid();
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
        Patient patient = new Patient(userId,firstName,lastName,phone,dob,nextOfKin,nextOfKinContact);
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.child("patients").child(userId).setValue(patient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(getApplicationContext(), "info updated successfully!!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(),PatientActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(getApplicationContext(), "Failed to update info!", Toast.LENGTH_LONG).show();
                    }
                });



    }

}
