package com.example.maternalhealthapp.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.maternalhealthapp.R;

import java.util.List;

public class PatientAdoptor extends RecyclerView.Adapter<PatientAdoptor.MyViewHolder> {
    private Context mContext;
    private List<Patient> patient_list;
    public PatientAdoptor(List<Patient> mTargetData) {
        patient_list = mTargetData;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,dob,phone,nextOfKin,nextOfKinContact;

        public MyViewHolder(View view) {
            super(view);
            name=(TextView)view.findViewById(R.id.txt_display_name);
            dob=(TextView)view.findViewById(R.id.txt_dob_date);
            phone=(TextView)view.findViewById(R.id.txt_number);
            nextOfKin=(TextView)view.findViewById(R.id.txt_display_net_of_kin);
            nextOfKinContact=(TextView)view.findViewById(R.id.txt_display_net_of_kin_contact);


        }
    }
    public PatientAdoptor(Context mContext, List<Patient> patientList) {
        this.mContext = mContext;
        this.patient_list= patientList;
    }
    @Override
    public PatientAdoptor.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View patientView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_row, parent, false);
        return new PatientAdoptor.MyViewHolder(patientView);
    }

    @Override
    public void onBindViewHolder(final PatientAdoptor.MyViewHolder holder, final int position) {
        final Patient patient = patient_list.get(position);
        holder.name.setText(patient.firstName +" "+ patient.lastName);
        holder.phone.setText(patient.phone);
        holder.dob.setText(patient.dob);
        holder.nextOfKin.setText(patient.next_of_kin);
        holder.nextOfKinContact.setText(patient.next_of_kin_contact);

    }


    @Override
    public int getItemCount() {
        try {
            return patient_list.size();
        } catch (Exception ex) {
            return 0;
        }
    }
}
