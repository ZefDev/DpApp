package com.example.vadim.dpapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.application.AppConfig;
import com.example.vadim.dpapp.application.DBHelper;
import com.example.vadim.dpapp.application.RESTController;
import com.example.vadim.dpapp.containers.UserContainer;

import java.util.ArrayList;

public class Sign_in extends Activity {
    public static Intent intent;
     Intent settingIntent;
    public static Intent registrationIntent;
    DBHelper dbHelper;
    public static  ArrayList<UserContainer> listUser = new ArrayList<UserContainer>();
    RESTController restController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        dbHelper = new DBHelper(this);
        dbHelper.create_db();
        dbHelper.open();

        settingIntent = new Intent(this, Setting.class);
        registrationIntent = new Intent(this, registration.class);
        intent = new Intent(this, MainActivity.class);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        final String deviceIMEI = telephonyManager.getDeviceId();
        AppConfig.uid=deviceIMEI.toString();
        restController = new RESTController(this, Sign_in.class.getSimpleName());
        restController.getUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(AppConfig.flagEnter)
            finish();
        else {
            restController.getUser();
        }
    }

}

