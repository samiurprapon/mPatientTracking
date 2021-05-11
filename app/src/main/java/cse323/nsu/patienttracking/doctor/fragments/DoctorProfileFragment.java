package cse323.nsu.patienttracking.doctor.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cse323.nsu.patienttracking.R;
import cse323.nsu.patienttracking.models.AvailableDoctor;
import cse323.nsu.patienttracking.models.Patient;
import cse323.nsu.patienttracking.utils.CustomProgressBar;


public class DoctorProfileFragment extends Fragment {

    private static DoctorProfileFragment fragment = null;

    private ImageView mAvatar;
    private MaterialTextView mName;
    private MaterialTextView mDegree;
    private MaterialTextView mExpertise;
    private MaterialTextView mPhone;
    private MaterialTextView mEmail;
    private MaterialTextView mWorkplace;
    private MaterialTextView mAbout;

    private ImageButton mSettings;

    CustomProgressBar progressBar;


    public DoctorProfileFragment() {
        // Required empty public constructor
    }

    public static DoctorProfileFragment newInstance() {
        if (fragment == null) {
            fragment = new DoctorProfileFragment();
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
        return inflater.inflate(R.layout.fragment_doctor_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAvatar = view.findViewById(R.id.iv_avatar);
        mName = view.findViewById(R.id.tv_name);
        mDegree = view.findViewById(R.id.tv_degree);
        mExpertise = view.findViewById(R.id.tv_expertise);
        mPhone = view.findViewById(R.id.tv_phone);
        mEmail = view.findViewById(R.id.doctor_email);
        mWorkplace = view.findViewById(R.id.doctor_address);
        mAbout = view.findViewById(R.id.tv_doctor_about);

        mSettings = view.findViewById(R.id.ib_profile_settings);

        progressBar = new CustomProgressBar(getContext());

        progressBar.show("loading...");
        parseDataFromFirebase();
        updatePlaceHolders(getSharedPreferences());

    }

    private void parseDataFromFirebase() {
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("doctors");

        reference.child(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AvailableDoctor doctor = task.getResult().getValue(AvailableDoctor.class);
                setSharedPreferences(doctor);
                updatePlaceHolders(doctor);
            }

            progressBar.hide();
        });
    }

    private void updatePlaceHolders(AvailableDoctor doctor) {

        if (doctor.getSex() != null && doctor.getSex().equals("male")) {
            mAvatar.setImageResource(R.drawable.ic_male_doctor);
        } else {
            mAvatar.setImageResource(R.drawable.ic_female_doctor);
        }

        if (doctor.getName() != null) {
            mName.setText(doctor.getName());
        }

        if (doctor.getDegree() != null) {
            mDegree.setText(doctor.getDegree());
        }

        if (doctor.getExpertise() != null) {
            mExpertise.setText(doctor.getExpertise());
        }

        if (doctor.getPhone() != null) {
            mPhone.setText(doctor.getPhone());
        }

        if (doctor.getName() != null) {
            mEmail.setText(doctor.getEmail());
        } else {
            mEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }

        if (doctor.getWorkplace() != null) {
            mWorkplace.setText(doctor.getWorkplace());

        }

        if (doctor.getAbout() != null) {
            mAbout.setText(doctor.getAbout());
        }
    }

    public void setSharedPreferences(AvailableDoctor doctor) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("doctorProfile", Context.MODE_PRIVATE).edit();

        editor.putString("name", doctor.getName());
        editor.putString("degree", doctor.getDegree());
        editor.putString("expertise", doctor.getExpertise());
        editor.putString("email", doctor.getEmail());
        editor.putString("phone", doctor.getPhone());
        editor.putString("sex", doctor.getSex());
        editor.putString("work", doctor.getWorkplace());
        editor.putString("about", doctor.getAbout());

        editor.apply();
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

}