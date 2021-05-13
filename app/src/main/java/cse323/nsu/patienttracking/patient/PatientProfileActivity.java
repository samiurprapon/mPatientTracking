package cse323.nsu.patienttracking.patient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import cse323.nsu.patienttracking.R;
import cse323.nsu.patienttracking.authentication.AuthenticationActivity;
import cse323.nsu.patienttracking.models.Patient;
import cse323.nsu.patienttracking.utils.CustomProgressBar;
import de.hdodenhof.circleimageview.CircleImageView;

public class PatientProfileActivity extends AppCompatActivity {

    private static final String TAG = "PatientProfileActivity";
    ImageButton mSettings;
    MaterialButton mLogout;

    private TextView mName;
    private TextView mAge;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mBlood;
    private TextView mLocation;

    CircleImageView mAvatar;
    CustomProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_patient_profile);

        progressBar = new CustomProgressBar(this);

        mSettings = findViewById(R.id.ib_settings);
        mLogout = findViewById(R.id.mb_logout);

        mAvatar = findViewById(R.id.iv_profile);
        mName = findViewById(R.id.tv_name);
        mAge = findViewById(R.id.tv_age);
        mPhone = findViewById(R.id.tv_phone);
        mEmail = findViewById(R.id.tv_email);
        mBlood = findViewById(R.id.tv_blood);
        mLocation = findViewById(R.id.tv_location);

        progressBar.show("");
        readDataFromFirebase();

        mSettings.setOnClickListener(view -> {
            Intent intent = new Intent(PatientProfileActivity.this, PatientProfileSettingsActivity.class);
            startActivity(intent);
        });

        mLogout.setOnClickListener(view -> {

            progressBar.show("Logging out...");

            new Handler(Looper.myLooper()).postDelayed(() -> {
                FirebaseAuth.getInstance().signOut();

                progressBar.hide();

                pavilion();

            }, 800);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void readDataFromFirebase() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getUid());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("patients");

        reference.child(uid).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Patient patient = task.getResult().getValue(Patient.class);
                updatePlaceholders(patient);

            } else {
                Log.w(TAG, "readDataFromFirebase:failure", task.getException());
            }
            progressBar.hide();

        });
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

    public void setSharedPreferences(Patient patient) {
        SharedPreferences.Editor editor =  getSharedPreferences("profile", Context.MODE_PRIVATE).edit();

        editor.putString("name", patient.getName());
        editor.putString("blood", patient.getBlood());
        editor.putString("email", patient.getEmail());
        editor.putString("phone", patient.getPhone());
        editor.putString("sex", patient.getSex());
        editor.putString("location", patient.getLocation());
        editor.putInt("age", patient.getAge());

        editor.apply();
    }

    public Patient getSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("profile", Context.MODE_PRIVATE);

        String name = preferences.getString("name", null);
        String blood = preferences.getString("blood", null);
        String email = preferences.getString("email", null);
        String phone = preferences.getString("phone", null);
        String sex = preferences.getString("sex", null);
        String location = preferences.getString("location", null);
        int age = preferences.getInt("age", 0);

        return new Patient(age, blood, email, location, name, phone, sex);
    }

    private void pavilion() {
        Intent intent = new Intent(PatientProfileActivity.this, AuthenticationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}