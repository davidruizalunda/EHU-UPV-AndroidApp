package com.example.tfgprueba2;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MyTasksViewListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final List<Tarea> tareas;
    private final Map<String, Asignatura> asignaturasMap;

    public MyTasksViewListAdapter(Activity context, List<Tarea> tareas, Map<String, Asignatura> asignaturasMap) {
        super(context, R.layout.mytaskslistview, new String[tareas.size()]);
        this.context = context;
        this.tareas = tareas;
        this.asignaturasMap = asignaturasMap;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mytaskslistview, null,true);

        TextView abreviaturaTareaText = (TextView) rowView.findViewById(R.id.abreviaturaTarea_textView);
        TextView textoTareaText = (TextView)rowView.findViewById(R.id.tarea_textview);

        Log.d("TAREA ID: ", tareas.get(position).getAsig_ID() + "");
        abreviaturaTareaText.setText(asignaturasMap.get(String.valueOf(tareas.get(position).getAsig_ID())).getAbreviatura());
        textoTareaText.setText(tareas.get(position).getTextoTarea());

        return rowView;
    };
}
