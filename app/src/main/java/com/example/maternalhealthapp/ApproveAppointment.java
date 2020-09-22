package com.example.maternalhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.maternalhealthapp.Data.Appointment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ApproveAppointment extends AppCompatActivity {
    TextView patientName, appointmentReason;
    EditText appointmentDate, appointmentStart, appointmentEnd;
    Button approvalButton;
    String status;
    DatePickerDialog datePickerDialog;
    private String ActualDate;
    private String format = "";
    ProgressBar progressBar;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_appointment);
        progressBar = (ProgressBar) findViewById(R.id.approve_appointment_progressBar);
        mDatabase = database.getInstance().getReference();
        approvalButton = (Button) findViewById(R.id.btn_book_appointment);
        appointmentDate = (EditText) findViewById(R.id.approve_appointment_date);
        appointmentStart = (EditText) findViewById(R.id.approve_appointment_start);
        appointmentEnd = (EditText) findViewById(R.id.approve_appointment_end);
        patientName = (TextView) findViewById(R.id.approve_appointment_name);
        appointmentReason = (TextView) findViewById(R.id.approve_appoint_reason);
        patientName.setText(getIntent().getStringExtra("name"));
        appointmentReason.setText(getIntent().getStringExtra("reasone"));
        appointmentDate.setText(getIntent().getStringExtra("appointment_date"));
        appointmentStart.setText(getIntent().getStringExtra("start_time"));
        appointmentEnd.setText(getIntent().getStringExtra("end_time"));

        //get new appointment date
        appointmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ApproveAppointment.this,
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

        appointmentStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ApproveAppointment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        showTime(selectedHour, selectedMinute);
                        appointmentStart.setText(new StringBuilder().append(selectedHour).append(" : ").append(selectedMinute).append(" ").append(format));
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        appointmentEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ApproveAppointment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        showTime(selectedHour, selectedMinute);
                        appointmentEnd.setText(new StringBuilder().append(selectedHour).append(" : ").append(selectedMinute).append(" ").append(format));
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        //approve appointment from here
        approvalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveAppointment();
            }
        });
    }

    public void getApprovalSelection(View view) {
        int id = view.getId();

        if (id == R.id.rb_accept) {
            status = "Approved";
        } else if (id == R.id.rb_reject) {
            status = "Rejected";
        } else {
            status = "Pending";
        }

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

    private void approveAppointment() {
        final String doctor_id = getIntent().getStringExtra("doctor_id");
        final String doctor = getIntent().getStringExtra("doctor");
        final String appointment_date = appointmentDate.getText().toString();
        final String appointmentReason = getIntent().getStringExtra("reasone");
        final String start_time = appointmentStart.getText().toString();
        final String end_time_ = appointmentEnd.getText().toString();
        final String name = getIntent().getStringExtra("name");
        final String email = getIntent().getStringExtra("email");
        final String phone = getIntent().getStringExtra("phone");
        final String userId = getIntent().getStringExtra("patient");
        final String appointmentId = getIntent().getStringExtra("id");
        final String doctorAppointments = doctor_id + status;


        Appointment appointment = new Appointment(userId, name, email, phone, doctor_id, doctor, appointment_date, start_time, end_time_, appointmentReason, true, status, doctorAppointments);
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.child("appointments").child(appointmentId).setValue(appointment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(getApplicationContext(), "Booked Appointment  successful!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(), AppointmentActivity.class);
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
