package cse323.nsu.patienttracking.patient;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

import cse323.nsu.patienttracking.R;
import cse323.nsu.patienttracking.models.AvailableDoctor;
import cse323.nsu.patienttracking.utils.CustomProgressBar;

public class DoctorProfileActivity extends AppCompatActivity {

    private ImageView mAvatar;
    private MaterialTextView mName;
    private MaterialTextView mDegree;
    private MaterialTextView mExpertise;
    private MaterialTextView mPhone;
    private MaterialTextView mEmail;
    private MaterialTextView mWorkplace;
    private MaterialTextView mAbout;

    ImageButton mAppointment;

    CustomProgressBar progressBar;

    AvailableDoctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_doctor_profile);

        if(getIntent() != null) {
            doctor = (AvailableDoctor) getIntent().getSerializableExtra("doctor");
        } else {
            doctor = new AvailableDoctor();
        }

        mAvatar = findViewById(R.id.iv_avatar);
        mName = findViewById(R.id.tv_name);
        mDegree = findViewById(R.id.tv_degree);
        mExpertise = findViewById(R.id.tv_date_time);
        mPhone = findViewById(R.id.tv_phone);
        mEmail = findViewById(R.id.doctor_email);
        mWorkplace = findViewById(R.id.doctor_address);
        mAbout = findViewById(R.id.tv_doctor_about);

        mAppointment = findViewById(R.id.ib_appointment);

        updatePlaceHolders(doctor);

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
            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                mEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            }
        }

        if (doctor.getWorkplace() != null) {
            mWorkplace.setText(doctor.getWorkplace());

        }

        if (doctor.getAbout() != null) {
            mAbout.setText(doctor.getAbout());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}