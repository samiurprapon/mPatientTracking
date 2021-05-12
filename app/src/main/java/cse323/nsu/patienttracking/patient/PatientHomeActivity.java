package cse323.nsu.patienttracking.patient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import cse323.nsu.patienttracking.R;
import cse323.nsu.patienttracking.authentication.AuthenticationActivity;
import cse323.nsu.patienttracking.utils.CustomProgressBar;

public class PatientHomeActivity extends AppCompatActivity {

    CardView mProfile;
    CardView mMyPrescription;
    CardView mAvailableDoctors;
    CardView mAppointment;

    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_patient_home);

        mProfile = findViewById(R.id.cv_profile);
        mMyPrescription = findViewById(R.id.cv_prescriptions);
        mAvailableDoctors = findViewById(R.id.cv_doctors);
        mAppointment = findViewById(R.id.cv_appointment);

        mProfile.setOnClickListener(view -> {
            Intent intent = new Intent(PatientHomeActivity.this, PatientProfileActivity.class);
            startActivity(intent);
        });

        mMyPrescription.setOnClickListener(view -> {
            Intent intent = new Intent(PatientHomeActivity.this, MyPrescriptionsActivity.class);
            startActivity(intent);
        });

        mAvailableDoctors.setOnClickListener(view -> {
            Intent intent = new Intent(PatientHomeActivity.this, AvailableDoctorsActivity.class);
            startActivity(intent);
        });

        mAppointment.setOnClickListener(view -> {
            Intent intent = new Intent(PatientHomeActivity.this, MakeAppointmentActivity.class);
            startActivity(intent);
        });


    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler(Looper.myLooper()).postDelayed(() -> exit = false, 3 * 1000);

        }
    }
}