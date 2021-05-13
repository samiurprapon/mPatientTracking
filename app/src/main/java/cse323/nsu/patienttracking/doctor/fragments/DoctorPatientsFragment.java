package cse323.nsu.patienttracking.doctor.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cse323.nsu.patienttracking.R;
import cse323.nsu.patienttracking.models.DoctorAppointment;
import cse323.nsu.patienttracking.models.Patient;
import cse323.nsu.patienttracking.utils.CustomProgressBar;
import cse323.nsu.patienttracking.utils.adapters.PatientsAdapter;

public class DoctorPatientsFragment extends Fragment {

    private static DoctorPatientsFragment fragment = null;

    ConstraintLayout mNoPatients;
    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView mRecyclerView;

    CustomProgressBar progressBar;
    PatientsAdapter adapter;

    private List<Patient> patientList;

    public DoctorPatientsFragment() {
        // Required empty public constructor
    }

    public static DoctorPatientsFragment newInstance() {
        if (fragment == null) {
            fragment = new DoctorPatientsFragment();
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
        return inflater.inflate(R.layout.fragment_doctor_patients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.rv_patients);
        swipeRefreshLayout= view.findViewById(R.id.refresh);
        mNoPatients = view.findViewById(R.id.cl_no_patients);

        progressBar = new CustomProgressBar(getContext());
        adapter = new PatientsAdapter(getContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(true);

        progressBar.show("");
        getPatientList();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            progressBar.show("");

            // implement Handler to wait for 3 seconds and then update UI means update value of TextView
            new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);

                getPatientList();
            }, 50);
        });
    }

    public void getPatientList() {
        patientList = new ArrayList<>();

        readDataFromFirebase();
    }

    private void readDataFromFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("appointments");
        String uid = FirebaseAuth.getInstance().getUid();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    DoctorAppointment appointment = dataSnapshot.getValue(DoctorAppointment.class);
                    if (appointment != null) {
                        appointment.setId(dataSnapshot.getKey());

                        Log.d("DoctorPatients:size", String.valueOf(patientList.size()));
                        Log.d("DoctorPatients:out", appointment.getPatientUid());

                        // call patients with patientUID
                        if(appointment.getDoctorUid().equals(uid) && !appointment.getStatus().equals("cancelled")) {
                            getPatientDetails(appointment.getPatientUid());
                            Log.d("DoctorPatients:size", String.valueOf(patientList.size()));
                            Log.d("DoctorPatients:int", appointment.getPatientUid());

                        }
                    }

                }

                progressBar.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.hide();
            }
        });

    }

    private void getPatientDetails(String uid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("patients");

        reference.child(uid).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Patient patient = task.getResult().getValue(Patient.class);
                Log.d("DoctorPatients:int", patient.toString());

                if(patient != null) {
                    patientList.add(patient);
                    Log.d("DoctorPatients:size", String.valueOf(patientList.size()));

                    if(patientList.size() != 0) {
                        mNoPatients.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        mRecyclerView.setVisibility(View.GONE);
                        mNoPatients.setVisibility(View.VISIBLE);
                    }

                    adapter.notifyDataSetChanged();
                }


                adapter.setPatientList(patientList);
                mRecyclerView.setAdapter(adapter);

            }
        });
        Log.d("DoctorPatients:size", String.valueOf(patientList.size()));

    }

}