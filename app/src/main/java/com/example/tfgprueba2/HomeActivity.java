package com.example.tfgprueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import android.os.Bundle;
/*
Tipo de servidor: IMAP o POP  (Mejor utiliza IMAP)
Nombre de servidor: ikasle.ehu.eus
Puerto: imap 993, pop 995
Protocolo SSL
Usuario: Tu login LDAP
Contraseña: Tu contraseña
*/
public class HomeActivity extends AppCompatActivity {
    private TextView textView5;
    private Dialog popupCorreow;
    private final int TIEMPO = 5000;
    private boolean terminado;
    Handler h = new Handler();
    int cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        textView5 = findViewById(R.id.textView5);
        popupCorreow = new Dialog(this);
    }
    @Override
    public void onStart() {
        super.onStart();
        terminado = false;
        updateAutomatically();
    }

    public void updateAutomatically(){
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if(!terminado){
                    Log.d("update ", "//////////////////////////////");
                    updateDataCorreowsNews();
                    h.postDelayed(this, TIEMPO);
                }
            }

        }, 0);
    }

    public void updateDataCorreowsNews() {
        Thread thread = new Thread(() -> {
            RSSReader lectorRSS = new RSSReader(getApplicationContext());
            lectorRSS.execute();

            try {
                BusinessLogic businessLogic = new BusinessLogic();
                Correow[] correows = businessLogic.getCorreows(10);
                ArrayList<News> news = businessLogic.getEHUNews(getApplicationContext());
                h.post(() -> {
                    updateAdaptersCorreowsNews(correows, news);
                });

            }catch (Exception e){
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void updateAdaptersCorreowsNews(Correow[] correows, ArrayList<News> news){
            MyMailsListAdapter mailsAdapter=new MyMailsListAdapter(this, correows);
            ListView mailListView = findViewById(R.id.mailListView);
            mailListView.setAdapter(mailsAdapter);

            mailListView.setOnItemClickListener((parent, view, position, id) -> {
                popupCorreow.setContentView(R.layout.popup_view_correows);
                popupCorreow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView from = popupCorreow.findViewById(R.id.from_popup);
                TextView subject = popupCorreow.findViewById(R.id.subject_popup);
                TextView date = popupCorreow.findViewById(R.id.date_popup);
                TextView content = popupCorreow.findViewById(R.id.content_popup);

                from.setText(correows[position].getFrom());
                subject.setText(correows[position].getSubject());
                date.setText(correows[position].getDate());
                content.setText(correows[position].getContents());

                popupCorreow.show();
            });

            MyNewsListAdapter newsAdapter=new MyNewsListAdapter(this, news);
            ListView newsListView = findViewById(R.id.newsListView);
            newsListView.setAdapter(newsAdapter);

            newsListView.setOnItemClickListener((parent, view, position, id) -> {
                Uri uri = Uri.parse(news.get(position).getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            });
    }

    public void onEgelaButtonClick(View view){
        Uri uri = Uri.parse("https://egela.ehu.eus/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onGaurButtonClick(View view){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.app.gaur");
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(intent);
    }

    public void onOptions(View view){
        terminado = true;
        Intent home = new Intent(this, Options.class);
        startActivity(home);
    }

    public void onPreferences(View view){
        terminado = true;
        Intent home = new Intent(this, PreferencesActivity.class);
        startActivity(home);
    }


}