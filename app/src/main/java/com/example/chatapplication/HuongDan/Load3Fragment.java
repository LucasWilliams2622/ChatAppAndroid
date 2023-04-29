package com.example.chatapplication.HuongDan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.chatapplication.Activity.SignInActivity;
import com.example.chatapplication.R;

public class Load3Fragment extends Fragment {
    private Button btnStart;
    private View mView;
private TextView tvNextLogin0,tvNextLogin;

    public Load3Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_load3, container, false);

        tvNextLogin0 = mView.findViewById(R.id.tvNextLogin0);
        tvNextLogin0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SignInActivity.class);
                getActivity().startActivity(i);
            }
        });



        tvNextLogin = mView.findViewById(R.id.tvNextLogin);
        tvNextLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SignInActivity.class);
                getActivity().startActivity(i);
            }
        });



        return mView;
    }
}