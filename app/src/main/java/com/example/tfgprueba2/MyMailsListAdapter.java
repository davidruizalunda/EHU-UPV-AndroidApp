package com.example.tfgprueba2;

import android.app.Activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyMailsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final Correow[] correows;

    public MyMailsListAdapter(Activity context, Correow[] correows) {
        super(context, R.layout.mymaillistview, new String[correows.length]);
        this.context = context;
        this.correows = correows;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mymaillistview, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.from_txtvw);
        TextView dataText = (TextView)rowView.findViewById(R.id.data_txtvw);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.title_txtvw);

        titleText.setText(correows[position].getFrom());
        dataText.setText(correows[position].getDate());
        subtitleText.setText(correows[position].getSubject());

        return rowView;
    };
}
