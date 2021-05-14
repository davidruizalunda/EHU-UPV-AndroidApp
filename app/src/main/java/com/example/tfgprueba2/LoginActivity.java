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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.RadioGroup;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    private EditText ldap, password;
    private CheckBox recordarCheckBox;
    private RadioGroup radioGroup;
    private Locale locale = null;
    private boolean logeado = false;
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

        try{
            Integer idioma = getIntent().getExtras().getInt("idioma");
            cargarRadioGroup(idioma);
        }catch(NullPointerException e){
            cargarRadioGroup(sharedPreferences.getInt("idioma", 0));
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId)
            {
                case R.id.es_radioButton:
                    locale = new Locale("es", "ES");
                    break;
                case R.id.eu_radioButton:
                    locale = new Locale("eu", "EU");
                    break;
                case R.id.en_radioButton:
                    locale = new Locale("en", "EN");
                    break;
                default:
                    locale = Locale.getDefault();
            }
            cambiarIdioma(locale);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void cargarRadioGroup(int id){
        switch (id){
            case 1:
                Log.d("HEY: ", "listen");
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

    public void cambiarIdioma(Locale locale){
        if (locale != null){
            Log.d("HOLA:", "hola");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        Intent refrescar = new Intent(getApplicationContext(), LoginActivity.class);

        switch (radioGroup.getCheckedRadioButtonId())
        {
            case R.id.es_radioButton:
                refrescar.putExtra("idioma", 1);
                break;
            case R.id.eu_radioButton:
                refrescar.putExtra("idioma", 2);
                break;
            case R.id.en_radioButton:
                refrescar.putExtra("idioma", 3);
                break;
            default:
                refrescar.putExtra("idioma", 0);
        }
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

            switch (radioGroup.getCheckedRadioButtonId()){
                case R.id.es_radioButton:
                    editor.putInt("idioma", 1);
                    break;
                case R.id.eu_radioButton:
                    editor.putInt("idioma", 2);
                    break;
                case R.id.en_radioButton:
                    editor.putInt("idioma", 3);
                    break;
                default:
                    editor.putInt("idioma", 0);
            }

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