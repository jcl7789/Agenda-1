package com.zeus.agenda;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Juan on 29/10/2017.
 */

public class AgendaAdapter extends BaseAdapter {
    private ArrayList<Agenda> al;
    private Context context;

    public AgendaAdapter(ArrayList<Agenda> al, Context context) {
        this.al = al;
        this.context = context;
    }

    public AgendaAdapter(Context context, ArrayList<Agenda> al) {
        this.al = al;
        this.context = context;
    }

    @Override
    public int getCount() {
        return al.size();
    }

    @Override
    public Object getItem(int position) {
        return al.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get selected entry
        Agenda entry = al.get(position);

        // inflating list view layout if null
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.linea_agenda, null);
        }

        // set avatar
        ImageView ivAvatar = (ImageView)convertView.findViewById(R.id.imgAvatar);
        ivAvatar.setImageBitmap(entry.getBitmap());

        // set name
        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
        tvName.setText(entry.getNombre());

        // set phone
        TextView tvPhone = (TextView)convertView.findViewById(R.id.tvPhone);
        tvPhone.setText(entry.getTelefono());

        // set email
        TextView tvEmail = (TextView)convertView.findViewById(R.id.tvEmail);
        tvEmail.setText(entry.getMail());

        return convertView;
    }


}
