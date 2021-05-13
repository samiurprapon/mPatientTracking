package cse323.nsu.patienttracking.patient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cse323.nsu.patienttracking.R;
import cse323.nsu.patienttracking.models.AvailableDoctor;
import cse323.nsu.patienttracking.models.DoctorAppointment;
import cse323.nsu.patienttracking.models.Patient;
import cse323.nsu.patienttracking.utils.Constants;
import cse323.nsu.patienttracking.utils.CustomProgressBar;

public class MakesAppointmentFragment extends BottomSheetDialogFragment {

    @SuppressLint("StaticFieldLeak")
    private static MakesAppointmentFragment fragment = null;

    EditText mDate;
    EditText mMessage;
    Spinner mType;

    MaterialButton mRequest;

    AvailableDoctor doctor;
    Patient patient;

    CustomProgressBar progressBar;
    public MakesAppointmentFragment() {
        // required constructor
    }

    public static MakesAppointmentFragment newInstance() {

        if (fragment == null) {
            fragment = new MakesAppointmentFragment();
        }

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity().getIntent() != null) {
            doctor = (AvailableDoctor) getActivity().getIntent().getSerializableExtra("doctor");
        }

        patient = getSharedPreferences();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_make_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDate = view.findViewById(R.id.et_date);
        mMessage = view.findViewById(R.id.et_message);
        mType = view.findViewById(R.id.spinner_appoint);
        mRequest = view.findViewById(R.id.mb_request);

        mType.setAdapter(new ArrayAdapter<>(getContext(), R.layout.simple_spinner_dropdown_item, Constants.APPOINTMENT_TYPE));

        progressBar = new CustomProgressBar(getContext());

        mRequest.setOnClickListener(v -> {
            String date = mDate.getText().toString().trim();
            String message = mMessage.getText().toString().trim();
            String type = (String) mType.getSelectedItem();

            if(date.isEmpty() || message.isEmpty() || type.isEmpty()) {
                return;
            }
            progressBar.show("asking...");


            new Handler(Looper.myLooper()).postDelayed(() -> {
                // save data on firebase
                sycWithFirebase(type, date, message);
            }, 800);
        });
    }


    public Patient getSharedPreferences() {
        SharedPreferences preferences = getContext().getSharedPreferences("profile", Context.MODE_PRIVATE);

        String name = preferences.getString("name", null);
        String blood = preferences.getString("blood", null);
        String email = preferences.getString("email", null);
        String phone = preferences.getString("phone", null);
        String sex = preferences.getString("sex", null);
        String location = preferences.getString("location", null);
        int age = preferences.getInt("age", 0);

        return new Patient(age, blood, email, location, name, phone, sex);
    }

    private void sycWithFirebase(String type, String date, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("appointments");
        String key = reference.push().getKey();

        DoctorAppointment appointment = new DoctorAppointment(doctor.getName(), doctor.getSex(), doctor.getUid(), "pending",
                type, date, message, patient.getName(), patient.getSex(), patient.getLocation(), FirebaseAuth.getInstance().getUid());


        reference.child(key).setValue(appointment).addOnCompleteListener(task -> {

            progressBar.hide();

            if(task.isSuccessful()) {
                dismiss();
                Toast.makeText(getContext(), "Appointment requested!", Toast.LENGTH_LONG).show();
            } else {
                dismiss();
                Toast.makeText(getContext(), "request failed!", Toast.LENGTH_LONG).show();
            }

        });
    }

}
