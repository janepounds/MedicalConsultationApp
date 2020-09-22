package com.example.maternalhealthapp.Data;

import com.google.firebase.firestore.IgnoreExtraProperties;

// [START patient_class]
@IgnoreExtraProperties
public class Patient {
    public String uid;
    public  String firstName;
    public  String lastName;
    public  String phone;
    public String dob;
    public String next_of_kin;
    public String next_of_kin_contact;

    public Patient() {
       //default constructor
    }

    public Patient(String uid,String firstName, String lastName, String phone, String dob, String next_of_kin, String next_of_kin_contact) {
        this.uid=uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.dob = dob;
        this.next_of_kin = next_of_kin;
        this.next_of_kin_contact = next_of_kin_contact;
    }
    public Patient(String firstName, String lastName, String phone, String dob, String next_of_kin, String next_of_kin_contact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.dob = dob;
        this.next_of_kin = next_of_kin;
        this.next_of_kin_contact = next_of_kin_contact;
    }
}
// [END patient_class]
