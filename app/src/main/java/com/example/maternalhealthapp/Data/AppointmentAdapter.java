package com.example.maternalhealthapp.Data;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maternalhealthapp.AppointmentActivity;
import com.example.maternalhealthapp.ApproveAppointment;
import com.example.maternalhealthapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> implements Filterable {
    Context mCntx;
    public ArrayList<Appointment> arrayList;
    public ArrayList<Appointment> arrayListFiltered;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = database.getInstance().getReference();

    public AppointmentAdapter(Context mCntx, ArrayList<Appointment> arrayList) {
        this.mCntx = mCntx;
        this.arrayList = arrayList;
        this.arrayListFiltered = new ArrayList<>(arrayList);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, email, phone, date, start, endtime, details, status, appointmentAction;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.patient_patientname);
            email = (TextView) view.findViewById(R.id.patient_patient_email);
            phone = (TextView) view.findViewById(R.id.patient_patient_phone_number);
            date = (TextView) view.findViewById(R.id.patient_appointment_date);
            start = (TextView) view.findViewById(R.id.patient_appointment_start_time);
            endtime = (TextView) view.findViewById(R.id.patient_appointment_end_time);
            details = (TextView) view.findViewById(R.id.patient_appointment_discription);
            status = (TextView) view.findViewById(R.id.patient_appointment_status_btn);
            appointmentAction = (TextView) view.findViewById(R.id.patient_appointment_action_btn);


        }
    }

    @Override
    public AppointmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View userView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_appointment_row, parent, false);
        return new AppointmentAdapter.MyViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(final AppointmentAdapter.MyViewHolder holder, final int position) {
        final Appointment appointment = arrayList.get(position);
        holder.name.setText(appointment.name);
        holder.phone.setText(appointment.phone);
        holder.email.setText(appointment.email);
        holder.date.setText(appointment.appointment_date);
        holder.start.setText(appointment.start);
        holder.endtime.setText(appointment.end);
        holder.details.setText(appointment.appointment_reason);
        holder.status.setText(appointment.status);
        holder.appointmentAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(mCntx);

// 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.cancel_message)
                        .setTitle(R.string.dialog_title);
                // Add the buttons
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String doctor_id = appointment.doctor_id;
                        final String doctor = appointment.doctor;
                        final String appointment_date = appointment.appointment_date;
                        final String appointmentReason = appointment.appointment_reason;
                        final String start_time = appointment.start;
                        final String end_time_ = appointment.end;
                        final String name = appointment.name;
                        final String email = appointment.email;
                        final String phone = appointment.phone;
                        final String userId = appointment.patient;
                        final String appointmentId = appointment.appointmentId;
                        final String doctorAppointments = doctor_id + "Cancelled";


                        Appointment appointment = new Appointment(userId, name, email, phone, doctor_id, doctor, appointment_date, start_time, end_time_, appointmentReason, true, "Cancelled", doctorAppointments);
                        // progressBar.setVisibility(View.VISIBLE);
                        mDatabase.child("appointments").child(appointmentId).setValue(appointment)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Write was successful!
                                        Toast.makeText(mCntx, "canceled!", Toast.LENGTH_LONG).show();
                                        //    progressBar.setVisibility(View.GONE);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Write failed
                                        Toast.makeText(mCntx, "Failed to cancel appointment!", Toast.LENGTH_LONG).show();
                                    }
                                });
                        // User clicked OK button
                        Toast.makeText(mCntx,
                                appointment.appointmentId, Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });


// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //set status color
        String state = appointment.status;
        if (state.equals("Rejected")) {
            holder.status.setBackgroundColor(Color.parseColor("#e53935"));
        }
        if (state.equals("Approved")) {
            holder.status.setBackgroundColor(Color.parseColor("#008000"));
        }

    }


    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Appointment> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayListFiltered);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Appointment item : arrayListFiltered) {
                    if (item.name.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            arrayList.clear();
            arrayList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
