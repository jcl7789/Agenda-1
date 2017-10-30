package com.zeus.agenda.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeus.agenda.R;

import java.util.ArrayList;

/**
 * Created by Juan on 29/10/2017.
 */

public class AgendaAdapter extends BaseAdapter {
    private ArrayList<Contacto> al;
    private Context context;

    public AgendaAdapter(Context context, ArrayList<Contacto> al) {
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
        Contacto entry = al.get(position);

        // inflating list view layout if null
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.linea_agenda, null);
        }

        // set avatar
        ImageView ivAvatar = convertView.findViewById(R.id.imgAvatar);
        ivAvatar.setImageBitmap(entry.getBitmap());

        // set name
        TextView tvName = convertView.findViewById(R.id.tvName);
        tvName.setText(entry.getNombre() + " " + entry.getApellido());

        // set phone
        TextView tvPhone = convertView.findViewById(R.id.tvPhone);
        tvPhone.setText(entry.getTelefono());

        // set email
        TextView tvEmail = convertView.findViewById(R.id.tvEmail);
        tvEmail.setText(entry.getMail());

        return convertView;
    }


}
