package cse323.nsu.patienttracking.patient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cse323.nsu.patienttracking.R;
import cse323.nsu.patienttracking.models.Patient;
import cse323.nsu.patienttracking.utils.Constants;
import cse323.nsu.patienttracking.utils.CustomProgressBar;

public class PatientProfileSettingsActivity extends AppCompatActivity {

    CustomProgressBar progressBar;
    private ImageView mAvatar;

    EditText mPhone;
    EditText mName;
    EditText mAge;
    TextView mEmail;
    EditText mLocation;
    Spinner mBlood;

    Button mSave;

    private String avatar = "female";
    private boolean flag = false;

    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_patient_profile_settings);

        progressBar = new CustomProgressBar(this);

        mAvatar = findViewById(R.id.iv_profile);
        mName = findViewById(R.id.et_name);
        mEmail = findViewById(R.id.tv_email);
        mPhone = findViewById(R.id.et_phone);
        mBlood = findViewById(R.id.spinner_blood);
        mLocation = findViewById(R.id.et_location);
        mAge = findViewById(R.id.et_age);

        mSave = findViewById(R.id.btn_save);

        mBlood.setAdapter(new ArrayAdapter<>(this, R.layout.simple_spinner_dropdown_item, Constants.BLOOD_GROUPS));

        Patient patient = getSharedPreferences();
        updatePlaceholders(patient);

        mReference = FirebaseDatabase.getInstance().getReference();
        mEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save data at firebase
                updateInformation();

            }
        });

        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAvatar.setEnabled(false);
                toggleAvatar();
                mAvatar.setEnabled(true);
            }
        });
    }

    private void toggleAvatar() {

        if (avatar.equals("female")) {
            mAvatar.animate().rotationBy(360f).setDuration(360).start();
            mAvatar.setImageResource(R.drawable.ic_male);
            avatar = "male";
        } else {
            mAvatar.animate().rotationBy(-360f).setDuration(360).start();
            mAvatar.setImageResource(R.drawable.ic_female);
            avatar = "female";
        }
    }

    private void updateInformation() {

        String name = mName.getText().toString().trim();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String blood = (String) mBlood.getSelectedItem();
        String location = mLocation.getText().toString().trim();
        String phoneNumber = mPhone.getText().toString().trim();
        int age = Integer.parseInt(mAge.getText().toString().trim());

        if (name.equals("") || phoneNumber.isEmpty() || location.isEmpty()) {
            return;
        }

        if (blood.equals(Constants.BLOOD_GROUPS[0])) {
            progressBar.show("missing blood group");

            new Handler(Looper.myLooper()).postDelayed(() -> progressBar.hide(), 500);
        } else {
            progressBar.show("saving...");
            Patient patient = new Patient(age, blood, email, location, name, phoneNumber, avatar);
            syncWithFirebase(patient);

        }
    }

    private void syncWithFirebase(Patient patient) {
        String uid = FirebaseAuth.getInstance().getUid();

        assert uid != null;
        mReference.child("patients").child(uid).setValue(patient).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // change intent to home
                changeRoute();

                Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error Occurs!", Toast.LENGTH_SHORT).show();
            }

            progressBar.hide();
        });
    }

    private void changeRoute() {
        if (flag) {
            onBackPressed();
            finish();
        } else {
            Intent intent = new Intent(PatientProfileSettingsActivity.this, PatientHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    private void updatePlaceholders(Patient patient) {

        if (patient.getSex() != null && patient.getSex().equals("male")) {
            mAvatar.setImageResource(R.drawable.ic_male);
        } else {
            mAvatar.setImageResource(R.drawable.ic_female);
            flag = false;
        }

        if (patient.getName() != null) {
            mName.setText(patient.getName());
            flag = true;
        }

        if (patient.getPhone() != null) {
            mPhone.setText(patient.getPhone());
        }

        if (patient.getEmail() != null) {
            mEmail.setText(patient.getEmail());
        } else {
            mEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }

        if (patient.getBlood() != null) {
            mBlood.setSelection(Constants.getPosition(patient.getBlood()));
        }
        if (patient.getLocation() != null) {
            mLocation.setText(patient.getLocation());
        }
        if (patient.getAge() != 0) {
            mAge.setText(String.valueOf(patient.getAge()));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

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
}