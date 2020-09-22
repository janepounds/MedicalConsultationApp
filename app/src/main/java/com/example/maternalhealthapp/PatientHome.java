package com.example.maternalhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maternalhealthapp.Data.Patient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PatientHome extends AppCompatActivity {
    TextView appointment, sos, checkups, my_appointments;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String TAG = "Home Activity";
    FusedLocationProviderClient mFusedLocationClient;
    private double myLocation;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        appointment=(TextView)findViewById(R.id.appointment_button);
        TextView username = (TextView) findViewById(R.id.patient_username);
        username.setText(user.getEmail());
        sos = (TextView)findViewById(R.id.sos_button);
        checkups =(TextView)findViewById(R.id.my_check_ups);
        my_appointments = (TextView) findViewById(R.id.appointment_view_button);
        my_appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientHome.this, MyAppointments.class);
                startActivity(intent);
            }
        });
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientHome.this,AddAppointments.class);
                startActivity(intent);
            }
        });
        checkups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientHome.this,MyCheckups.class);
                startActivity(intent);

            }
        });
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS();
                getLocation();
            }
        });

    }

    protected void sendSMS() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);

        } else {
            SendTextMsg();
        }
    }

    ///get location
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);


        } else {
            Log.d(TAG, "getLocation: permissions granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SendTextMsg();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SOS failed, please try again.", Toast.LENGTH_LONG).show();
                }


                break;
            case REQUEST_LOCATION_PERMISSION: {
                if (grantResults.length > 1
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            "Location Permission granted !",
                            Toast.LENGTH_SHORT).show();
                    getLocation();
                } else {
                    Toast.makeText(this,
                            "Location Permission not granted !",
                            Toast.LENGTH_SHORT).show();
                }
            }
            break;

        }
    }

    private void SendTextMsg() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            myLocation = location.getAltitude();
                        }
                    }
                });

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("0754670542", null, "I need immediate help from this location :" + String.valueOf(myLocation), null, null);

        Toast.makeText(getApplicationContext(), "SOS sent.",
                Toast.LENGTH_LONG).show();
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

    public void logOut() {
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
