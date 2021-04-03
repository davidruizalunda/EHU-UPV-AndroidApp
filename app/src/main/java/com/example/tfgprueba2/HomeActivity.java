package com.example.tfgprueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.MimeUtility;

import android.widget.TextView;
import android.widget.Toast;

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
    private EditText ldap, password;
    private TextView urlprueba, textView3, textView5;
    private ListView mailListView, newsListView;
    private final int TIEMPO = 5000;
    Handler h = new Handler();
    int i = 0;
    int cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        textView3 = (TextView)findViewById(R.id.textView3);
        textView5 = (TextView)findViewById(R.id.textView5);
        tarea();

    }

    public void tarea(){
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {

                medio();
                handler.postDelayed(this, TIEMPO);
            }

        }, 0);
    }
    public void medio() {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                RSSReader lectorRSS = new RSSReader(getApplicationContext());
                lectorRSS.execute();

                try {
                    BusinessLogic businessLogic = new BusinessLogic();
                    Correow[] correows = businessLogic.getCorreows(10);
                    ArrayList<News> news = businessLogic.getEHUnews(getApplicationContext());
                    h.post(new Runnable() {
                               @Override
                               public void run() {

                                   cont++;
                                   textView5.setText("Count: " + cont);
                                   actualizar(correows, news);
                               }
                           });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        hilo.start();
    }
    public void actualizar(Correow[] correows, ArrayList<News> news){
            MyMailsListAdapter mailsAdapter=new MyMailsListAdapter(this, correows);
            mailListView=(ListView)findViewById(R.id.mailListView);
            mailListView.setAdapter(mailsAdapter);

            /*
            mailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Uri uri = Uri.parse(links.get(position));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            */

            MyNewsListAdapter newsAdapter=new MyNewsListAdapter(this, news);
            newsListView=(ListView)findViewById(R.id.newsListView);
            newsListView.setAdapter(newsAdapter);

            newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Uri uri = Uri.parse(news.get(position).getLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                }
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


}