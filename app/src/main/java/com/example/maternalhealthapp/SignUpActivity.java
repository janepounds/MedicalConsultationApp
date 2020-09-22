package com.example.maternalhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maternalhealthapp.Data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {


    private EditText emailTV, passwordTV;
    private Button regBtn;
    private ProgressBar progressBar;
    private TextView txtBackTologin;
    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        txtBackTologin=(TextView)findViewById(R.id.txt_back_to_login);
        regBtn =(Button)findViewById(R.id.btn_signup);
        progressBar =(ProgressBar)findViewById(R.id.signup_progressBar);
        emailTV=(EditText)findViewById(R.id.signup_email);
        passwordTV=(EditText)findViewById(R.id.password_signup);
        //calling the firebase instance
        mAuth = FirebaseAuth.getInstance();

       // initializeUI();
        ///taking user back to login  using the back to login link
        txtBackTologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,Login.class);
                startActivity(intent);
            }
        });
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });


    }
    //register new user from here
    private void registerNewUser(){
        progressBar.setVisibility(View.VISIBLE);
       final String password,email;
        email = emailTV.getText().toString();
        password= passwordTV.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //login user after registration
                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                               // Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);

                                                Intent intent = new Intent(SignUpActivity.this, Profile.class);
                                                startActivity(intent);
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "an error has occured", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });

                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                           // Intent intent = new Intent(SignUpActivity.this, Login.class);
                           // startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }


}
