package cse323.nsu.patienttracking.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import cse323.nsu.patienttracking.R;
import cse323.nsu.patienttracking.models.Patient;

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.ViewHolder> {

    Context context;
    LayoutInflater layoutInflater;

    private List<Patient> patientList;

    public PatientsAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        patientList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PatientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_patient, parent, false);

        return new PatientsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsAdapter.ViewHolder holder, int position) {
        if (patientList != null) {
            Patient patient = patientList.get(position);

            holder.mAvatar.setImageResource(patient.getSex().equals("male") ? R.drawable.illustration_male_doctor : R.drawable.illustration_female_doctor);

            if(patient.getName() != null) {
                holder.mName.setText(patient.getName());
            }
            if(patient.getAge() != 0) {
                holder.mAge.setText(String.valueOf(patient.getAge()));
            }
            if(patient.getLocation() != null) {
                holder.mLocation.setText(patient.getLocation());
            }

        }
    }

    @Override
    public int getItemCount() {
        if (patientList != null) {
            return patientList.size();
        } else {
            return 0;
        }
    }

    public void setPatientList(List<Patient> patientList) {
        this.patientList = patientList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView mContainer;
        ImageView mAvatar;

        TextView mName;
        TextView mAge;
        TextView mLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mContainer = itemView.findViewById(R.id.container);

            mAvatar = itemView.findViewById(R.id.iv_avatar);
            mName = itemView.findViewById(R.id.tv_name);
            mAge = itemView.findViewById(R.id.tv_age);
            mLocation = itemView.findViewById(R.id.tv_address);
        }

    }

}
