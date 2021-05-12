package cse323.nsu.patienttracking.patient;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import cse323.nsu.patienttracking.utils.adapters.DoctorAppointmentAdapter;

public class MakeAppointmentActivity extends AppCompatActivity {

    ConstraintLayout mNoAppointments;
    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView mRecyclerView;

    CustomProgressBar progressBar;
    DoctorAppointmentAdapter adapter;
    private List<DoctorAppointment> appointmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_make_appointment);

        mNoAppointments = findViewById(R.id.cl_no_appointment);

        swipeRefreshLayout = findViewById(R.id.refresh);
        mRecyclerView = findViewById(R.id.rv_made_appointments);

        progressBar = new CustomProgressBar(this);
        adapter = new DoctorAppointmentAdapter(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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

        appointmentList.add(new DoctorAppointment("test1", "male", "Pending", "Consultation", "25/12/2021 at 5 PM"));
        appointmentList.add(new DoctorAppointment("test1", "female", "Accepted", "Consultation", "25/12/2021 at 5 PM"));

//        readDataFromFirebase();

        // debug starts
        progressBar.hide();
        adapter.setDoctorAppointmentList(appointmentList);
        mRecyclerView.setAdapter(adapter);
        // debug ends

        if(appointmentList.size() != 0) {
            mNoAppointments.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mNoAppointments.setVisibility(View.VISIBLE);
        }
    }

    private void readDataFromFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("appointments");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    DoctorAppointment appointment = dataSnapshot.getValue(DoctorAppointment.class);
                    if (appointment != null) {
                        appointment.setId(dataSnapshot.getKey());
                    }

                    appointmentList.add(appointment);
                }

                progressBar.hide();

                adapter.setDoctorAppointmentList(appointmentList);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.hide();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}