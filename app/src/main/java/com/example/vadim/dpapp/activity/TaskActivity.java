package com.example.vadim.dpapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.adapters.CompliteTaskAdapter;
import com.example.vadim.dpapp.adapters.OTaskAdapter;
import com.example.vadim.dpapp.application.DBHelper;
import com.example.vadim.dpapp.application.RESTController;
import com.example.vadim.dpapp.containers.CompliteTaskContainer;
import com.example.vadim.dpapp.containers.OTaskContainer;
import com.example.vadim.dpapp.containers.TaskContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


public class TaskActivity extends AppCompatActivity {

    TextView editCode,editName,editContractor,editDate,editTypeTask,textView24;
    Spinner spinnerDirectoryUser;
    CheckBox checkComplite,checkIsApproved;
    ListView listViewOTask,listTask;
    DBHelper dbHelper;
    Button buttonSave,fabTask,addActiv,newOTask;
    String code,name,contractor,Date,typeTask,isApproved,waypoint,nextExecuter;
    TaskContainer taskContainer;
    RESTController rest;
    OTaskAdapter a;
    CompliteTaskAdapter b;
    ArrayList<OTaskContainer> arrayList;
    ArrayList<CompliteTaskContainer> listCompliteTask;
    LinearLayout layoutIsApproved;
    Context context;
    Intent scannerIntent;
    EditText shtrihCode,nameActiv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_tasks);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DBHelper(this);
        arrayList = new ArrayList<>();
        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Поручение");
        tabSpec.setContent(R.id.linearLayout2);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Описание");
        tabSpec.setContent(R.id.linearLayout3);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("Вып. работы");
        tabSpec.setContent(R.id.linearLayout4);
        tabHost.addTab(tabSpec);

        editCode = (TextView) findViewById(R.id.editCodeTask);
        editName = (TextView) findViewById(R.id.editNameTask);
        editContractor = (TextView) findViewById(R.id.editContractorTask);
        editDate = (TextView) findViewById(R.id.editDateTask);
        editTypeTask = (TextView) findViewById(R.id.editTypeTask);
        newOTask = (Button) findViewById(R.id.addCompliteTask);
        addActiv = (Button) findViewById(R.id.addActiv);
        spinnerDirectoryUser = (Spinner) findViewById(R.id.spinnerDirectoryUser);
        checkComplite = (CheckBox) findViewById(R.id.CompliteTask);
        checkIsApproved =  (CheckBox) findViewById(R.id.checkBox2);
        layoutIsApproved = (LinearLayout) findViewById(R.id.layoutIsApproved);
        textView24 = (TextView) findViewById(R.id.textView24);
        InitializationVariable();

        buttonSave = (Button) findViewById(R.id.bSaveTask);
        fabTask = (Button) findViewById(R.id.fabTask);
        listViewOTask = (ListView) findViewById(R.id.listViewOTask);
        listTask = (ListView) findViewById(R.id.listViewCompliteTask);

        editCode.setText(code);
        editName.setText(name);
        editContractor.setText(contractor);
        editDate.setText(Date);
        editTypeTask.setText(typeTask);

        //scannerIntent = new Intent(TaskActivity.this, ElementActivity.class);

        arrayList = dbHelper.getAllOTask(code);
        listCompliteTask = dbHelper.getAllCompliteTask(code);
        a = new OTaskAdapter(this, arrayList);
        b = new CompliteTaskAdapter(this,listCompliteTask);
        listViewOTask.setAdapter(a);
        listTask.setAdapter(b);
        newOTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog(true,0);
            }
        });
        if(getIntent().getStringExtra("codeTask")!=null) {
            taskContainer = dbHelper.getAllTasks(getIntent().getStringExtra("codeTask")).get(0);
            listViewOTask.setAdapter(new OTaskAdapter(this, taskContainer.getOtasks()));
            listTask.setAdapter(new CompliteTaskAdapter(this,taskContainer.getCompliteTask()));
        }
        registerForContextMenu(listTask);
        //listViewOTask
        addActiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogAddDescriptionTask(true,0);
            }
        });

        rest = new RESTController(this,TaskActivity.class.getSimpleName());
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveChanges();
                String isComplite = "";
                if(String.valueOf(checkComplite.isChecked())=="true") {
                    isComplite = "Да";
                }
                else{
                    isComplite = "Нет";
                }
                if(String.valueOf(checkIsApproved.isChecked())=="true") {
                    isApproved = "Да";
                }
                else{
                    isApproved = "Нет";
                }
                if(layoutIsApproved.getVisibility()==View.VISIBLE) {
                    nextExecuter = "";
                }
                else
                {
                    nextExecuter = spinnerDirectoryUser.getSelectedItem().toString();
                }
                rest.sendTask(code, name, contractor, Date, isComplite,listCompliteTask,typeTask, isApproved,waypoint,nextExecuter);
                dbHelper.remove(code,"Code","Task");
                finish();
            }
        });
        rest.getDirectoryUser(spinnerDirectoryUser);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:
                ShowDialog(false,info.position);
                return true;
            case R.id.delete:
                dbHelper.remove(String.valueOf(info.position),"id","CompliteTask");
                listCompliteTask = dbHelper.getAllCompliteTask(code);
                b = new CompliteTaskAdapter(context, listCompliteTask);
                b.notifyDataSetChanged();
                listTask.setAdapter(b);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void InitializationVariable(){
        code = getIntent().getStringExtra("codeTask");
        if(code==null | code==""){
            ArrayList<TaskContainer> list = dbHelper.getAllTasks(null);
            if(list.size()!=0) {
                code = list.get(list.size() - 1).getCode() + 1;
            }
            else{
                code = String.valueOf(1);
            }
        }
        name = getIntent().getStringExtra("nameTask");
        contractor = getIntent().getStringExtra("contractorTask");
        Date = getIntent().getStringExtra("dateTask");
        ArrayList<TaskContainer> tasks =  dbHelper.getAllTasks(code);
        typeTask = tasks.get(0).getTypeTask();
        isApproved = tasks.get(0).getIsApproved();
        waypoint = tasks.get(0).getWaypoint();
        if (waypoint.equals("Проверка автором") && waypoint !=null){
            textView24.setText("Одобрено");
            spinnerDirectoryUser.setVisibility(View.GONE);
            layoutIsApproved.setVisibility(View.VISIBLE);
        }
        else {
            textView24.setText("Следующий исполнитель");
            spinnerDirectoryUser.setVisibility(View.VISIBLE);
            layoutIsApproved.setVisibility(View.GONE);
        }
    }
    public void SaveChanges(){
        code = editCode.getText().toString();
        name = editName.getText().toString();
        contractor = editContractor.getText().toString();
        Date = editDate.getText().toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        String date = data.getStringExtra("date");
        String complteOTask = data.getStringExtra("complteOTask");
        String time = data.getStringExtra("time");
        shtrihCode.setText(data.getStringExtra("shtrihCode"));
        nameActiv.setText(data.getStringExtra("nameActiv"));
        dbHelper.addCompliteTask(new CompliteTaskContainer(1,date,complteOTask,time,code,""),listCompliteTask.size()+1);
        listCompliteTask = dbHelper.getAllCompliteTask(code);
        b = new CompliteTaskAdapter(this,listCompliteTask);
        b.notifyDataSetChanged();
        listTask.setAdapter(b);
    }

    public void ShowDialog(final boolean isCreate, final int position){
        final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(this);
        ratingdialog.setIcon(android.R.drawable.btn_star_big_on);
        ratingdialog.setTitle("Выполненная работа");

        View linearlayout = getLayoutInflater().inflate(R.layout.dialog_add_otask, null);
        ratingdialog.setView(linearlayout);
        EditText editTextDate = (EditText) linearlayout.findViewById(R.id.editTextDate);
        final EditText complteOTask = (EditText) linearlayout.findViewById(R.id.editTextOTask);
        final EditText time = (EditText) linearlayout.findViewById(R.id.editTextTime);
        Button addCompliteTask = (Button) linearlayout.findViewById(R.id.addCompliteTask);
        if(!isCreate) {
            complteOTask.setText(listCompliteTask.get(position).getCompliteTask());
            time.setText(listCompliteTask.get(position).getTime());
        }
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        String month ="";
        String day ="";
        if(mMonth<9){
            month = "0"+mMonth;
        }
        else {
            month = String.valueOf(mMonth);
        }
        if(mDay<9){
            day = "0"+mDay;
        }
        else {
            day = String.valueOf(mDay);
        }
        final String date = ""+day+"."+month+"."+ mYear;
        editTextDate.setText(date);
        final Dialog dialog =  ratingdialog.create();
        dialog.show();
        addCompliteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                if(complteOTask.getText().toString().equals("")){
                    complteOTask.setHint("Заполните поле!");
                    complteOTask.setHintTextColor(Color.RED);
                    flag = false;
                }
                if(time.getText().toString().equals("")){
                    time.setHint("Введите потраченное время!");
                    time.setHintTextColor(Color.RED);
                    flag = false;
                }
                if(flag==true) {
                    if(isCreate)
                        dbHelper.addCompliteTask(new CompliteTaskContainer(position, date, complteOTask.getText().toString(), time.getText().toString(), code, ""),listCompliteTask.size()+1);
                    else
                        dbHelper.updateCompliteTaskS(position,(new CompliteTaskContainer(position, date, complteOTask.getText().toString(), time.getText().toString(), code, "")));
                    listCompliteTask = dbHelper.getAllCompliteTask(code);
                    b = new CompliteTaskAdapter(context, listCompliteTask);
                    b.notifyDataSetChanged();
                    listTask.setAdapter(b);
                    dialog.dismiss();
                }
            }
        });
    }

    public void ShowDialogAddDescriptionTask(final boolean isCreate, final int position){
        final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(this);
        ratingdialog.setIcon(android.R.drawable.btn_star_big_on);
        ratingdialog.setTitle("Описание работы");

        View linearlayout = getLayoutInflater().inflate(R.layout.dialog_description_task, null);
        ratingdialog.setView(linearlayout);
        nameActiv = (EditText) linearlayout.findViewById(R.id.nameActiv);
        shtrihCode = (EditText) linearlayout.findViewById(R.id.shtrihCode);
        final EditText descriptionWork = (EditText) linearlayout.findViewById(R.id.descriptionWork);
        Button openScan = (Button) linearlayout.findViewById(R.id.openScan);
        //Button addCompliteTask = (Button) linearlayout.findViewById(R.id.addCompliteTask);
        /*if(!isCreate) {
            complteOTask.setText(listCompliteTask.get(position).getCompliteTask());
            time.setText(listCompliteTask.get(position).getTime());
        }*/
        final Dialog dialog =  ratingdialog.create();
        dialog.show();
        openScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(windowAddActiv);
                scannerIntent = new Intent(TaskActivity.this, Pizdec.class);
                startActivityForResult(scannerIntent, 2);
            }
        });
        /*addCompliteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                if(nameActiv.getText().toString().equals("")){
                    nameActiv.setHint("Заполните поле!");
                    nameActiv.setHintTextColor(Color.RED);
                    flag = false;
                }
                if(shtrihCode.getText().toString().equals("")){
                    shtrihCode.setHint("Введите наименование!");
                    shtrihCode.setHintTextColor(Color.RED);
                    flag = false;
                }
                if(shtrihCode.getText().toString().equals("")){
                    shtrihCode.setHint("Считайте штрих-код!");
                    shtrihCode.setHintTextColor(Color.RED);
                    flag = false;
                }
                /*if(flag==true) {
                    if(isCreate)
                        dbHelper.addCompliteTask(new CompliteTaskContainer(position, date, complteOTask.getText().toString(), time.getText().toString(), code, ""),listCompliteTask.size()+1);
                    else
                        dbHelper.updateCompliteTaskS(position,(new CompliteTaskContainer(position, date, complteOTask.getText().toString(), time.getText().toString(), code, "")));
                    listCompliteTask = dbHelper.getAllCompliteTask(code);
                    b = new CompliteTaskAdapter(context, listCompliteTask);
                    b.notifyDataSetChanged();
                    listTask.setAdapter(b);
                    dialog.dismiss();
                }
            }
        });*/
    }
}
