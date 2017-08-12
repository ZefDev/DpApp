package com.example.vadim.dpapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.containers.ActivContainer;
import com.example.vadim.dpapp.containers.ReportContainer;

import java.util.ArrayList;

/**
 * Created by Vadim on 31.07.2017.
 */
public class ReportAdapter extends BaseAdapter {

    LayoutInflater lInflater;
    Context context;
    ArrayList<ReportContainer> report;

    public ReportAdapter(Context context, ArrayList<ReportContainer> report){
        this.context = context;
        this.report = report;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return report.size();
    }

    @Override
    public Object getItem(int position) {
        return report.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_activ, parent, false);
        }

        ReportContainer t = (ReportContainer) getItem(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.textCodeActiv)).setText(t.getStatus());
        ((TextView) view.findViewById(R.id.textNameActiv)).setText(t.getNameActiv());
        ((TextView) view.findViewById(R.id.textShtrihCodeActiv)).setText(t.getShtrihCod());
        return view;
    }
}
