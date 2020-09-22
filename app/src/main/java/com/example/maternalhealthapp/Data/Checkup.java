package com.example.maternalhealthapp.Data;

public class Checkup {
    public String appointmentId;
    public String appointmentReason;
    public String patientId;
    public String patientName;
    public String doctorId;
    public String doctorName;
    public String checkupId;
    public String medicalHistory;
    public String bloodPressure;
    public double height;
    public double weight;
    public String pelvicExam;
    public String dopplerFetalHeartRate;
    public String urineTest;
    public String bloodTest;
    public String recommendation;
    public String nextAppointmentDate;
    public String priscription;
    public String treatmentDate;

    public Checkup(String appointmentId, String appointmentReason, String patientId, String patientName, String doctorId, String doctorName, String medicalHistory, String bloodPressure, double height, double weight, String pelvicExam, String dopplerFetalHeartRate, String urineTest, String bloodTest, String recommendation, String priscription, String nextAppointmentDate, String treatmentDate) {
        this.appointmentId = appointmentId;
        this.appointmentReason = appointmentReason;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.medicalHistory = medicalHistory;
        this.bloodPressure = bloodPressure;
        this.height = height;
        this.weight = weight;
        this.pelvicExam = pelvicExam;
        this.dopplerFetalHeartRate = dopplerFetalHeartRate;
        this.urineTest = urineTest;
        this.bloodTest = bloodTest;
        this.recommendation = recommendation;
        this.priscription = priscription;
        this.nextAppointmentDate = nextAppointmentDate;
        this.treatmentDate = treatmentDate;
    }

    public Checkup() {

    }
}
