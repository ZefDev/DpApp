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
import com.example.vadim.dpapp.activity.OtchetActivity;
import com.example.vadim.dpapp.activity.TaskActivity;
import com.example.vadim.dpapp.adapters.DocAdapter;
import com.example.vadim.dpapp.adapters.ReportAdapter;
import com.example.vadim.dpapp.application.AppConfig;
import com.example.vadim.dpapp.application.DBHelper;
import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.activity.ElementActivity;
import com.example.vadim.dpapp.adapters.ActivAdapter;
import com.example.vadim.dpapp.application.RESTController;
import com.example.vadim.dpapp.containers.ActivContainer;
import com.example.vadim.dpapp.containers.DocContainer;
import com.example.vadim.dpapp.containers.ReportContainer;

import java.util.ArrayList;

public class  ActivFragment extends Fragment {
    public static String TAG = ActivFragment.class.getSimpleName();
    ArrayList<ActivContainer> arrayList;
    RESTController restController;
    DBHelper dbHelper;
    Intent intent;
    Spinner spinner_conditionActiv;
    View view;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_second, container, false);
        final Context context = view.getContext();
        listView = (ListView)view.findViewById(R.id.listView);
        MainActivity activity = (MainActivity) getActivity();
        spinner_conditionActiv = (Spinner) view.findViewById(R.id.spinner_conditionActiv);
        dbHelper = new DBHelper(activity);
        //dbHelper.removeAll("Activ");
        intent = new Intent(getContext(), ElementActivity.class);
        arrayList = new ArrayList<>();
        restController = new RESTController(activity, TAG);
        //arrayList = dbHelper.getAllActiv();
        //if(activity.isConnected) {
        //restController.getActiv(listView,null);
        if(AppConfig.rights.equals("Контрагент") | AppConfig.rights.equals ("Контрагент_пользователь")) {
            restController = new RESTController(activity, OtchetActivity.class.getSimpleName());
            //arrayList = dbHelper.getAllActiv();
            //if(activity.isConnected) {
            restController.getOtchet(listView,spinner_conditionActiv);
        }
        else {
            restController.getActiv(listView,spinner_conditionActiv,null);
            /*arrayList = dbHelper.getAllActiv();
            listView.setAdapter(new ActivAdapter(getActivity(), arrayList));
            listView.setDividerHeight(0);*/
        }
        //listView.setAdapter(new ActivAdapter(getActivity(),arrayList));
        //listView.setDividerHeight(0);
        spinner_conditionActiv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String string = null;
                try {
                    string = spinner_conditionActiv.getSelectedItem().toString();
                }
                catch (Exception e){

                }
                if(AppConfig.rights.equals("Контрагент") | AppConfig.rights.equals ("Контрагент_пользователь")) {
                    ArrayList<ReportContainer> list = dbHelper.getAllReport(null);
                    ArrayList<ReportContainer> list2 = new ArrayList<ReportContainer>();
                    for (ReportContainer item : list) {
                        if (item.getStatus() != null && item.getStatus().equals(string)) {
                            list2.add(item);
                        }
                    }
                    if (!list2.isEmpty()) {
                        listView.setAdapter(new ReportAdapter(context, list2));
                    }
                }
                else {
                    ArrayList<ActivContainer> list = dbHelper.getAllActiv();
                    ArrayList<ActivContainer> list2 = new ArrayList<ActivContainer>();
                    for (ActivContainer item : list) {
                        if (item.getConditionActiv() != null && item.getConditionActiv().equals(string)) {
                            list2.add(item);
                        }
                    }
                    if (!list2.isEmpty()) {
                        listView.setAdapter(new ActivAdapter(context, list2));
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView codeActiv = (TextView) view.findViewById(R.id.textCodeActiv);
                TextView nameActiv = (TextView) view.findViewById(R.id.textNameActiv);
                TextView shtrihCode = (TextView) view.findViewById(R.id.textShtrihCodeActiv);
                if(AppConfig.rights.equals("Контрагент")| AppConfig.rights.equals ("Контрагент_пользователь")){
                    intent = new Intent(getActivity(), OtchetActivity.class);
                }
                else{
                    intent = new Intent(getActivity(), ElementActivity.class);
                }
                intent.putExtra("flag",false);
                intent.putExtra("code",codeActiv.getText());
                intent.putExtra("nameActiv",nameActiv.getText());
                intent.putExtra("shtrihCode",shtrihCode.getText());
                for(ActivContainer a: arrayList){
                    if(a.getCode().equals(codeActiv.getText())) {
                        intent.putExtra("photo", a.getPhoto());
                        intent.putExtra("contragent",a.getContractor());
                    }
                }
                startActivity(intent);
            }
        });
        return view;
    }
}