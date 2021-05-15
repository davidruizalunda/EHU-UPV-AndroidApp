package com.example.tfgprueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    private EditText ldap, password;
    private CheckBox recordarCheckBox;
    private RadioGroup radioGroup;
    private TextView textView4;
    private Locale locale = null;
    private boolean logeado = false;
    private boolean changed = false;
    private ImageView loadingGif;
    Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ldap = findViewById(R.id.ldap_number);
        password = findViewById(R.id.ldap_password);
        loadingGif = findViewById(R.id.loadingGIF);
        recordarCheckBox = findViewById(R.id.recordar_checkBox);
        radioGroup = findViewById(R.id.radioGroup);

        SharedPreferences sharedPreferences = getSharedPreferences("credencialesEHUusuario", Context.MODE_PRIVATE);
        ldap.setText(sharedPreferences.getString("ldap", ""));
        password.setText(sharedPreferences.getString("password", ""));
        recordarCheckBox.setChecked(sharedPreferences.getBoolean("recordar", false));
        cargarRadioGroup(sharedPreferences.getInt("idioma", 0));
        System.out.println(changed);
        try{
            changed = getIntent().getExtras().getBoolean("changed");
            Integer idioma = getIntent().getExtras().getInt("idioma");
            cargarRadioGroup(idioma);
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        if (!changed){
            changed = true;
            cambiarLocalidad(sharedPreferences.getInt("idioma", 0));
        }
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            cambiarLocalidad(checkedIdToId(checkedId));
        });

    }

    public int checkedIdToId(int checkedId) {
        switch (checkedId)
        {
            case R.id.es_radioButton:
                return 1;
            case R.id.eu_radioButton:
                return 2;
            case R.id.en_radioButton:
                return 3;
            default:
                return 0;
        }
    }

    public void cargarRadioGroup(int id){
        switch (id){
            case 1:
                radioGroup.check(R.id.es_radioButton);
                break;
            case 2:
                radioGroup.check(R.id.eu_radioButton);
                break;
            case 3:
                radioGroup.check(R.id.en_radioButton);
                break;
            default:
                radioGroup.check(R.id.sys_radioButton);
        }
    }

    public void cambiarLocalidad(int id){
        switch (id)
        {
            case 1:
                locale = new Locale("es", "ES");
                break;
            case 2:
                locale = new Locale("eu", "EU");
                break;
            case 3:
                locale = new Locale("en", "EN");
                break;
            default:
                locale = Locale.getDefault();
        }
        cambiarIdioma(locale);
        refrescar(id);
    }

    public void cambiarIdioma(Locale locale){
        if (locale != null){
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    public void refrescar(int id){
        Intent refrescar = new Intent(getApplicationContext(), LoginActivity.class);
        refrescar.putExtra("idioma", id);
        refrescar.putExtra("changed", changed);
        startActivity(refrescar);
        finish();
    }

    public void guardarDatos(){
        SharedPreferences sharedPreferences = getSharedPreferences("credencialesEHUusuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (recordarCheckBox.isChecked()){
            editor.putString("ldap", ldap.getText().toString());
            editor.putString("password", password.getText().toString());
            editor.putBoolean("recordar", true);
            editor.putInt("idioma", checkedIdToId(radioGroup.getCheckedRadioButtonId()));

        }else{
            editor.putString("ldap", "");
            editor.putString("password", "");
            editor.putBoolean("recordar", false);
            editor.putInt("idioma", 0);
        }
        editor.commit();
    }

    public void login(View view) {
        guardarDatos();

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
                    Intent home = new Intent(view.getContext(), HomeActivity.class);
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