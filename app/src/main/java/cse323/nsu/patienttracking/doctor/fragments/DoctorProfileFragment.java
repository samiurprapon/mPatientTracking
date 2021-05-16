package cse323.nsu.patienttracking.doctor.fragments;

import android.annotation.SuppressLint;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cse323.nsu.patienttracking.R;
import cse323.nsu.patienttracking.models.AvailableDoctor;
import cse323.nsu.patienttracking.utils.CustomProgressBar;


public class DoctorProfileFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static DoctorProfileFragment fragment = null;

    private ImageView mAvatar;
    private MaterialTextView mName;
    private MaterialTextView mDegree;
    private MaterialTextView mExpertise;
    private MaterialTextView mPhone;
    private MaterialTextView mEmail;
    private MaterialTextView mWorkplace;
    private MaterialTextView mAbout;

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
        mDegree = view.findViewById(R.id.tv_age);
        mExpertise = view.findViewById(R.id.tv_date_time);
        mPhone = view.findViewById(R.id.tv_phone);
        mEmail = view.findViewById(R.id.doctor_email);
        mWorkplace = view.findViewById(R.id.doctor_address);
        mAbout = view.findViewById(R.id.tv_doctor_about);

        ImageButton mSettings = view.findViewById(R.id.ib_profile_settings);

        progressBar = new CustomProgressBar(getContext());

        progressBar.show("loading...");
        parseDataFromFirebase();
        updatePlaceHolders(getSharedPreferences());

        mSettings.setOnClickListener(v -> {
            // change
            changeFragment(DoctorProfileSettingsFragment.newInstance());
        });

    }

    private void parseDataFromFirebase() {
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("doctors");

        assert uid != null;
        reference.child(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AvailableDoctor doctor = task.getResult().getValue(AvailableDoctor.class);

                if(doctor != null) {
                    setSharedPreferences(doctor);
                    updatePlaceHolders(doctor);
                }
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

        if(doctor.getName() != null) {
            editor.putString("name", doctor.getName());
        }

        if(doctor.getDegree() != null) {
            editor.putString("degree", doctor.getDegree());
        }
        if(doctor.getExpertise() != null) {
            editor.putString("expertise", doctor.getExpertise());
        }
        if(doctor.getEmail() != null) {
            editor.putString("email", doctor.getEmail());
        }
        if(doctor.getPhone() != null) {
            editor.putString("phone", doctor.getPhone());
        }
        if(doctor.getName() != null) {
            editor.putString("sex", doctor.getSex());
        }
        if(doctor.getWorkplace() != null) {
            editor.putString("work", doctor.getWorkplace());
        }
        if(doctor.getAbout() != null) {
            editor.putString("about", doctor.getAbout());
        }

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

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.left_enter, R.anim.right_out);
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }
}