package cse323.nsu.patienttracking.doctor.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import cse323.nsu.patienttracking.utils.CustomProgressBar;
import cse323.nsu.patienttracking.utils.adapters.PatientAppointmentRequestAdapter;

public class DoctorHomeFragment extends Fragment {

    private static DoctorHomeFragment fragment = null;

    ConstraintLayout mNoAppointments;
    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView mRecyclerView;

    CustomProgressBar progressBar;
    PatientAppointmentRequestAdapter adapter;
    private List<DoctorAppointment> appointmentList;

    public DoctorHomeFragment() {
        // Required empty public constructor
    }

    public static DoctorHomeFragment newInstance() {
        if (fragment == null) {
            fragment = new DoctorHomeFragment();
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
        return inflater.inflate(R.layout.fragment_doctor_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mNoAppointments = view.findViewById(R.id.cl_no_appointment);

        swipeRefreshLayout = view.findViewById(R.id.refresh);
        mRecyclerView = view.findViewById(R.id.rv_appointment_request);

        progressBar = new CustomProgressBar(getContext());
        adapter = new PatientAppointmentRequestAdapter(getContext());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(true);

        progressBar.show("");
        getAppointmentList();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            progressBar.show("");

            // implement Handler to wait for 3 seconds and then update UI means update value of TextView
            new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);

                getAppointmentList();
            }, 50);
        });
    }

    public void getAppointmentList() {
        appointmentList = new ArrayList<>();

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

//                        Log.d("appointment:success", appointment.getPatientUid());
//                        Log.d("appointment:success", uid);

                        if(appointment.getDoctorUid() != null && appointment.getDoctorUid().equals(uid) && appointment.getStatus().equals("pending")){
                            appointmentList.add(appointment);
                        }
                    }

                }

                progressBar.hide();

                adapter.setDoctorAppointmentList(appointmentList);
                mRecyclerView.setAdapter(adapter);

                if(appointmentList.size() != 0) {
                    mNoAppointments.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mNoAppointments.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.hide();
            }
        });

    }


}