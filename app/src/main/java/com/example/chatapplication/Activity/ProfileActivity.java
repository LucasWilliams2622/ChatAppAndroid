package com.example.chatapplication.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.chatapplication.Model.User;
import com.example.chatapplication.Utilities.Constants;
import com.example.chatapplication.Utilities.PreferenceManager;
import com.example.chatapplication.Verify_OTP.SendOTPActivity;
import com.example.chatapplication.databinding.ActivityProfileBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private PreferenceManager preferenceManager;

    private FirebaseFirestore database;
    private String encodedImage;
    User mUser;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        init();
        setListeners();
        loadUserDetails();
        checkVerify();
    }

    private void checkVerify() {
        Boolean check = sharedPreferences.getBoolean(Constants.KEY_IS_VERIFY, false);
        Log.d(">>>>>>>>>>","check"+check);
        if (check) {
            binding.btnVerifyPhone.setText("Checked");
        } else {
            showToast("Un verify");
        }

    }

    private void init() {
        sharedPreferences = getSharedPreferences("Number", 0);
        database = FirebaseFirestore.getInstance();
        binding.btnVerifyPhone.setText("Verify");

    }

    private void setListeners() {
        binding.btnLogOut.setOnClickListener(v -> signOut());
        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.btnVerifyPhone.setOnClickListener(v -> {
            if (binding.edtPhoneNumberUser.getText().toString().trim().isEmpty()) {
                Log.d("--------------.0", "" + binding.edtPhoneNumberUser.getText().toString().trim().isEmpty());
                showToast("Please enter your phone number");
            } else {
                onVerification();
            }
        });
        binding.btnUpdateAccount.setOnClickListener(v -> {
            if (isValidSignUpDetails()) {
                updateProfile();
            }
        });
        binding.ivEditProfileUser.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->
            {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.ivEditProfileUser.setImageBitmap(bitmap);

                            encodedImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private String encodeImage(Bitmap bitmap) {
        int previewWith = 150;
        int previewHeight = bitmap.getHeight() * previewWith / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWith, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void onVerification() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_PHONE_NUMBER, binding.edtPhoneNumberUser.getText().toString().trim());
        editor.apply();
        Log.d(">>>>>>>>>>>>>>>>>", "num" + binding.edtPhoneNumberUser.getText().toString().trim());


        Intent intent = new Intent(getApplicationContext(), SendOTPActivity.class);
        startActivity(intent);
    }

    private void updateProfile() {
        loading(true);
        if (!binding.btnVerifyPhone.getText().toString().trim().equals("Checked")) {
            binding.btnVerifyPhone.setError("Please verify your number ! ");
            binding.btnVerifyPhone.requestFocus();
        }
        FirebaseFirestore databse = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, binding.edtFullNameUser.getText().toString());
        user.put(Constants.KEY_EMAIL, binding.edtEmailUser.getText().toString());
        user.put(Constants.KEY_PHONE_NUMBER, binding.edtPhoneNumberUser.getText().toString());
        user.put(Constants.KEY_IMAGE, encodedImage);

        databse.collection(Constants.KEY_COLLECTION_USERS).document()
                .update(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
//                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME, binding.edtFullNameUser.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_EMAIL, binding.edtEmailUser.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_PHONE_NUMBER, binding.edtPhoneNumberUser.getText().toString().trim());
                    preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                    showToast("Update Successfully");

                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    showToast(exception.getMessage());
                });

/*
        db.collection("Courses").
                // below line is use toset the id of
                // document where we have to perform
                // update operation.
                        document(courses.getId()).

                // after setting our document id we are
                // passing our whole object class to it.
                        set(updatedCourse).

                // after passing our object class we are
                // calling a method for on success listener.
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // on successful completion of this process
                        // we are displaying the toast message.
                        Toast.makeText(UpdateCourse.this, "Course has been updated..", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            // inside on failure method we are
            // displaying a failure message.
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateCourse.this, "Fail to update the data..", Toast.LENGTH_SHORT).show();
            }
        });

*/

    }

    private void showToast(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    private void loadUserDetails() {
        binding.edtFullNameUser.setText(preferenceManager.getString(Constants.KEY_NAME));
        Log.d("->>>>>>>>>", "" + preferenceManager.getString(Constants.KEY_EMAIL));
        binding.edtEmailUser.setText(preferenceManager.getString(Constants.KEY_EMAIL));
        binding.edtPhoneNumberUser.setText(preferenceManager.getString(Constants.KEY_PHONE_NUMBER));

        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.ivEditProfileUser.setImageBitmap(bitmap);
    }

    private void signOut() {
        showToast("Log out");

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID));
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused ->
                {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable  to sign out"));
    }

    private void loadDataUser() {

    }

    private Boolean isValidSignUpDetails() {

        if (binding.edtFullNameUser.getText().toString().trim().isEmpty()) {
            showToast("Please enter username");
            return false;
        } else if (binding.edtEmailUser.getText().toString().trim().isEmpty()) {
            showToast("Please enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmailUser.getText().toString()).matches()) {

            showToast("Please enter valid email");
            return false;
        } else if (binding.edtPhoneNumberUser.getText().toString().trim().isEmpty()) {
            showToast("Please enter your phone number");
            return false;
        } else {
            return true;
        }
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.processBar.setVisibility(View.VISIBLE);
            binding.btnUpdateAccount.setVisibility(View.INVISIBLE);

        } else {
            binding.processBar.setVisibility(View.INVISIBLE);
            binding.btnUpdateAccount.setVisibility(View.VISIBLE);
        }
    }
}