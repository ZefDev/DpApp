package com.example.vadim.dpapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.application.DBHelper;
import com.example.vadim.dpapp.application.RESTController;
import com.example.vadim.dpapp.containers.ReportContainer;

import java.util.ArrayList;

/**
 * Created by Vadim on 01.06.2017.
 */
public class OtchetActivity extends Activity {
    DBHelper dbHelper;
    RESTController rest;
    ArrayList<ReportContainer> list = new ArrayList<>();
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otchet);
        dbHelper = new DBHelper(this);
        dbHelper.create_db();
        //rest = new RESTController(this,OtchetActivity.class.getSimpleName());
        //rest.getOtchet();
        Intent intent = getIntent();

        String shtrih = intent.getStringExtra("shtrihCode");
        String name = intent.getStringExtra("nameActiv");
        TextView nameActiv = (TextView) findViewById(R.id.editNameActiv);
        TextView shtrihCode = (TextView) findViewById(R.id.editShtrihCode);
        TextView status = (TextView) findViewById(R.id.status);
        TextView mol = (TextView) findViewById(R.id.editMol);
        TextView devision = (TextView) findViewById(R.id.textView21);
        list = dbHelper.getAllReport(shtrih);
        if(list.size()>0) {
            nameActiv.setText(name);
            shtrihCode.setText(list.get(0).getShtrihCod());
            status.setText(list.get(0).getStatus());
            mol.setText(list.get(0).getMol());
            devision.setText(list.get(0).getDivisionOfContractor());
        }

    }
}
