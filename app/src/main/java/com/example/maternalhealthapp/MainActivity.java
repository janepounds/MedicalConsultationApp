package com.example.maternalhealthapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    CardView checkupCard,patientCard,staffCard,appointmentsCard;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView username = (TextView) findViewById(R.id.doctor_username);
        username.setText(user.getEmail());
        checkupCard =(CardView)findViewById(R.id.chech_up_card);
        patientCard =(CardView)findViewById(R.id.patients_card);
        staffCard =(CardView)findViewById(R.id.staffs_card);
        appointmentsCard =(CardView)findViewById(R.id.appointments_card);
        checkupCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoCheckups=new Intent(MainActivity.this,CheckUpActivity.class);
                startActivity(gotoCheckups);
            }
        });
        patientCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToPatient= new Intent(MainActivity.this,PatientActivity.class);
                startActivity(goToPatient);
            }
        });

        staffCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoStaffs = new Intent(MainActivity.this,StaffActivity.class);
                startActivity(gotoStaffs);
            }
        });
        appointmentsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAppointments = new Intent(MainActivity.this, AppointmentActivity.class);
                startActivity(goToAppointments);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                goToSetting();
                return true;
            case R.id.help:
                showHelp();
                return true;
            case R.id.logout:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, Login.class));
        finish();
    }
 public void goToSetting() {
     Toast.makeText(getApplicationContext(), "feature not yet implemented..", Toast.LENGTH_LONG).show();
 }
    public void showHelp() {
        Toast.makeText(getApplicationContext(), "contact +256 754 670542", Toast.LENGTH_LONG).show();
    }

}
