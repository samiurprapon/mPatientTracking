package cse323.nsu.patienttracking.authentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import cse323.nsu.patienttracking.R;
import cse323.nsu.patienttracking.utils.CustomProgressBar;

import static android.content.ContentValues.TAG;

public class RegisterFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static RegisterFragment fragment = null;

    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;

    RadioGroup mType;
    private Button mSignUp;

    String type;
    SharedPreferences preferences;
    private CustomProgressBar loadingDialog;

    FirebaseAuth mAuth;
    DatabaseReference mReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = Objects.requireNonNull(getContext()).getSharedPreferences("user", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
    }

    public synchronized static RegisterFragment newInstance() {
        if (fragment == null) {
            fragment = new RegisterFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEmail = view.findViewById(R.id.et_email);
        mPassword = view.findViewById(R.id.et_password);
        mConfirmPassword = view.findViewById(R.id.et_confirm_password);

        mType = view.findViewById(R.id.radioGroup);

        mSignUp = view.findViewById(R.id.btn_sign_up);

        loadingDialog = new CustomProgressBar(getContext());

        mSignUp.setOnClickListener(v -> {

            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String confirmPassword = mConfirmPassword.getText().toString().trim();

            if (!validation(password, confirmPassword)) {
                return;
            }

            loadingDialog.show("");

//            mSignUp.setError(null);
            new Handler(Looper.myLooper()).postDelayed(() -> {
                createAccount(email, password) ;

            }, 250);

        });

        mType.setOnCheckedChangeListener((group, checkedId) -> {

            mSignUp.setError(null);

            switch (checkedId) {
                case R.id.rb_doctor:
                    type = "doctor";
                    break;
                case R.id.rb_patient:
                    type = "patient";
                    break;

                default:
                    type = "null";
                    break;
            }
        });

    }

    private void registration(String email, String password, String type) {

        mAuth.setLanguageCode("en");
        String newUserUid = mAuth.getUid();
        mReference = FirebaseDatabase.getInstance().getReference("users").child(newUserUid);


        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("password", password);
        userMap.put("type", type);

        mReference.setValue(userMap).addOnCompleteListener(task -> {
            if (task.isComplete()) {
                route();
            } else {
                Log.w(TAG, "databaseInsert:failure", task.getException());

            }

            loadingDialog.hide();

        });

    }

    private void route() {
        ((AuthenticationActivity) Objects.requireNonNull(getActivity())).selectTab(0);

        Snackbar.make(Objects.requireNonNull(getView()), "Signed up successfully!", Snackbar.LENGTH_SHORT).show();

    }

    private boolean validation(String password, String confirmPassword) {
        if (type.equals("null")) {
            mSignUp.setError("Select type");

            return false;
        }

        if (!password.equals(confirmPassword)) {
            mConfirmPassword.setError("Not matched.");
            return false;
        }

        return password.length() >= 6;
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful()) {
                registration(email, password, type);
            } else {
                loadingDialog.hide();

                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", task.getException());
                Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}