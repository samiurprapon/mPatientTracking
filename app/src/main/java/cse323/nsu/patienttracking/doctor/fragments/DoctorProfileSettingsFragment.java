package cse323.nsu.patienttracking.doctor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import cse323.nsu.patienttracking.R;
import cse323.nsu.patienttracking.authentication.AuthenticationActivity;
import cse323.nsu.patienttracking.utils.CustomProgressBar;

public class DoctorProfileSettingsFragment extends Fragment {

    private static DoctorProfileSettingsFragment fragment = null;

    MaterialButton mLogout;
    CustomProgressBar progressBar;

    public DoctorProfileSettingsFragment() {
        // Required empty public constructor
    }

    public static DoctorProfileSettingsFragment newInstance() {
        if (fragment == null) {
            fragment = new DoctorProfileSettingsFragment();
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
        return inflater.inflate(R.layout.fragment_doctor_profile_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLogout = view.findViewById(R.id.mb_logout);
        progressBar = new CustomProgressBar(getContext());

        mLogout.setOnClickListener(v -> {
            progressBar.show("Logging out...");

            new Handler(Looper.myLooper()).postDelayed(() -> {
                FirebaseAuth.getInstance().signOut();

                progressBar.hide();

                pavilion();

            }, 800);
        });
    }

    private void pavilion() {
        Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}