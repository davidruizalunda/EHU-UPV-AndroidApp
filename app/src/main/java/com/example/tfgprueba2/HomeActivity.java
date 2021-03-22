package com.example.tfgprueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
    private TextView urlprueba;
    private ListView mailListView, newsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RSSReader lectorRSS = new RSSReader(getApplicationContext());
        lectorRSS.execute();

        try{
            BusinessLogic businessLogic = new BusinessLogic();

            Correow[] correows = businessLogic.getCorreows(10);
            ArrayList<News> news = businessLogic.getEHUnews(getApplicationContext());

            Log.d("tamaño: ", news.size()+"");
            for(int i=0; i<news.size(); i++){
               System.out.println(news.get(i).getDate());
            }


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




        } catch (NoSuchProviderException e) {
            Toast.makeText(this, "ERROR NoSuchProviderException", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (MessagingException e) {
            Toast.makeText(this, "ERROR MessagingException", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (Exception e) {
            Toast.makeText(this, "ERROR EXCEPTION", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }



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