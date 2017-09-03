package com.example.vadim.dpapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.application.DBHelper;
import com.example.vadim.dpapp.application.RESTController;

/**
 * Created by Vadim on 12.08.2017.
 */
public class Filter extends Activity {
    DBHelper dbHelper;
    RESTController rest;
    String contractor = null;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        dbHelper = new DBHelper(this);
        dbHelper.create_db();

        Spinner spinner_table = (Spinner) findViewById(R.id.spinner_table);
        Spinner spinner_contractor = (Spinner) findViewById(R.id.spinner_contractor);
        Spinner spinner_moll = (Spinner) findViewById(R.id.spinner_moll);
        //rest.getContractors(spinner_contractor,contractor);
    }
}
