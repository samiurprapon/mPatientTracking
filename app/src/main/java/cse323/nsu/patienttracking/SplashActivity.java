package cse323.nsu.patienttracking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import cse323.nsu.patienttracking.authentication.AuthenticationActivity;
import cse323.nsu.patienttracking.doctor.DoctorHomeActivity;
import cse323.nsu.patienttracking.patient.PatientHomeActivity;

public class SplashActivity extends AppCompatActivity {

    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseApp.initializeApp(this);

        userType = getSharedPreferences("user", Context.MODE_PRIVATE).getString("type", null);

        new Handler().postDelayed(this::loginStatus, 750);
    }

    private void loginStatus() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null && userType != null) {
            Intent intent = null;

            if(userType.equals("doctor")) {
                intent = new Intent(SplashActivity.this, DoctorHomeActivity.class);
            } else  if(userType.equals("patient")) {
                intent = new Intent(SplashActivity.this, PatientHomeActivity.class);
            } else {
                userType = null;
                loginStatus();
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashActivity.this, AuthenticationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }
}