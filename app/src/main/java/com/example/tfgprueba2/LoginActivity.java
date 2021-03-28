package com.example.tfgprueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;


import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Properties;

import android.os.Bundle;
import com.example.tfgprueba2.DataAccess;

public class LoginActivity extends AppCompatActivity {
    private EditText ldap, password;
    private TextView wait,textView4;
    private Button button;
    private RelativeLayout layout;
    private ProgressBar progressBar;
    Handler h = new Handler();
    private boolean logeado = false;
    private ImageView loading;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ldap = (EditText)findViewById(R.id.ldap_number);
        password = (EditText)findViewById(R.id.ldap_password);
        wait = (TextView)findViewById(R.id.wait_txtvw);
        button = (Button)findViewById(R.id.button);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        textView4 = (TextView)findViewById(R.id.textView4);
        loading = (ImageView)findViewById(R.id.loadingGIF);



    }


    public void login(View view) {
        loading.setBackgroundResource(R.drawable.loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) loading.getBackground();

        Intent home = new Intent(this, HomeActivity.class);
        startActivity(home);

        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }
        });

        Thread hilo2 = new Thread(new Runnable() {
            @Override
            public void run() {
                DataAccess dataAccess = new DataAccess();
                if(dataAccess.login(ldap.getText().toString(), password.getText().toString())){
                    logeado = true;
                }
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        if(logeado){
                            textView4.setText("Bien");
                            loading.setBackground(null);
                            /*
                            Intent home = new Intent(this, HomeActivity.class);
                            startActivity(home);

                             */
                        }else {
                            textView4.setText("Mal");
                            loading.setBackground(null);
                        }

                    }
                });


            }
        });
        Log.d("LOGEADO: ", logeado + "");
        hilo.start();
        hilo2.start();




    }



}