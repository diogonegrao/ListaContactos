package com.example.listacontactos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.listacontactos.R;
import com.example.listacontactos.entities.Contact;

import java.util.ArrayList;

public class CustomArrayAdapter extends ArrayAdapter<Contact> {

    public CustomArrayAdapter(Context context, ArrayList<Contact> users){
        super(context,0,users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Contact c = getItem(position);
        if(convertView == null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.layout_linha, parent,false);
        }

        ((TextView) convertView.findViewById(R.id.nome)).setText(c.getNome());
        ((TextView) convertView.findViewById(R.id.apelido)).setText(c.getApelido());
        ((TextView) convertView.findViewById(R.id.numero)).setText(String.valueOf(c.getNumero()));

        return convertView;
    }
}
