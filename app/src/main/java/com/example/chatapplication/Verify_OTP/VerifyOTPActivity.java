package com.example.chatapplication.Verify_OTP;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapplication.Activity.ProfileActivity;
import com.example.chatapplication.R;

import com.example.chatapplication.Utilities.Constants;
import com.example.chatapplication.Utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends AppCompatActivity {
    private EditText edtinputCode, edtinputCode2, edtinputCode3, edtinputCode4, edtinputCode5, edtinputCode6;
    private String verificationID;
    private TextView tvResetOTP;
    private PreferenceManager preferenceManager;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otpactivity);
        TextView tvMobile = findViewById(R.id.tvMobile);
        sharedPreferences = getSharedPreferences("Number", 0);

        tvMobile.setText(String.format(
                "+84-%s",
                getIntent().getStringExtra("mobilePhone"),
                Log.d("------------------>", "" + getIntent().getStringExtra("mobilePhone"))
//                getIntent().getStringExtra(preferenceManager.getString(Constants.KEY_PHONE_NUMBER))


        ));
        tvResetOTP = findViewById(R.id.tvResetOTP);

        edtinputCode = findViewById(R.id.edtinputCode);
        edtinputCode2 = findViewById(R.id.edtinputCode2);
        edtinputCode3 = findViewById(R.id.edtinputCode3);
        edtinputCode4 = findViewById(R.id.edtinputCode4);
        edtinputCode5 = findViewById(R.id.edtinputCode5);
        edtinputCode6 = findViewById(R.id.edtinputCode6);
        setupOTPInputs();

        final ProgressBar progress_barVerifyOTP = findViewById(R.id.progress_barVerifyOTP);
        final Button btnVerifyOTP = findViewById(R.id.btnVerifyOTP);
        verificationID = getIntent().getStringExtra("verificationId");
        btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtinputCode.getText().toString().trim().isEmpty()
                        || edtinputCode2.getText().toString().trim().isEmpty()
                        || edtinputCode3.getText().toString().trim().isEmpty()
                        || edtinputCode4.getText().toString().trim().isEmpty()
                        || edtinputCode5.getText().toString().trim().isEmpty()
                        || edtinputCode6.getText().toString().trim().isEmpty()
                ) {
                    Toast.makeText(VerifyOTPActivity.this, "Plase Enter valid code", Toast.LENGTH_SHORT).show();
                    return;

                }
                String code =
                        edtinputCode.getText().toString().trim() +
                                edtinputCode2.getText().toString().trim() +
                                edtinputCode3.getText().toString().trim() +
                                edtinputCode4.getText().toString().trim() +
                                edtinputCode5.getText().toString().trim() +
                                edtinputCode6.getText().toString().trim();
                if (verificationID != null) {
                    progress_barVerifyOTP.setVisibility(View.VISIBLE);
                    btnVerifyOTP.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationID,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progress_barVerifyOTP.setVisibility(View.GONE);
                                    btnVerifyOTP.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean(Constants.KEY_IS_VERIFY, true);
                                        Log.d(">>>>.", "" + editor.putBoolean(Constants.KEY_IS_VERIFY, true));
                                        //after verify will trans to activity
                                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(VerifyOTPActivity.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


        tvResetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+84" + getIntent().getStringExtra("mobilePhone")
                        , 60
                        , TimeUnit.SECONDS
                        , VerifyOTPActivity.this

                        , new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verificationID = newVerificationId;
                                Toast.makeText(VerifyOTPActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();

                            }
                        }
                );
            }
        });
    }

    private void setupOTPInputs() {
        edtinputCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    edtinputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtinputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    edtinputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtinputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    edtinputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtinputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    edtinputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtinputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    edtinputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}