package com.example.vadim.dpapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.application.DBHelper;
import com.example.vadim.dpapp.application.RESTController;

import java.util.ArrayList;

/**
 * Created by Vadim on 05.04.2017.
 */
public class Setting extends Activity {
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        dbHelper.create_db();
        dbHelper.open();
        setContentView(R.layout.setting);
        final EditText editIP = (EditText) findViewById(R.id.editIP);
        final EditText editPort = (EditText) findViewById(R.id.editPort);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        Button saveSetting = (Button) findViewById(R.id.saveSetting);
        String ipServer = dbHelper.getIP();
        ipServer.indexOf(":");
        String ip = ipServer.substring(0,ipServer.indexOf(":"));
        String port = ipServer.substring(ipServer.indexOf(":")+1,ipServer.indexOf(":")+5);
        editIP.setText(ip);
        editPort.setText(port);
        final RESTController restController = new RESTController(this, Sign_in.class.getSimpleName());
        saveSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.saveIP(editIP.getText().toString(),editPort.getText().toString());
                Toast.makeText(getApplicationContext(),"Изменения успешно сохранены", Toast.LENGTH_SHORT).show();
                dbHelper.close();
                restController.getUser();
                finish();
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()) {
                    editIP.setText("192.168.99.198");
                }
                else {
                    editIP.setText("86.57.165.79");
                }
            }
        });
    }
}
