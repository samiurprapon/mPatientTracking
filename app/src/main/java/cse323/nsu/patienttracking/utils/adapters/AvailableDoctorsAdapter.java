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
import cse323.nsu.patienttracking.models.AvailableDoctor;

public class AvailableDoctorsAdapter extends RecyclerView.Adapter<AvailableDoctorsAdapter.ViewHolder> {

    Context context;
    LayoutInflater layoutInflater;

    private List<AvailableDoctor> availableDoctorList;

    public AvailableDoctorsAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        availableDoctorList = new ArrayList<>();
    }

    @NonNull
    @Override
    public AvailableDoctorsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_doctor, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableDoctorsAdapter.ViewHolder holder, int position) {
        if (availableDoctorList != null) {
            AvailableDoctor availableDoctor = availableDoctorList.get(position);

            holder.mAvatar.setImageResource(availableDoctor.getSex().equals("male") ? R.drawable.illustration_male_doctor : R.drawable.illustration_female_doctor);

            holder.mName.setText(availableDoctor.getName());
            holder.mDegree.setText(availableDoctor.getDegree());
            holder.mWorkAt.setText(availableDoctor.getWorkAt());
            holder.mExpertise.setText(availableDoctor.getExpertise());

            holder.mContainer.setOnClickListener(v -> {
                //ToDo
                // open doctor profile activity
            });

        }
    }

    @Override
    public int getItemCount() {
        if (availableDoctorList != null) {
            return availableDoctorList.size();
        } else {
            return 0;
        }
    }

    public void setAvailableDoctorList(List<AvailableDoctor> availableDoctorList) {
        this.availableDoctorList = availableDoctorList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView mContainer;
        ImageView mAvatar;
        TextView mName;
        TextView mDegree;
        TextView mWorkAt;
        TextView mExpertise;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mContainer = itemView.findViewById(R.id.container);

            mAvatar = itemView.findViewById(R.id.iv_avatar);
            mName = itemView.findViewById(R.id.tv_name);
            mDegree = itemView.findViewById(R.id.tv_degree);
            mExpertise = itemView.findViewById(R.id.tv_expertise);
            mWorkAt = itemView.findViewById(R.id.tv_chamber);

        }
    }


}
