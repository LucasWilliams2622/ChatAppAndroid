package com.example.chatapplication.Verify_OTP;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapplication.R;

import com.example.chatapplication.Utilities.Constants;
import com.example.chatapplication.Utilities.PreferenceManager;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTPActivity extends AppCompatActivity {

SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otpactivity);
        final EditText edtInputNumber = findViewById(R.id.edtInputNumber);
        final ProgressBar progressBarOTP = findViewById(R.id.progressBarOTP);
        Button btnGetOTP = findViewById(R.id.btnGetOTP);


        sharedPreferences= getSharedPreferences("Number",0);
        String number = sharedPreferences.getString(Constants.KEY_PHONE_NUMBER,"");
        Log.d("---------->>",""+number);
          edtInputNumber.setText(number);

        btnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtInputNumber.getText().toString().trim().isEmpty()) {

                    Toast.makeText(SendOTPActivity.this, "You must enter your phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBarOTP.setVisibility(View.VISIBLE);
                btnGetOTP.setVisibility(View.INVISIBLE);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+84" + edtInputNumber.getText().toString().trim()
                        , 60
                        , TimeUnit.SECONDS
                        , SendOTPActivity.this

                        , new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressBarOTP.setVisibility(View.GONE);
                                btnGetOTP.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBarOTP.setVisibility(View.GONE);
                                btnGetOTP.setVisibility(View.VISIBLE);
                                Toast.makeText(SendOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBarOTP.setVisibility(View.GONE);
                                btnGetOTP.setVisibility(View.VISIBLE);
                                Intent intentSendOTP = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                                intentSendOTP.putExtra("mobilePhone", edtInputNumber.getText().toString().trim());
                                intentSendOTP.putExtra("verificationId", verificationId);
                                startActivity(intentSendOTP);

                            }
                        }
                );

            }
        });
    }
}