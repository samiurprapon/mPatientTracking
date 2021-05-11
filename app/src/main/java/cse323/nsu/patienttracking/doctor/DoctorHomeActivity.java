package cse323.nsu.patienttracking.doctor;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import cse323.nsu.patienttracking.R;
import cse323.nsu.patienttracking.doctor.fragments.DoctorAppointmentsFragment;
import cse323.nsu.patienttracking.doctor.fragments.DoctorHomeFragment;
import cse323.nsu.patienttracking.doctor.fragments.DoctorPatientsFragment;
import cse323.nsu.patienttracking.doctor.fragments.DoctorProfileFragment;
import cse323.nsu.patienttracking.utils.CustomProgressBar;


public class DoctorHomeActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    CustomProgressBar progressBar;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_doctor_home);

        progressBar = new CustomProgressBar(this);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        fragmentManager = getSupportFragmentManager();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.navigation_patients:
                    fragment = DoctorPatientsFragment.newInstance();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_appointment:
                    fragment = DoctorAppointmentsFragment.newInstance();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    fragment = DoctorProfileFragment.newInstance();
                    loadFragment(fragment);
                    return true;

                default:
                    fragment = DoctorHomeFragment.newInstance();
                    loadFragment(fragment);
                    return true;
            }

        });

        // load the store fragment by default
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        loadFragment(new DoctorHomeFragment());

    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.left_enter, R.anim.right_out);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            fragmentManager.popBackStack();

            if (count == 1) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_container, new DoctorHomeFragment())
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.left_enter, R.anim.right_out)
                        .commit();
            }

        }
    }
}