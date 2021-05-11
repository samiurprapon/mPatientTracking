package cse323.nsu.patienttracking.doctor.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cse323.nsu.patienttracking.R;

public class DoctorAppointmentsFragment extends Fragment {

    private static DoctorAppointmentsFragment fragment = null;

    public DoctorAppointmentsFragment() {
        // Required empty public constructor
    }

    public static DoctorAppointmentsFragment newInstance() {
        if (fragment == null) {
            fragment = new DoctorAppointmentsFragment();
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_appointments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

}