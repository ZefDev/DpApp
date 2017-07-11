package com.example.vadim.dpapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.containers.DocContainer;

import java.util.ArrayList;

public class DocAdapter  extends BaseAdapter {

    Context context;
    LayoutInflater lInflater;
    ArrayList<DocContainer> docs;

    public DocAdapter(Context context, ArrayList<DocContainer> tasks) {
        this.context = context;
        this.docs = tasks;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return docs.size();
    }

    @Override
    public DocContainer getItem(int position) {
        return docs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_doc, parent, false);
        }

        DocContainer t = getItem(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.codeDoc)).setText(String.valueOf(t.getCodeDoc()));
        ((TextView) view.findViewById(R.id.nameDoc)).setText(t.getAvtorDoc());
        ((TextView) view.findViewById(R.id.textDoc)).setText(t.getLastDate().substring(0,19));

        return view;
    }
}