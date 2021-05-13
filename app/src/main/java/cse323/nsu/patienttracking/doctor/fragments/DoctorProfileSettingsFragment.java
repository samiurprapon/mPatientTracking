package cse323.nsu.patienttracking.doctor.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cse323.nsu.patienttracking.R;
import cse323.nsu.patienttracking.authentication.AuthenticationActivity;
import cse323.nsu.patienttracking.models.AvailableDoctor;
import cse323.nsu.patienttracking.utils.CustomProgressBar;

public class DoctorProfileSettingsFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static DoctorProfileSettingsFragment fragment = null;

    MaterialButton mLogout;
    CustomProgressBar progressBar;

    private ImageView mAvatar;
    private AppCompatEditText mName;
    private AppCompatEditText mDegree;
    private AppCompatEditText mExpertise;
    private AppCompatEditText mPhone;
    private AppCompatEditText mEmail;
    private AppCompatEditText mWorkPlace;
    private AppCompatEditText mAbout;
    MaterialButton mUpdate;

    private String avatar = "female";

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

        mAvatar = view.findViewById(R.id.iv_avatar);
        mName = view.findViewById(R.id.et_name);
        mDegree = view.findViewById(R.id.et_degree);
        mExpertise = view.findViewById(R.id.et_expertise);
        mPhone = view.findViewById(R.id.et_phone);
        mEmail = view.findViewById(R.id.doctor_email);
        mWorkPlace = view.findViewById(R.id.doctor_address);
        mAbout = view.findViewById(R.id.tv_doctor_about);

        mUpdate = view.findViewById(R.id.mb_update);

        updatePlaceholders(getSharedPreferences());

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

        mAvatar.setOnClickListener(v -> {
            mAvatar.setEnabled(false);
            toggleAvatar();
            mAvatar.setEnabled(true);
        });

        mUpdate.setOnClickListener(v -> {
            // update information
            updateInformation();
        });
    }

    private void updateInformation() {

        String name = mName.getText().toString().trim();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String phone = mPhone.getText().toString().trim();
        String about = mAbout.getText().toString().trim();
        String degree = mDegree.getText().toString().trim();
        String expertise = mExpertise.getText().toString().trim();
        String workplace = mWorkPlace.getText().toString().trim();

        if (name.equals("") || phone.isEmpty() || about.isEmpty() || degree.isEmpty() || expertise.isEmpty() || workplace.isEmpty()) {
            return;
        }


        progressBar.show("saving...");
        AvailableDoctor doctor = new AvailableDoctor(name, email, degree, workplace, expertise, avatar, phone, about);
        syncWithFirebase(doctor);

    }

    private void syncWithFirebase(AvailableDoctor doctor) {
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();

        assert uid != null;
        mReference.child("doctors").child(uid).setValue(doctor).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // change intent to home
                changeFragment(DoctorProfileFragment.newInstance());

                Toast.makeText(getContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error Occurs!", Toast.LENGTH_SHORT).show();
            }

            progressBar.hide();
        });
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.left_enter, R.anim.right_out);
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }

    private void toggleAvatar() {

        if (avatar.equals("female")) {
            mAvatar.animate().rotationBy(360f).setDuration(360).start();
            mAvatar.setImageResource(R.drawable.ic_male_doctor);
            avatar = "male";
        } else {
            mAvatar.animate().rotationBy(-360f).setDuration(360).start();
            mAvatar.setImageResource(R.drawable.ic_female_doctor);
            avatar = "female";
        }
    }

    private void updatePlaceholders(AvailableDoctor doctor) {

        mEmail.setEnabled(false);


        if (doctor.getSex() != null && doctor.getSex().equals("male")) {
            mAvatar.setImageResource(R.drawable.ic_male_doctor);
        } else {
            mAvatar.setImageResource(R.drawable.ic_female_doctor);
        }

        if (doctor.getName() != null) {
            mName.setText(doctor.getName());
        }

        if (doctor.getPhone() != null) {
            mPhone.setText(doctor.getPhone());
        }

        if (doctor.getEmail() != null) {
            mEmail.setText(doctor.getEmail());
        } else {
            mEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }

        if (doctor.getDegree() != null) {
            mDegree.setText(doctor.getDegree());
        }
        if (doctor.getExpertise() != null) {
            mExpertise.setText(doctor.getExpertise());
        }
        if (doctor.getExpertise() != null) {
            mWorkPlace.setText(doctor.getWorkplace());
        }
        if (doctor.getAbout() != null) {
            mAbout.setText(doctor.getAbout());
        }

    }

    private AvailableDoctor getSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences("doctorProfile", Context.MODE_PRIVATE);

        String name = preferences.getString("name", null);
        String degree = preferences.getString("degree", null);
        String email = preferences.getString("email", null);
        String expertise = preferences.getString("expertise", null);
        String phone = preferences.getString("phone", null);
        String sex = preferences.getString("sex", null);
        String workplace = preferences.getString("work", null);
        String about = preferences.getString("about", null);

        return new AvailableDoctor(name, email, degree, workplace, expertise, sex, phone, about);
    }


    private void pavilion() {
        Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}