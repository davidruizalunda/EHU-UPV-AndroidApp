package com.example.tfgprueba2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyTutoriasViewListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<Tutoria> tutorias;
    private final String[] dias;

    public MyTutoriasViewListAdapter(Activity context, ArrayList<Tutoria> tutorias) {
        super(context, R.layout.mytutoriaslistview, new String[tutorias.size()]);
        this.context = context;
        this.tutorias = tutorias;
        dias = new String[]{
                context.getResources().getString(R.string.lunes),
                context.getResources().getString(R.string.martes),
                context.getResources().getString(R.string.miercoles),
                context.getResources().getString(R.string.jueves),
                context.getResources().getString(R.string.viernes)
        };
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mytutoriaslistview, null,true);

        TextView tutoriaText = rowView.findViewById(R.id.tutoria_textview);
        tutoriaText.setText(dias[Integer.parseInt(tutorias.get(position).getDia())] + ": " + tutorias.get(position).getHoraInicio() + " - " + tutorias.get(position).getHoraFin());

        return rowView;
    }
}
