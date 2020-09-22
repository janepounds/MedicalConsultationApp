package com.example.maternalhealthapp.Data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.maternalhealthapp.ApproveAppointment;
import com.example.maternalhealthapp.Examination;
import com.example.maternalhealthapp.R;

import java.util.ArrayList;
import java.util.List;

public class CheckupAppointmentAdapter extends RecyclerView.Adapter<CheckupAppointmentAdapter.MyViewHolder> implements Filterable {
    Context mCntx;
    public ArrayList<Appointment> arrayList;
    public ArrayList<Appointment> arrayListFiltered;

    public CheckupAppointmentAdapter(Context mCntx, ArrayList<Appointment> arrayList) {
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
        public TextView name, email, phone, date, start, endtime, details, appointmentAction;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.check_up_patientname);
            email = (TextView) view.findViewById(R.id.check_up_patient_email);
            phone = (TextView) view.findViewById(R.id.check_up_patient_phone_number);
            date = (TextView) view.findViewById(R.id.check_up_appointment_date);
            start = (TextView) view.findViewById(R.id.check_up_appointment_start_time);
            endtime = (TextView) view.findViewById(R.id.check_up_appointment_end_time);
            details = (TextView) view.findViewById(R.id.check_up_appointment_discription);
            appointmentAction = (TextView) view.findViewById(R.id.check_up_appointment_action_btn);


        }
    }

    @Override
    public CheckupAppointmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View userView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_checkup_row, parent, false);
        return new CheckupAppointmentAdapter.MyViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(final CheckupAppointmentAdapter.MyViewHolder holder, final int position) {
        final Appointment appointment = arrayList.get(position);
        holder.name.setText(appointment.name);
        holder.phone.setText(appointment.phone);
        holder.email.setText(appointment.email);
        holder.date.setText(appointment.appointment_date);
        holder.start.setText(appointment.start);
        holder.endtime.setText(appointment.end);
        holder.details.setText(appointment.appointment_reason);
        holder.appointmentAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCntx, Examination.class);
                intent.putExtra("name", appointment.name);
                intent.putExtra("email", appointment.email);
                intent.putExtra("reason", appointment.appointment_reason);
                intent.putExtra("appointment_date", appointment.appointment_date);
                intent.putExtra("doctor", appointment.doctor);
                intent.putExtra("doctor_id", appointment.doctor_id);
                intent.putExtra("end_time", appointment.end);
                intent.putExtra("start_time", appointment.start);
                intent.putExtra("patient", appointment.patient);
                intent.putExtra("phone", appointment.phone);
                intent.putExtra("status", appointment.status);
                intent.putExtra("terms_and_conditions", appointment.terms_and_condition);
                intent.putExtra("id", appointment.appointmentId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCntx.startActivity(intent);
            }
        });

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
