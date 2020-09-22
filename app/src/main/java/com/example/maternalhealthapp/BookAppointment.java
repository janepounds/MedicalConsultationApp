package com.example.maternalhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.maternalhealthapp.Data.Appointment;
import com.example.maternalhealthapp.Data.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class BookAppointment extends AppCompatActivity {
    TextView doctor;
    private EditText appointmentDate, appointmentStartTime, appointmentEndTime, reason;
    CheckBox termsAndCondition;
    ProgressBar progressBar;
    Button addAppointment;
    private String format = "";
    private static final String REQUIRED = "Required";
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatePickerDialog datePickerDialog;
    private String ActualDate;
    //declaring user extra info
    String name, email, phone;
    String status = "Pending";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userId = user.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        // database.setPersistenceEnabled(true);
        //initialise database instance
        mDatabase = database.getInstance().getReference();
        doctor = (TextView) findViewById(R.id.booking_doc_name);
        doctor.setText("Dr. " + getIntent().getStringExtra("doctor"));
        appointmentDate = (EditText) findViewById(R.id.book_appointment_date);
        appointmentStartTime = (EditText) findViewById(R.id.book_appointment_start);
        appointmentEndTime = (EditText) findViewById(R.id.book_appointment_end);
        reason = (EditText) findViewById(R.id.book_appoint_reason);
        termsAndCondition = (CheckBox) findViewById(R.id.terms_and_conditions);
        addAppointment = (Button) findViewById(R.id.btn_book_appointment);
        //hide appointment button
        addAppointment.setVisibility(View.GONE);
        addAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookAppointment();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.add_appointment_progressBar);
        appointmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(BookAppointment.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                appointmentDate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                ActualDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        appointmentStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BookAppointment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        showTime(selectedHour, selectedMinute);
                        appointmentStartTime.setText(new StringBuilder().append(selectedHour).append(" : ").append(selectedMinute).append(" ").append(format));
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        appointmentEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BookAppointment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        showTime(selectedHour, selectedMinute);
                        appointmentEndTime.setText(new StringBuilder().append(selectedHour).append(" : ").append(selectedMinute).append(" ").append(format));
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        mDatabase.child("profiles").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User patientUser = dataSnapshot.getValue(User.class);
                name = patientUser.firstName + " " + patientUser.lastName;
                email = user.getEmail();
                phone = patientUser.phone;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void showTime(int hour, int minutes) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
    }

    ///hide and show button using checkbox
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.terms_and_conditions:
                if (checked) {
                    addAppointment.setVisibility(View.VISIBLE);
                }
                // Put some meat on the sandwich
                else {
                    addAppointment.setVisibility(View.GONE);
                }
                break;
            // TODO: Veggie sandwich
        }
    }


    private void bookAppointment() {
        final String doctor_id = getIntent().getStringExtra("doctor_id");
        final String doctor = getIntent().getStringExtra("doctor");
        final String appointment_date = appointmentDate.getText().toString();
        final String appointmentReason = reason.getText().toString();
        final String start_time = appointmentStartTime.getText().toString();
        final String end_time_ = appointmentEndTime.getText().toString();
        final boolean terms = termsAndCondition.isChecked();
        final String doctorAppointments = doctor_id + status;

        final String patient = userId;

        // first name is required
        if (TextUtils.isEmpty(appointment_date)) {
            appointmentDate.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(appointmentReason)) {
            reason.setError(REQUIRED);
            return;
        }
        // lastname is required
        if (TextUtils.isEmpty(start_time)) {
            appointmentStartTime.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(end_time_)) {
            appointmentEndTime.setError(REQUIRED);
            return;
        }


        Appointment appointment = new Appointment(userId, name, email, phone, doctor_id, doctor, appointment_date, start_time, end_time_, appointmentReason, terms, status, doctorAppointments);
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.child("appointments").push().setValue(appointment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(getApplicationContext(), "Booked Appointment  successful!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(), PatientHome.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(getApplicationContext(), "Failed to make appointment!", Toast.LENGTH_LONG).show();
                    }
                });

    }
}
