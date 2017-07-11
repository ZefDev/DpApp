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

import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.activity.DocActivity;
import com.example.vadim.dpapp.activity.MainActivity;
import com.example.vadim.dpapp.activity.TaskActivity;
import com.example.vadim.dpapp.adapters.DocAdapter;
import com.example.vadim.dpapp.adapters.TaskAdapter;
import com.example.vadim.dpapp.application.DBHelper;
import com.example.vadim.dpapp.application.RESTController;
import com.example.vadim.dpapp.containers.DocContainer;

import java.util.ArrayList;

public class DocFragment  extends Fragment {
    ArrayList<DocContainer> arrayList;
    RESTController restController;
    DBHelper dbHelper;
    Intent intent;
    public static String TAG = DocFragment.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doc, container, false);
        ListView listView = (ListView)view.findViewById(R.id.listDocuments);
        MainActivity activity = (MainActivity) getActivity();
        dbHelper = new DBHelper(activity);
        restController = new RESTController(activity,TAG);
        intent = new Intent(getContext(), DocActivity.class);
        arrayList = new ArrayList<DocContainer>();
        arrayList = dbHelper.getAllDocuments();
        //if(activity.isConnected) {
        restController.getDocuments(listView);
        /*}
        else {
            arrayList = dbHelper.getAllDocuments();
            listView.setAdapter(new DocAdapter(getActivity(), arrayList));
            listView.setDividerHeight(0);
        }*/
        listView.setAdapter(new DocAdapter(getActivity(),arrayList));
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textCodeDoc = (TextView) view.findViewById(R.id.codeDoc);
                TextView textNameDoc = (TextView) view.findViewById(R.id.nameDoc);
                TextView textDoc = (TextView) view.findViewById(R.id.textDoc);

                intent.putExtra("codeDoc",textCodeDoc.getText());
                intent.putExtra("nameDoc",textNameDoc.getText());
                intent.putExtra("textDoc",textDoc.getText());
                intent.putExtra("isEdit","true");
                startActivity(intent);
            }
        });
        return view;
    }

}
