package com.example.vadim.dpapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vadim.dpapp.activity.MainActivity;
import com.example.vadim.dpapp.activity.OtchetActivity;
import com.example.vadim.dpapp.activity.TaskActivity;
import com.example.vadim.dpapp.adapters.DocAdapter;
import com.example.vadim.dpapp.application.AppConfig;
import com.example.vadim.dpapp.application.DBHelper;
import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.activity.ElementActivity;
import com.example.vadim.dpapp.adapters.ActivAdapter;
import com.example.vadim.dpapp.application.RESTController;
import com.example.vadim.dpapp.containers.ActivContainer;
import com.example.vadim.dpapp.containers.DocContainer;

import java.util.ArrayList;

public class  ActivFragment extends Fragment {

    ArrayList<ActivContainer> arrayList;
    RESTController restController;
    DBHelper dbHelper;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        ListView listView = (ListView)view.findViewById(R.id.listView);
        MainActivity activity = (MainActivity) getActivity();
        dbHelper = new DBHelper(activity);
        dbHelper.removeAll("Activ");
        intent = new Intent(getContext(), ElementActivity.class);
        arrayList = new ArrayList<>();
        restController = new RESTController(activity, ElementActivity.class.getSimpleName());
        //arrayList = dbHelper.getAllActiv();
        //if(activity.isConnected) {
        restController.getActiv(listView);
        if(AppConfig.rights.equals("Контрагент")) {
            restController = new RESTController(activity, OtchetActivity.class.getSimpleName());
            //arrayList = dbHelper.getAllActiv();
            //if(activity.isConnected) {
            restController.getOtchet(listView);
        }
        //else{

        //}
        //}
        else {
            arrayList = dbHelper.getAllActiv();
            listView.setAdapter(new ActivAdapter(getActivity(), arrayList));
            listView.setDividerHeight(0);
        }
        //listView.setAdapter(new ActivAdapter(getActivity(),arrayList));
        //listView.setDividerHeight(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView codeActiv = (TextView) view.findViewById(R.id.textCodeActiv);
                TextView nameActiv = (TextView) view.findViewById(R.id.textNameActiv);
                TextView shtrihCode = (TextView) view.findViewById(R.id.textShtrihCodeActiv);
                if(AppConfig.rights.equals("Контрагент")){
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