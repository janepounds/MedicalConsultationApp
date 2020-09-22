package com.example.maternalhealthapp.Data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maternalhealthapp.Examination;
import com.example.maternalhealthapp.R;
import com.example.maternalhealthapp.TreatmentDetails;

import java.util.ArrayList;
import java.util.List;

public class TreatmentAdapter extends RecyclerView.Adapter<TreatmentAdapter.MyViewHolder> implements Filterable {
    Context mCntx;
    public ArrayList<Checkup> arrayList;
    public ArrayList<Checkup> arrayListFiltered;

    public TreatmentAdapter(Context mCntx, ArrayList<Checkup> arrayList) {
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
        public TextView name, treatmentDate, workdOnBy, nextAppointDate, recommendation;
        public CardView treatmentCard;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txt_display_patient_names);
            treatmentDate = (TextView) view.findViewById(R.id.txt_treatmentDate_date);
            workdOnBy = (TextView) view.findViewById(R.id.txt_display_worked_on_by);
            nextAppointDate = (TextView) view.findViewById(R.id.txt_display_next_appointment_date);
            recommendation = (TextView) view.findViewById(R.id.txt_recommendation);
            treatmentCard = (CardView) view.findViewById(R.id.treatment_card_view);

        }
    }

    @Override
    public TreatmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View userView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.treatment_row, parent, false);
        return new TreatmentAdapter.MyViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(final TreatmentAdapter.MyViewHolder holder, final int position) {
        final Checkup treatment = arrayList.get(position);
        holder.name.setText(treatment.patientName);
        holder.treatmentDate.setText(treatment.treatmentDate);
        holder.workdOnBy.setText(treatment.doctorName);
        holder.nextAppointDate.setText(treatment.nextAppointmentDate);
        holder.recommendation.setText(treatment.recommendation);
        holder.treatmentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCntx, TreatmentDetails.class);
                intent.putExtra("patientname", treatment.patientName);
                intent.putExtra("patientId", treatment.patientId);
                intent.putExtra("appointmentId", treatment.appointmentId);
                intent.putExtra("appointmentReason", treatment.appointmentReason);
                intent.putExtra("appointment_date", treatment.treatmentDate);
                intent.putExtra("doctor", treatment.doctorName);
                intent.putExtra("doctor_id", treatment.doctorId);
                intent.putExtra("medical_history", treatment.medicalHistory);
                intent.putExtra("bloodPressure", treatment.bloodPressure);
                intent.putExtra("height", treatment.height);
                intent.putExtra("weight", treatment.weight);
                intent.putExtra("pelvicExam", treatment.pelvicExam);
                intent.putExtra("dopplerFetalHeartRate", treatment.dopplerFetalHeartRate);
                intent.putExtra("urineTest", treatment.urineTest);
                intent.putExtra("bloodTest", treatment.bloodTest);
                intent.putExtra("recommendation", treatment.recommendation);
                intent.putExtra("nextAppointmentDate", treatment.nextAppointmentDate);
                intent.putExtra("priscription", treatment.priscription);
                intent.putExtra("treatmentDate", treatment.treatmentDate);

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
            List<Checkup> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayListFiltered);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Checkup item : arrayListFiltered) {
                    if (item.patientName.toLowerCase().contains(filterPattern)) {
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
