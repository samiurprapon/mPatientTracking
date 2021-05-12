package cse323.nsu.patienttracking.patient;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import cse323.nsu.patienttracking.models.AvailableDoctor;
import cse323.nsu.patienttracking.utils.CustomProgressBar;
import cse323.nsu.patienttracking.utils.adapters.AvailableDoctorsAdapter;

public class AvailableDoctorsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    AvailableDoctorsAdapter adapter;
    private List<AvailableDoctor> doctorList;

    CustomProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_available_doctors);

        mRecyclerView = findViewById(R.id.rv_doctors);
        swipeRefreshLayout = findViewById(R.id.refresh);

        adapter = new AvailableDoctorsAdapter(this);
        progressBar = new CustomProgressBar(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(true);

        progressBar.show("");
        getAvailableDoctorList();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            progressBar.show("");

            // implement Handler to wait for 3 seconds and then update UI means update value of TextView
            new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);

                getAvailableDoctorList();
            }, 50);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void getAvailableDoctorList() {
        doctorList = new ArrayList<>();

        // get doctor list from firebase database
        readDataFromFirebase();

    }

    private void readDataFromFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("doctors");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    AvailableDoctor availableDoctor = dataSnapshot.getValue(AvailableDoctor.class);
                    if (availableDoctor != null) {
                        availableDoctor.setUid(dataSnapshot.getKey());
                    }

                    doctorList.add(availableDoctor);
                }

                progressBar.hide();
                adapter.setAvailableDoctorList(doctorList);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.hide();
            }
        });

    }
}