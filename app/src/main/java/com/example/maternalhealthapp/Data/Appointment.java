package com.example.maternalhealthapp.Data;

public class Appointment {
    public String appointmentId;
    public String patient;
    public String name;
    public String email;
    public String phone;
    public String doctor_id;
    public String doctor;
    public String appointment_date;
    public String start;
    public String end;
    public String appointment_reason;
    public boolean terms_and_condition;
    public String status;
    public String doctorAppointments;

    public Appointment(String patient, String doctor, String appointment_date, String start, String end, String appointment_reason, boolean terms_and_condition) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointment_date = appointment_date;
        this.start = start;
        this.end = end;
        this.appointment_reason = appointment_reason;
        this.terms_and_condition = terms_and_condition;
    }

    public Appointment() {
    }

    public Appointment(String patient, String name, String email, String phone, String doctor_id, String doctor, String appointment_date, String start, String end, String appointment_reason, boolean terms_and_condition, String status, String doctorAppointments) {
        this.patient = patient;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.doctor_id = doctor_id;
        this.doctor = doctor;
        this.appointment_date = appointment_date;
        this.start = start;
        this.end = end;
        this.appointment_reason = appointment_reason;
        this.terms_and_condition = terms_and_condition;
        this.status = status;
        this.doctorAppointments = doctorAppointments;
    }
}
