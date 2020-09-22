package com.example.maternalhealthapp.Data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.maternalhealthapp.BookAppointment;
import com.example.maternalhealthapp.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> implements Filterable {

    Context mCntx;
    public ArrayList<User> arrayList;
    public ArrayList<User> arrayListFiltered;

    public UserAdapter(Context mCntx, ArrayList<User> arrayList) {
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

    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View userView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_row, parent, false);
        return new UserAdapter.MyViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(final UserAdapter.MyViewHolder holder, final int position) {
        final User doc = arrayList.get(position);
        holder.name.setText(doc.firstName +" "+ doc.lastName);
        holder.phone.setText(doc.phone);
        holder.appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCntx, BookAppointment.class);
                intent.putExtra("doctor",doc.firstName +" "+ doc.lastName);
                intent.putExtra("doctor_id", doc.uid);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCntx.startActivity(intent);
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCntx, BookAppointment.class);
                intent.putExtra("doctor",doc.firstName +" "+ doc.lastName);
                intent.putExtra("phone_number", doc.phone);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCntx.startActivity(intent);
            }
        });
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, hospital, phone;
        public CircleImageView profileImage;
        public Button appointmentButton;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.doc_name);
            hospital = (TextView) view.findViewById(R.id.hospital_name);
            phone = (TextView) view.findViewById(R.id.doc_phone);
            profileImage = (CircleImageView) view.findViewById(R.id.profile_image);
            appointmentButton = (Button) view.findViewById(R.id.doc_book_btn);


        }
    }

    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(arrayListFiltered);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (User item : arrayListFiltered) {
                    if (item.firstName.toLowerCase().contains(filterPattern)) {
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
