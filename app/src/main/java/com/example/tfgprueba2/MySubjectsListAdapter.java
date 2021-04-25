package com.example.tfgprueba2;

import android.app.Activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MySubjectsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<Asignatura> asignaturas;

    public MySubjectsListAdapter(Activity context, List<Asignatura> asignaturas) {
        super(context, R.layout.mysubjectslistview, new String[asignaturas.size()]);
        this.context = context;
        this.asignaturas = asignaturas;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mysubjectslistview, null,true);

        TextView horaInicioText = (TextView) rowView.findViewById(R.id.horaInicioAsignaturaPreferencias_textView);
        TextView horaFinText = (TextView)rowView.findViewById(R.id.horaFinAsignaturaPreferencias_textView);
        TextView abreviaturaText = (TextView)rowView.findViewById(R.id.abreviaturaAsignaturaPreferencias_textView);
        TextView aulaText = (TextView) rowView.findViewById(R.id.aulaAsignaturaPreferencias_textView);

        abreviaturaText.setText(asignaturas.get(position).getAbreviatura());
        return rowView;
    };
}