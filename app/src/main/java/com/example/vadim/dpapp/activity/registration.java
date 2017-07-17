package com.example.vadim.dpapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.application.AppConfig;
import com.example.vadim.dpapp.application.DBHelper;
import com.example.vadim.dpapp.application.RESTController;
import com.example.vadim.dpapp.containers.UserContainer;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class registration extends Activity {

    DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        final EditText regLogin = (EditText) findViewById(R.id.regLogin);
        final EditText regPassword = (EditText) findViewById(R.id.regPassword);
        Button registration = (Button) findViewById(R.id.registration);
        dbHelper = new DBHelper(this);
        dbHelper.create_db();
        dbHelper.open();
        final RESTController restController = new RESTController(this, Setting.class.getSimpleName());

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        final String numberPhone = telephonyManager.getLine1Number();
        final Toast t = Toast.makeText(this,"Запрос отправлен, ожидайте подтверждение регистрации!",Toast.LENGTH_LONG);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                if (regLogin.getText().length() == 0) {
                    regLogin.setBackgroundResource(R.drawable.not_valid);
                    regLogin.setHintTextColor(getResources().getColor(R.color.red));
                    flag = false;
                }
                else{
                    regLogin.setBackgroundResource(R.drawable.edit_text_style);
                }

                if (regPassword.getText().length() == 0) {
                    regPassword.setBackgroundResource(R.drawable.not_valid);
                    regPassword.setHintTextColor(getResources().getColor(R.color.red));
                    flag = false;
                }
                else {
                    regPassword.setBackgroundResource(R.drawable.edit_text_style);
                }
                AppConfig.flagEnter = true;
                restController.sendUser( regLogin.getText().toString(), numberPhone, regPassword.getText().toString(), AppConfig.uid);
                t.show();
                //noAccess(2,getApplicationContext());
            }
        });
    }


}
