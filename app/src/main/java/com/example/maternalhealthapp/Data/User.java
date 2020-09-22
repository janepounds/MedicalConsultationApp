package com.example.maternalhealthapp.Data;

import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START user_class]
@IgnoreExtraProperties
public class User {
    public String uid;
    public String firstName;
    public String lastName;
    public String phone;
    public String dob;
    public String role;

    public User() {
    }

    public User(String uid, String firstName, String lastName, String phone, String dob, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone=phone;
        this.dob = dob;
        this.role = role;
        this.uid=uid;
    }
    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("phone", phone);
        result.put("dob", dob);
        result.put("role", role);

        return result;
    }
    // [END post_to_map]
}
// [END user_class]
