package com.example.vadim.dpapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.containers.TaskContainer;

import java.util.ArrayList;

/**
 * Created by Vadim on 21.03.2017.
 */
public class TaskAdapter extends BaseAdapter {

    Context context;
    LayoutInflater lInflater;
    ArrayList<TaskContainer> tasks;

    public TaskAdapter(Context context, ArrayList<TaskContainer> tasks) {
        this.context = context;
        this.tasks = tasks;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public TaskContainer getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_task, parent, false);
        }

        TaskContainer t = getItem(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.textCodeTask)).setText(t.getCode());
        ((TextView) view.findViewById(R.id.textNameTask)).setText(t.getTaskName());
        ((TextView) view.findViewById(R.id.textDateTask)).setText(t.getDate());
        ((TextView) view.findViewById(R.id.Complite)).setText(t.getComplite());
        ((TextView) view.findViewById(R.id.textKontragentTask)).setText(t.getContractor());

        return view;
    }
}
