package com.example.vadim.dpapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.containers.OTaskContainer;

import java.util.ArrayList;

/**
 * Created by Vadim on 21.03.2017.
 */
public class OTaskAdapter extends BaseAdapter {

    Context context;
    LayoutInflater lInflater;
    ArrayList<OTaskContainer> otasks;

    public OTaskAdapter(Context context, ArrayList<OTaskContainer> tasks) {
        this.context = context;
        this.otasks = tasks;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return otasks.size();
    }

    @Override
    public OTaskContainer getItem(int position) {
        return otasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_otask, parent, false);
        }

        OTaskContainer t = getItem(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.codeOTask)).setText(String.valueOf(position+1));
        ((TextView) view.findViewById(R.id.nameOTask)).setText(t.getOpisanie());
       // ((TextView) view.findViewById(R.id.codeTask)).setText(t.getCodeTask());

        return view;
    }
}
