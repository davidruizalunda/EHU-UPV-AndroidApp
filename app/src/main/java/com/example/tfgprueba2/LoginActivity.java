package com.example.tfgprueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;

import android.widget.TextView;
import android.widget.Toast;

import java.util.Properties;

import android.os.Bundle;
import com.example.tfgprueba2.DataAccess;

public class LoginActivity extends AppCompatActivity {
    private EditText ldap, password;
    private TextView wait;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ldap = (EditText)findViewById(R.id.ldap_number);
        password = (EditText)findViewById(R.id.ldap_password);
        wait = (TextView)findViewById(R.id.wait_txtvw);
        button = (Button)findViewById(R.id.button);
    }

    public void login(View view) {

        DataAccess dataAccess = new DataAccess();
        if(dataAccess.login(ldap.getText().toString(), password.getText().toString())){
            Intent home = new Intent(this, HomeActivity.class);
            startActivity(home);
        }else{
            wait.setText("Entrar");
            Toast.makeText(this, "Comprueba tu usuario y/o contraseña y vuelve a intentarlo.", Toast.LENGTH_LONG).show();
        }
    }



}