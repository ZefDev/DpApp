package com.example.vadim.dpapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vadim.dpapp.containers.ActivContainer;
import com.example.vadim.dpapp.R;

import java.util.ArrayList;

/**
 * Created by Vadim on 26.03.2017.
 */
public class ActivAdapter extends BaseAdapter {

 //   Context context;
    LayoutInflater lInflater;
    Context context;
    ArrayList<ActivContainer> activ;

    public ActivAdapter(Context context, ArrayList<ActivContainer> activ){
        this.context = context;
        this.activ = activ;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return activ.size();
    }

    @Override
    public Object getItem(int position) {
        return activ.get(position);
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

        ActivContainer t = (ActivContainer) getItem(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.textCodeActiv)).setText(t.getCode());
        ((TextView) view.findViewById(R.id.textNameActiv)).setText(t.getName());
        ((TextView) view.findViewById(R.id.textShtrihCodeActiv)).setText(t.getShtrihCode());
        if(t.getPhoto()!=null) {
            ((ImageView) view.findViewById(R.id.imageActiv)).setImageBitmap(convertBase64StringToBitmap(t.getPhoto()));
        }
        else{
            ((ImageView) view.findViewById(R.id.imageActiv)).setImageResource(R.drawable.nophoto);
        }
        return view;
    }
    public static Bitmap convertBase64StringToBitmap(String source) {
            byte[] rawBitmap = Base64.decode(source.getBytes(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(rawBitmap, 0, rawBitmap.length);
            return bitmap;
    }
}
