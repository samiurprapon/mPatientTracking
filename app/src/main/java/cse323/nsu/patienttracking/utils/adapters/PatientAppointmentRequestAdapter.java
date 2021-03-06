package cse323.nsu.patienttracking.utils.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import cse323.nsu.patienttracking.R;
import cse323.nsu.patienttracking.models.DoctorAppointment;
import cse323.nsu.patienttracking.utils.CustomProgressBar;
import de.hdodenhof.circleimageview.CircleImageView;

public class PatientAppointmentRequestAdapter extends RecyclerView.Adapter<PatientAppointmentRequestAdapter.ViewHolder> {

    Context context;
    LayoutInflater layoutInflater;

    CustomProgressBar progressBar;

    private List<DoctorAppointment> appointmentList;

    public PatientAppointmentRequestAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        appointmentList = new ArrayList<>();
        progressBar = new CustomProgressBar(context);
    }

    @NonNull
    @Override
    public PatientAppointmentRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_appointment_request, parent, false);

        return new PatientAppointmentRequestAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAppointmentRequestAdapter.ViewHolder holder, int position) {
        if (appointmentList != null) {
            DoctorAppointment appointment = appointmentList.get(position);

            holder.mAvatar.setImageResource(appointment.getPatientSex() != null && appointment.getPatientSex().equals("male")? R.drawable.ic_male : R.drawable.ic_female);

            holder.mPatientName.setText(appointment.getPatientName());
            holder.mLocation.setText(appointment.getPatientLocation());
            holder.mType.setText(appointment.getType());
            holder.mDate.setText(appointment.getDate());
            holder.mMessage.setText(appointment.getMessage());

            holder.mAccept.setOnClickListener(v -> {
                progressBar.show("");
                acceptRequest(appointment);
            });

            holder.mCancel.setOnClickListener(v -> {
                progressBar.show("");
                canceledRequest(appointment);
            });

        }
    }

    private void acceptRequest(DoctorAppointment appointment) {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("appointments");
        appointment.setStatus("accepted");

        mReference.child(appointment.getId()).setValue(appointment).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Appointment accepted!", Toast.LENGTH_SHORT).show();
                progressBar.hide();
                notifyDataSetChanged();
            } else {
                progressBar.hide();
            }
        });

    }

    private void canceledRequest(DoctorAppointment appointment) {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("appointments");
        appointment.setStatus("cancelled");

        mReference.child(appointment.getId()).setValue(appointment).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Appointment cancelled!", Toast.LENGTH_SHORT).show();
                progressBar.hide();
                notifyDataSetChanged();
            } else {
                progressBar.hide();
            }
        });

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

        MaterialButton mAccept;
        MaterialButton mCancel;

        CircleImageView mAvatar;
        MaterialTextView mPatientName;
        MaterialTextView mLocation;

        MaterialTextView mDate;
        MaterialTextView mMessage;
        MaterialTextView mType;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mAvatar = itemView.findViewById(R.id.cv_avatar);
            mPatientName = itemView.findViewById(R.id.tv_username);
            mLocation = itemView.findViewById(R.id.tv_location);
            mMessage = itemView.findViewById(R.id.tv_message);
            mDate = itemView.findViewById(R.id.tv_date);
            mType = itemView.findViewById(R.id.tv_appointment_type);

            mAccept = itemView.findViewById(R.id.mb_accept);
            mCancel = itemView.findViewById(R.id.mb_cancel);

        }
    }


}
