package cse323.nsu.patienttracking.doctor.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cse323.nsu.patienttracking.R;
import cse323.nsu.patienttracking.models.Patient;
import de.hdodenhof.circleimageview.CircleImageView;


public class PatientProfileFragment extends Fragment {

    private TextView mName;
    private TextView mAge;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mBlood;
    private TextView mLocation;

    CircleImageView mAvatar;

    Patient patient;

    public PatientProfileFragment(Patient patient) {
        this.patient = patient;
    }

    public static PatientProfileFragment newInstance(Patient patient) {
        return new PatientProfileFragment(patient);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAvatar = view.findViewById(R.id.iv_profile);
        mName = view.findViewById(R.id.tv_name);
        mAge = view.findViewById(R.id.tv_age);
        mPhone = view.findViewById(R.id.tv_phone);
        mEmail = view.findViewById(R.id.tv_email);
        mBlood = view.findViewById(R.id.tv_blood);
        mLocation = view.findViewById(R.id.tv_location);

        if (patient != null) {
            updatePlaceholders(patient);
        }

    }

    private void updatePlaceholders(Patient patient) {

        if (patient.getSex().equals("male")) {
            mAvatar.setImageResource(R.drawable.ic_male);
        } else {
            mAvatar.setImageResource(R.drawable.ic_female);
        }

        if (patient.getName() != null) {
            mName.setText(patient.getName());
        }

        if (patient.getPhone() != null) {
            mPhone.setText(patient.getPhone());
        }

        if (patient.getEmail() != null) {
            mEmail.setText(patient.getEmail());
        }

        if (patient.getBlood() != null) {
            mBlood.setText(patient.getBlood());
        }

        if (patient.getLocation() != null) {
            mLocation.setText(patient.getLocation());
        }


        if (patient.getAge() != 0) {
            mAge.setText(String.valueOf(patient.getAge()));
        }
    }
}