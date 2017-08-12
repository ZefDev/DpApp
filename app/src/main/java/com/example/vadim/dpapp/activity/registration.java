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
        final EditText surname = (EditText) findViewById(R.id.surname);
        final EditText name = (EditText) findViewById(R.id.name);
        final EditText secondname = (EditText) findViewById(R.id.secondname);
        final EditText regPassword = (EditText) findViewById(R.id.regPassword);
        final EditText numberphone = (EditText) findViewById(R.id.numberphone);
        Button registration = (Button) findViewById(R.id.registration);
        final EditText[] mas = new EditText[5];
        mas[0] = surname;
        mas[1] = name;
        mas[2] = secondname;
        mas[3] = regPassword;
        mas[4] = numberphone;
        dbHelper = new DBHelper(this);
        dbHelper.create_db();
        dbHelper.open();
        final RESTController restController = new RESTController(this, Setting.class.getSimpleName());
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                for(int i=0;i<mas.length;i++){
                    if (mas[i].getText().length() == 0) {
                        mas[i].setBackgroundResource(R.drawable.not_valid);
                        mas[i].setHintTextColor(getResources().getColor(R.color.red));
                        flag = false;
                    }
                    else{
                        mas[i].setBackgroundResource(R.drawable.edit_text_style);
                    }
                }
                String regLogin = mas[0].getText().toString() +
                        " "+ mas[1].getText().toString() +
                        " "+ mas[2].getText().toString();
                String numberPhone =  mas[4].getText().toString();
                AppConfig.flagEnter = true;
                if(flag) {
                    restController.sendUser(regLogin, numberPhone, regPassword.getText().toString(), AppConfig.uid);
                    dbHelper.create_db();
                    //finish();
                }
                //finish();
        }
        });
    }


}
