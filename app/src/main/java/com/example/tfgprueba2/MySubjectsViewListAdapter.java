package com.example.tfgprueba2;

import android.app.Activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MySubjectsViewListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final Map<String, Asignatura> asignaturasMap;
    private final List<Clase> clases;

    public MySubjectsViewListAdapter(Activity context, List<Clase> clases, Map<String, Asignatura> asignaturasMap) {
        super(context, R.layout.mysubjectslistview, new String[clases.size()]);
        this.context = context;
        this.clases = clases;
        this.asignaturasMap = asignaturasMap;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mysubjectslistview, null,true);

        TextView horaInicioText = (TextView) rowView.findViewById(R.id.horaInicioAsignaturaPreferencias_textView);
        TextView horaFinText = (TextView)rowView.findViewById(R.id.horaFinAsignaturaPreferencias_textView);
        TextView abreviaturaText = (TextView)rowView.findViewById(R.id.abreviaturaAsignaturaPreferencias_textView);
        TextView aulaText = (TextView) rowView.findViewById(R.id.aulaAsignaturaPreferencias_textView);

        horaInicioText.setText(clases.get(position).getHoraInicio());
        horaFinText.setText(clases.get(position).getHoraFin());
        aulaText.setText(clases.get(position).getAula());
        abreviaturaText.setText(asignaturasMap.get(String.valueOf(clases.get(position).getAsig_id())).getAbreviatura());

        return rowView;
    };
}