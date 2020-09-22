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
import com.example.maternalhealthapp.Data.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Profile extends AppCompatActivity {
    private static final String TAG = "add profile";
    private static final String REQUIRED = "Required";
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    EditText ed_firstName,ed_lastName,ed_phone,ed_dob;
    DatePickerDialog datePickerDialog;
    Button submitprofile;
    private ProgressBar progressBar;
    String dob,role;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]
        ed_firstName=(EditText)findViewById(R.id.input_profile_fName);
        ed_lastName=(EditText)findViewById(R.id.input_profile_lName);
        progressBar=(ProgressBar)findViewById(R.id.add_profile_progress_bar);
        ed_phone=(EditText)findViewById(R.id.input_profile_phone);
        ed_dob=(EditText)findViewById(R.id.input_profile_date);
        ed_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(Profile.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                ed_dob.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                dob=year+"-"+(monthOfYear + 1) + "-"+dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        submitprofile =(Button)findViewById(R.id.btn_post_profile);
        submitprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitProfile();
            }
        });
    }
    public void handleRoleSelection(View view){
        int id = view.getId();

        if (id == R.id.rb_patient){
            role="patient";
        }
        else if(id==R.id.rb_doctor){
            role="doctor";
        }
        else {
            role=null;
        }

    }
    private void submitProfile() {

        final String firstName = ed_firstName.getText().toString();
        final String lastName = ed_lastName.getText().toString();
        final String phone = ed_phone.getText().toString();
        final String dob = ed_dob.getText().toString();


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

        User userProfile = new User(userId,firstName,lastName,phone,dob,role);
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.child("profiles").child(userId).setValue(userProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(getApplicationContext(), "Profile updated  successful!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(),Login.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(getApplicationContext(), "Failed to add profile!", Toast.LENGTH_LONG).show();
                    }
                });

    }

}
