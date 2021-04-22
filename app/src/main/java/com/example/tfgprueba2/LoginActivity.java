package com.example.tfgprueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {
    private EditText ldap, password;
    Handler h = new Handler();
    private boolean logeado = false;
    private ImageView loadingGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ldap = findViewById(R.id.ldap_number);
        password = findViewById(R.id.ldap_password);
        loadingGif = findViewById(R.id.loadingGIF);

    }

    public void login(View view) {

        loadingGif.setBackgroundResource(R.drawable.loading);
        AnimationDrawable animationDrawable = (AnimationDrawable) loadingGif.getBackground();

        Thread thread1 = new Thread(animationDrawable::start);

        Thread thread2 = new Thread(() -> {
            DataAccess dataAccess = new DataAccess();
            if(dataAccess.login(ldap.getText().toString(), password.getText().toString())){
                logeado = true;
            }
            h.post(() -> {
                if(logeado){
                    loadingGif.setBackground(null);
                    Intent home = new Intent(view.getContext(), HomeActivity.class); //En vez de Intent home = new Intent(this, HomeActivity.class);
                    startActivity(home);


                }else {
                    loadingGif.setBackground(null);
                }
            });

        });
        thread1.start();
        thread2.start();

    }


}