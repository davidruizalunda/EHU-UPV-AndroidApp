package com.example.tfgprueba2;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyNewsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private  ArrayList<News> news;

    public MyNewsListAdapter(Activity context, ArrayList<News> news) {
        super(context, R.layout.mynewslistview, new String[news.size()]);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.news = news;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mynewslistview, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title_txtvw);
        TextView authorText = (TextView)rowView.findViewById(R.id.author_txtvw);
        TextView dateText = (TextView) rowView.findViewById(R.id.date_txtvw);

        titleText.setText(news.get(position).getTitle());
        authorText.setText(news.get(position).getAuthor());
        dateText.setText(news.get(position).getDate());

        return rowView;

    };
}
