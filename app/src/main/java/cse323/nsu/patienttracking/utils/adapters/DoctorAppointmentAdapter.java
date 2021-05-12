package cse323.nsu.patienttracking.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

import cse323.nsu.patienttracking.R;
import cse323.nsu.patienttracking.models.DoctorAppointment;

public class DoctorAppointmentAdapter extends RecyclerView.Adapter<DoctorAppointmentAdapter.ViewHolder> {

    Context context;
    LayoutInflater layoutInflater;

    private List<DoctorAppointment> appointmentList;

    public DoctorAppointmentAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        appointmentList = new ArrayList<>();
    }

    @NonNull
    @Override
    public DoctorAppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_appointment, parent, false);

        return new DoctorAppointmentAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAppointmentAdapter.ViewHolder holder, int position) {
        if (appointmentList != null) {
            DoctorAppointment appointment = appointmentList.get(position);

            holder.mAvatar.setImageResource(appointment.getDoctorSex().equals("male") ? R.drawable.illustration_male_doctor : R.drawable.illustration_female_doctor);

            holder.mDoctorName.setText(appointment.getDoctorName());
            holder.mAppointmentId.setText(appointment.getId());
            holder.mType.setText(appointment.getType());
            holder.mDate.setText(appointment.getDate());
            holder.mStatus.setText(appointment.getStatus());

            holder.mContainer.setOnClickListener(v -> {
                //ToDo
                // open Appointment Page activity
            });

        }
    }

    @Override
    public int getItemCount() {
        if (appointmentList != null) {
            return appointmentList.size();
        } else {
            return 0;
        }
    }

    public void setDoctorAppointmentList(List<DoctorAppointment> appointmentList) {
        this.appointmentList = appointmentList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView mContainer;
        ImageView mAvatar;
        MaterialTextView mDoctorName;
        MaterialTextView mAppointmentId;
        MaterialTextView mDate;
        MaterialTextView mStatus;
        MaterialTextView mType;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mContainer = itemView.findViewById(R.id.container);

            mAvatar = itemView.findViewById(R.id.iv_avatar);

            mDoctorName = itemView.findViewById(R.id.tv_name);
            mAppointmentId = itemView.findViewById(R.id.tv_appointment_id);
            mStatus = itemView.findViewById(R.id.tv_status);
            mDate = itemView.findViewById(R.id.tv_date_time);
            mType = itemView.findViewById(R.id.tv_appointment_type);

        }
    }


}
