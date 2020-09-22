package com.example.maternalhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.*;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TreatmentDetails extends AppCompatActivity {
    public TextView name, height, weight, bloodPressure, treatmentDate, doctor, nextAppointment, reason, medicalHistory, pelvicExam, doppler;
    TextView urineTest, bloodTest, prescription, recommendation;
    Button printButton;
    private static final int STORAGE_CODE = 1000;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_details);
        name = (TextView) findViewById(R.id.txt_details_patient_names);
        height = (TextView) findViewById(R.id.txt_details_patient_height);
        weight = (TextView) findViewById(R.id.txt_details_patient_weight);
        bloodPressure = (TextView) findViewById(R.id.txt_details_patient_blood_pressure);
        treatmentDate = (TextView) findViewById(R.id.details_treatmentDate_date);
        doctor = (TextView) findViewById(R.id.details_display_worked_on_by);
        nextAppointment = (TextView) findViewById(R.id.details_display_next_appointment_date);
        reason = (TextView) findViewById(R.id.details_appointment_reason);
        medicalHistory = (TextView) findViewById(R.id.details_medical_history);
        pelvicExam = (TextView) findViewById(R.id.details_pelvic_exam);
        doppler = (TextView) findViewById(R.id.details_dopplerFetalHeartRate);
        urineTest = (TextView) findViewById(R.id.details_txt_urine_test);
        bloodTest = (TextView) findViewById(R.id.details_txt_blood_test);
        prescription = (TextView) findViewById(R.id.details_txt_prescription);
        recommendation = (TextView) findViewById(R.id.details_txt_recommendation);
        printButton = (Button) findViewById(R.id.btn_print);
        //get details
        name.setText(getIntent().getStringExtra("patientname"));
        height.setText(String.valueOf(getIntent().getDoubleExtra("height", 0.0)));
        weight.setText(String.valueOf(getIntent().getDoubleExtra("weight", 0.0)));
        bloodPressure.setText(getIntent().getStringExtra("bloodPressure"));
        treatmentDate.setText(getIntent().getStringExtra("appointment_date"));
        doctor.setText(getIntent().getStringExtra("doctor"));
        nextAppointment.setText(getIntent().getStringExtra("nextAppointmentDate"));
        reason.setText(getIntent().getStringExtra("appointmentReason"));
        medicalHistory.setText(getIntent().getStringExtra("appointmentReason"));
        pelvicExam.setText(getIntent().getStringExtra("pelvicExam"));
        doppler.setText(getIntent().getStringExtra("dopplerFetalHeartRate"));
        urineTest.setText(getIntent().getStringExtra("urineTest"));
        bloodTest.setText(getIntent().getStringExtra("bloodTest"));
        prescription.setText(getIntent().getStringExtra("priscription"));
        recommendation.setText(getIntent().getStringExtra("recommendation"));
        //print to pdf using itexG
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                        requestPermissions(permissions, STORAGE_CODE);
                    } else {
                        createPdf();
                    }
                } else {
                    createPdf();
                }
            }
        });

    }

    private void createPdf() {
        Document document = new Document(PageSize.A4);
        document.setMargins(36, 36, 72, 36);
        //file name
        String patientName = name.getText().toString();
        String fileName = patientName + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        // file path
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + fileName + ".pdf";
        try {
            // create instance of pdf writer
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            //open the document for writing
            document.open();
            // get text from TextViews
            // String patientName=name.getText().toString();
            String mDoctor = doctor.getText().toString();
            //making doctor the auther
            document.addAuthor(mDoctor);
            document.addCreationDate();
            document.addHeader("header", "my content");


            // a table with three columns
            PdfPTable table = new PdfPTable(2);
            // the cell object
            PdfPCell cell;
            // we add a cell with colspan 3

            // now we add a cell with rowspan 2
            cell = new PdfPCell(new Phrase("Patient", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setRowspan(1);
            // cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(patientName));
            cell.setRowspan(1);
            //cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            // doctor
            cell = new PdfPCell(new Phrase("Height", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setRowspan(1);
            //cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(height.getText().toString()));
            cell.setRowspan(1);
            // cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            // now we add a cell with rowspan 2
            cell = new PdfPCell(new Phrase("Weight", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setRowspan(1);
            // cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(weight.getText().toString()));
            cell.setRowspan(1);
            // cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            // now we add a cell with rowspan 2
            cell = new PdfPCell(new Phrase("Blood Pressure", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setRowspan(1);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(bloodPressure.getText().toString()));
            cell.setRowspan(1);
            // cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            // now we add a cell with rowspan 2
            cell = new PdfPCell(new Phrase("Treatment Date", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setRowspan(1);
            //cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(treatmentDate.getText().toString()));
            cell.setRowspan(1);
            // cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            // now we add a cell with rowspan 2
            cell = new PdfPCell(new Phrase("Worked on by", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setRowspan(1);
            // cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(doctor.getText().toString()));
            cell.setRowspan(1);
            // cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            document.add(table);
            // now we add a cell with rowspan 2
            cell = new PdfPCell(new Phrase("Next Treatment Date", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            cell.setRowspan(1);
            // cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nextAppointment.getText().toString()));
            cell.setRowspan(1);
            //cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            // now we add a cell with rowspan 2
            // add paragraph
            document.add(new Paragraph("Reason for Treatment :", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            // add paragraph
            document.add(new Paragraph(reason.getText().toString()));
            document.add(new Paragraph("Medical History :", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            // add paragraph
            document.add(new Paragraph(medicalHistory.getText().toString()));
            document.add(new Paragraph("Pelvic Exam :", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            // add paragraph
            document.add(new Paragraph(pelvicExam.getText().toString()));

            document.add(new Paragraph("Doppler Fetal Heart Rate :", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            // add paragraph
            document.add(new Paragraph(doppler.getText().toString()));
            document.add(new Paragraph("Urine Test :", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            // add paragraph
            document.add(new Paragraph(urineTest.getText().toString()));
            document.add(new Paragraph("Blood Test :", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            // add paragraph
            document.add(new Paragraph(bloodTest.getText().toString()));
            document.add(new Paragraph("Prescription :", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            // add paragraph
            document.add(new Paragraph(prescription.getText().toString()));
            document.add(new Paragraph("Recommendation :", new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
            // add paragraph
            document.add(new Paragraph(recommendation.getText().toString()));
            document.close();
            // showing notification that document has been saved
            Toast.makeText(this, fileName + ".pdf\nis saved to\n " + filePath, Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            // if things go south
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //handle permission result

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted

                } else {
                    //permission denied
                    Toast.makeText(getApplicationContext(), "Permissions Denied!.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


}
