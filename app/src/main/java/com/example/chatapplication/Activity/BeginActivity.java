package com.example.chatapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.SharedPreferance.DataLocalManager;


public class BeginActivity extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);
        ImageView ivLogo = findViewById(R.id.logo);
        DataLocalManager.init(getApplicationContext());
        if (!DataLocalManager.getFirstInstall())//false first time
        {

            DataLocalManager.setFisrtInstalled(true);
            Log.d(">>>>>>>>>>>>>>>", "First time");
            Intent i = new Intent(BeginActivity.this, SignInActivity.class);
            startActivity(i);

//            Glide.with(this).load(context.getResources().openRawResource(R.raw.chat_animation)).into(ivLogo);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent i = new Intent(BeginActivity.this, SignInActivity.class);
//                    startActivity(i);
//                }
//            }, 2500);
        } else {
            Log.d(">>>>>>>>>>>>>>>", "Second time");
            Intent i = new Intent(BeginActivity.this, SignInActivity.class);
            startActivity(i);
//            Glide.with(this).load(context.getResources().openRawResource(R.raw.chat_animation)).into(ivLogo);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent i = new Intent(BeginActivity.this, SignInActivity.class);
//                    startActivity(i);
//                }
//            }, 2000);
        }

    }
}