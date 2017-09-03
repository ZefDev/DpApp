package com.example.vadim.dpapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vadim.dpapp.activity.MainActivity;
import com.example.vadim.dpapp.activity.TaskActivity;
import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.application.DBHelper;
import com.example.vadim.dpapp.adapters.TaskAdapter;
import com.example.vadim.dpapp.application.RESTController;
import com.example.vadim.dpapp.containers.TaskContainer;

import java.util.ArrayList;

public class TaskFragment extends Fragment {

    public static String TAG = TaskFragment.class.getSimpleName();
    View view;
    ListView listView;
    Spinner spinner_typeTask;
    ArrayList<TaskContainer> tasks;
    DBHelper dbHelper;
    RESTController restController;
    Intent intent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first, container, false);
        final Context context = view.getContext();
        listView = (ListView)view.findViewById(R.id.list);
        spinner_typeTask  = (Spinner) view.findViewById(R.id.spinner_typeTask);
        intent = new Intent(getContext(), TaskActivity.class);
        MainActivity activity = (MainActivity) getActivity();
        dbHelper = new DBHelper(activity);
        restController = new RESTController(activity,TAG);
        tasks = new ArrayList<>();
        //if(activity.isConnected) {
        restController.getTasks(listView,spinner_typeTask);
        //dbHelper.statusTask();
        /*}
        else {
            tasks = dbHelper.getAllTasks(null);
            listView.setAdapter(new TaskAdapter(getActivity(), tasks));
            listView.setDividerHeight(0);
        }*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textCodeTask = (TextView) view.findViewById(R.id.textCodeTask);
                TextView textNameTask = (TextView) view.findViewById(R.id.textNameTask);
                TextView textKontragentTask = (TextView) view.findViewById(R.id.textKontragentTask);
                TextView textDateTask = (TextView) view.findViewById(R.id.textDateTask);
                TextView compliteTask = (TextView) view.findViewById(R.id.Complite);

                intent.putExtra("compliteTask",compliteTask.getText());
                intent.putExtra("codeTask",textCodeTask.getText());
                intent.putExtra("nameTask",textNameTask.getText());
                intent.putExtra("contractorTask",textKontragentTask.getText());
                intent.putExtra("dateTask",textDateTask.getText());
                startActivity(intent);
            }
        });
        spinner_typeTask.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
               String string = null;
                try {
                    string = spinner_typeTask.getSelectedItem().toString();
                }
                catch (Exception e){

                }
                ArrayList<TaskContainer> list = dbHelper.getAllTasks(null);
                ArrayList<TaskContainer> list2 = new ArrayList<TaskContainer>();
                 for(TaskContainer item: list){
                   if (item.getTypeTask()!= null && item.getTypeTask().equals(string)){
                       list2.add(item);
                   }
            }
                if(!list2.isEmpty()) {
                    listView.setAdapter(new TaskAdapter(context, list2));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return view;
    }

}
