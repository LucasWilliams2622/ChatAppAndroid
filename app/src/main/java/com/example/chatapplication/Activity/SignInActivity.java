package com.example.chatapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.chatapplication.Utilities.Constants;
import com.example.chatapplication.Utilities.PreferenceManager;
import com.example.chatapplication.databinding.ActivitySignInBinding;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity {
    private PreferenceManager preferenceManager;
    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            //if it already login in ur app next time doesn't to login anymore
            //and when user login if press back wwin out app
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.tvGoSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });
        binding.btnSignIn.setOnClickListener(v ->
        {
            if (isValidSignUpDetails()) {
                signIn();
            }
        });
//        binding.btnSignIn.setOnClickListener(v ->addDataToFireStore());
    }

    private void showToast(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    private void signIn() {
        loading(true);
        FirebaseFirestore databse = FirebaseFirestore.getInstance();
        databse.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.edtEmail.getText().toString().trim())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.edtPassword.getText().toString().trim())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_PHONE_NUMBER, documentSnapshot.getString(Constants.KEY_PHONE_NUMBER));

                        preferenceManager.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL));
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));

                        Intent intent = new Intent(getApplicationContext(), ChatMainScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        loading(false);
                        showToast("Please check your email and password");
                    }
                });


    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.processBarSignUp.setVisibility(View.VISIBLE);
            binding.btnSignIn.setVisibility(View.INVISIBLE);

        } else {
            binding.processBarSignUp.setVisibility(View.INVISIBLE);
            binding.btnSignIn.setVisibility(View.VISIBLE);
        }
    }

    private Boolean isValidSignUpDetails() {


        if (binding.edtEmail.getText().toString().trim().isEmpty()) {
            showToast("Please enter username");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.getText().toString()).matches()) {

            showToast("Please enter valid email");
            return false;
        } else if (binding.edtPassword.getText().toString().trim().isEmpty()) {
            showToast("Please enter password");
            return false;
        } else {
            return true;
        }
    }


//    private  void addDataToFireStore()
//    {
//        FirebaseFirestore database =  FirebaseFirestore.getInstance();
//        HashMap<String,Object> data = new HashMap<>();
//
//        data.put("first_name","Lucas");
//        data.put("last_name","Williams");
//        database.collection("users")
//                .add(data)
//                .addOnSuccessListener(documentReference -> {
//                    Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(exception ->{
//                    Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }
}