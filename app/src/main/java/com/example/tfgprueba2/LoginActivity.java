package com.example.tfgprueba2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

/**
 * Esta es la clase login enlazada con el activity activity_login.
 * Data: 21/05/2021
 * @author David Ruiz Alunda
 * @version 1.0
 */

public class LoginActivity extends AppCompatActivity {
    private EditText ldap, password;
    private CheckBox recordarCheckBox;
    private RadioGroup radioGroup;
    private boolean logeado = false;
    private boolean changed = false;
    private ImageView loadingGif;
    private Handler h = new Handler();

    /** ////////////////////INFO////////////////////
     *  Métodos para logearse y funciones para cambiar el idioma seleccionado (seleccionar el radio button y cambiar el idioma).
     *  Se usarán estos números (id) para distinguir los idiomas.
     *  ids:
     *  0 - Idioma del sistema
     *  1 - Español
     *  2 - Euskera
     *  3 - Inglés
     *
     *  ////////////////////////////////////////////
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ldap = findViewById(R.id.ldap_number);
        password = findViewById(R.id.ldap_password);
        loadingGif = findViewById(R.id.loadingGIF);
        recordarCheckBox = findViewById(R.id.recordar_checkBox);
        radioGroup = findViewById(R.id.radioGroup);

        /**
         *   Aquí se obtienen, si los hay, los datos persistentes en memoria relacionados con las credenciales.
         */
        SharedPreferences sharedPreferences = getSharedPreferences("credencialesEHUusuario", Context.MODE_PRIVATE);
        ldap.setText(sharedPreferences.getString("ldap", ""));
        password.setText(sharedPreferences.getString("password", ""));
        recordarCheckBox.setChecked(sharedPreferences.getBoolean("recordar", false));
        cargarRadioGroup(sharedPreferences.getInt("idioma", 0));
        System.out.println(changed);
        try{
            changed = getIntent().getExtras().getBoolean("changed");
            int idioma = getIntent().getExtras().getInt("idioma");
            cargarRadioGroup(idioma);
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        if (!changed){
            changed = true;
            cambiarLocalidad(sharedPreferences.getInt("idioma", 0));
        }
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> cambiarLocalidad(checkedIdToId(checkedId)));

    }

    /**
     *  Convierte el id del radiobutton en el id con el que trabajamos en login.
     *  @param checkedId Es el id relacionado con el radiobutton del idioma.
     *  @return El número de id de idioma(ver ids).
     */
    @SuppressLint("NonConstantResourceId")
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

    /**
     *  Selecciona un radiobutton.
     *  @param id Es el id relacionado con el idioma (ver ids).
     */
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


    /**
     *  Dado un id de idioma (ver ids) cambia la localidad de la aplicación.
     *  Al cambiar la localidad se cambia el idioma teniendo en cuenta los archivos .xml de strings.
     *  @param id Es el id relacionado con el idioma (ver ids).
     */
    public void cambiarLocalidad(int id){
        Locale locale;
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

    /**
     *  Dado un objeto locale cambia la localidad de la aplicación y por tanto el idioma.
     *  @param locale objeto locale instanciado con un idioma.
     */
    public void cambiarIdioma(Locale locale){
        if (locale != null){
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    /**
     *  Refresca el activity. Pasa el id para que cuando se refresque se mantenga seleccionado el radiobutton
     *  @param id Es el id relacionado con el idioma (ver ids).
     */
    public void refrescar(int id){
        Intent refrescar = new Intent(getApplicationContext(), LoginActivity.class);
        refrescar.putExtra("idioma", id);
        refrescar.putExtra("changed", changed);
        startActivity(refrescar);
        finish();
    }

    /**
     *  Guarda las credenciales persistentemente en memoria. Esto lo hará solo cuando el checbox de recordar esté seleccionado.
     */
    @SuppressLint("ApplySharedPref")
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

    /**
     *  Método principal de la clase login.
     *  Crea 2 hilos (thread):
     *
     *  thread1 se encarga de la animación del círculo de progreso.
     *  thread2 se encarga de verificar las credenciales del usuario.
     *
     */
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