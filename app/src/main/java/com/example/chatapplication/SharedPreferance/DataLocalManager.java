package com.example.chatapplication.SharedPreferance;

import android.content.Context;

public class DataLocalManager {
    private static final String PREF_FIRST_INSTALL = "PREF_FIRST_INSTALL";
    private static DataLocalManager instance;
    private MySharedPerferances mySharedPerferances;

    public static void init(Context context) {
        instance = new DataLocalManager();
        instance.mySharedPerferances = new MySharedPerferances(context);

    }

    public static DataLocalManager getInstance() {
        if (instance == null) {
            instance = new DataLocalManager();
        }
        return instance;
    }

    public static void setFisrtInstalled(boolean isFisrt) {
        DataLocalManager.getInstance().mySharedPerferances.putBooleanValue(PREF_FIRST_INSTALL, isFisrt);
    }

    public static boolean getFirstInstall() {
        return DataLocalManager.getInstance().mySharedPerferances.getBooleanValue(PREF_FIRST_INSTALL);
    }
}
