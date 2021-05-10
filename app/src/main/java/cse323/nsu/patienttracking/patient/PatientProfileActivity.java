package cse323.nsu.patienttracking.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import cse323.nsu.patienttracking.R;

public class PatientProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_patient_profile);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}