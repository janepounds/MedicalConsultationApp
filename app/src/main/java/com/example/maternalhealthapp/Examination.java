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
import android.widget.TextView;
import android.widget.Toast;

import com.example.maternalhealthapp.Data.Appointment;
import com.example.maternalhealthapp.Data.Checkup;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Examination extends AppCompatActivity {
    private EditText medicalHistory, bloodPressure, pelvicExam, dopplerFetalHeartRate, urineTest, bloodTest, recommendation, prescription, nextAppointmentDate;
    private EditText height, weight;
    private TextView name, reason;
    private Button submit;
    private DatabaseReference mDatabase;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ProgressBar progressBar;
    private static final String REQUIRED = "Required";
    DatePickerDialog datePickerDialog;
    private String ActualDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);
        name = (TextView) findViewById(R.id.examination_name);
        mDatabase = database.getInstance().getReference();
        progressBar = (ProgressBar) findViewById(R.id.examination_appointment_progressBar);
        reason = (TextView) findViewById(R.id.examination_reason);
        height = (EditText) findViewById(R.id.examination_height);
        weight = (EditText) findViewById(R.id.examination_weight);
        medicalHistory = (EditText) findViewById(R.id.examination_medical_history);
        prescription = (EditText) findViewById(R.id.examination_prescription);
        nextAppointmentDate = (EditText) findViewById(R.id.exam_appointment_date_next);
        bloodPressure = (EditText) findViewById(R.id.examination_blood_pressure);
        pelvicExam = (EditText) findViewById(R.id.examination_pelvic_exam);
        dopplerFetalHeartRate = (EditText) findViewById(R.id.examination_doppler_fatal_monitor);
        urineTest = (EditText) findViewById(R.id.examination_urine_test);
        bloodTest = (EditText) findViewById(R.id.examination_blood_test);
        recommendation = (EditText) findViewById(R.id.examination_recommendations);
        submit = (Button) findViewById(R.id.btn_exam_submit);
        name.setText(getIntent().getStringExtra("name"));
        reason.setText(getIntent().getStringExtra("reason"));
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPatient();
            }
        });
        nextAppointmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(Examination.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                nextAppointmentDate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                ActualDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    private void checkPatient() {
        final String appointmentId = getIntent().getStringExtra("id");
        final String doctor_id = getIntent().getStringExtra("doctor_id");
        final String doctor = getIntent().getStringExtra("doctor");
        final String appointmentReason = getIntent().getStringExtra("reason");
        final String patientID = getIntent().getStringExtra("patient");
        final String patientName = getIntent().getStringExtra("name");
        final String pmedicalHistory = medicalHistory.getText().toString();
        final String pbloodPressure = bloodPressure.getText().toString();
        final double patientHeight = Double.parseDouble(height.getText().toString());
        final double patientWeight = Double.parseDouble(weight.getText().toString());
        final String PpelvicExam = pelvicExam.getText().toString();
        final String PdopplerFetalHeartRate = dopplerFetalHeartRate.getText().toString();
        final String PUrineTest = urineTest.getText().toString();
        final String PBloodTest = bloodTest.getText().toString();
        final String PRecommendation = recommendation.getText().toString();
        final String prePrescription = prescription.getText().toString();
        final String nextAppointSchedule = nextAppointmentDate.getText().toString();
        final String treatmentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


        // first name is required
        if (TextUtils.isEmpty(pmedicalHistory)) {
            medicalHistory.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(PdopplerFetalHeartRate)) {
            dopplerFetalHeartRate.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(PpelvicExam)) {
            pelvicExam.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(appointmentReason)) {
            reason.setError(REQUIRED);
            return;
        }
        // lastname is required
        if (TextUtils.isEmpty(pbloodPressure)) {
            bloodPressure.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(PRecommendation)) {
            recommendation.setError(REQUIRED);
            return;
        }


        Checkup checkup = new Checkup(appointmentId, appointmentReason, patientID, patientName, doctor_id, doctor, pmedicalHistory, pbloodPressure, patientHeight, patientWeight, PpelvicExam, PdopplerFetalHeartRate, PUrineTest, PBloodTest, PRecommendation, prePrescription, nextAppointSchedule, treatmentDate);
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.child("checkups").push().setValue(checkup)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(getApplicationContext(), "Checkup results saved  successful!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        // Intent intent = new Intent(getApplicationContext(), PatientHome.class);
                        // startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(getApplicationContext(), "Failed to process result!", Toast.LENGTH_LONG).show();
                    }
                });

        ///change appointment status to completed
        final String email = getIntent().getStringExtra("email");
        final String phone = getIntent().getStringExtra("phone");
        final String appointment_date = getIntent().getStringExtra("appointment_date");
        final String start_time = getIntent().getStringExtra("start_time");
        final String end_time_ = getIntent().getStringExtra("end_time");
        final String doctorAppointments = doctor_id + "completed";


        Appointment appointment = new Appointment(patientID, patientName, email, phone, doctor_id, doctor, appointment_date, start_time, end_time_, appointmentReason, true, "completed", doctorAppointments);
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.child("appointments").child(appointmentId).setValue(appointment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        // Toast.makeText(getApplicationContext(), "Booked Appointment  successful!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(), StaffActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // Toast.makeText(getApplicationContext(), "Failed to make appointment!", Toast.LENGTH_LONG).show();
                    }
                });


    }
}
