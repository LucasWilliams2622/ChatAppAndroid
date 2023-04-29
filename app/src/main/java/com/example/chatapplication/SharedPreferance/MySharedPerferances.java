package com.example.chatapplication.SharedPreferance;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPerferances {
    private static final String MY_SHARED_PREFERANCES = "MY_SHARED_PREFERANCES";
    private Context mContext;

    public MySharedPerferances(Context mContext) {
        this.mContext = mContext;
    }

    public void putBooleanValue(String key, boolean value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERANCES,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBooleanValue(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERANCES
                , Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }
}
