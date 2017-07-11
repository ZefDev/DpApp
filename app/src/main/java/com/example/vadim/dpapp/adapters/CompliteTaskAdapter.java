package com.example.vadim.dpapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.containers.CompliteTaskContainer;
import com.example.vadim.dpapp.containers.OTaskContainer;

import java.util.ArrayList;

/**
 * Created by Vadim on 28.05.2017.
 */
public class CompliteTaskAdapter extends BaseAdapter {

    Context context;
    LayoutInflater lInflater;
    ArrayList<CompliteTaskContainer> compliteTask;

    public CompliteTaskAdapter(Context context, ArrayList<CompliteTaskContainer> Task) {
        this.context = context;
        this.compliteTask = Task;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return compliteTask.size();
    }

    @Override
    public Object getItem(int position) {
        return compliteTask.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_complite_task, parent, false);
        }

        CompliteTaskContainer t = (CompliteTaskContainer) getItem(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.textViewDate)).setText(t.getDate());
        ((TextView) view.findViewById(R.id.textViewTime)).setText(t.getTime());
        ((TextView) view.findViewById(R.id.textViewCompliteTask)).setText(t.getCompliteTask());

        return view;
    }
}
