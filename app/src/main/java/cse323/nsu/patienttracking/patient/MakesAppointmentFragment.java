package cse323.nsu.patienttracking.patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import cse323.nsu.patienttracking.R;

public class MakesAppointmentFragment extends BottomSheetDialogFragment {

    private static MakesAppointmentFragment fragment = null;

    public MakesAppointmentFragment() {
        // required constructor
    }

    public static MakesAppointmentFragment newInstance() {

        if(fragment == null) {
            fragment = new MakesAppointmentFragment();
        }

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_make_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
