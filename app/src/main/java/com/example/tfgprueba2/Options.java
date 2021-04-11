package com.example.tfgprueba2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Options extends AppCompatActivity {

    private Dialog popupDocente;
    Spinner spinerNombres;
    RequestQueue requestQueue;
    ArrayAdapter<String> docentesAdapter;
    ArrayList<String> listaDocentes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        popupDocente = new Dialog(this);
        requestQueue = Volley.newRequestQueue(this);
        spinerNombres = findViewById(R.id.spinner);


        BusinessLogic businessLogic = new BusinessLogic();
        docentesAdapter = new ArrayAdapter<>(Options.this, android.R.layout.simple_spinner_item, businessLogic.cargarSpinner(requestQueue));
        docentesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerNombres.setAdapter(docentesAdapter);
    }


    public void onButtonClick1(View view){
            popupDocente.setContentView(R.layout.docente_popup);
            popupDocente.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            EditText correo_EHU = popupDocente.findViewById(R.id.correo_EditText);
            EditText nombre = popupDocente.findViewById(R.id.nombre_EditText);
            EditText apellidos = popupDocente.findViewById(R.id.apellidos_EditText);
            EditText despacho = popupDocente.findViewById(R.id.despacho_EditText);
            Button addDocente_button = popupDocente.findViewById(R.id.addDocente_button);

            popupDocente.show();

            addDocente_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String correoS = correo_EHU.getText().toString();
                    Log.d("Correo bueno: ", correoS);
                    String nombreS = nombre.getText().toString();
                    String apellidosS = apellidos.getText().toString();
                    String despachoS = despacho.getText().toString();

                    Log.d("Correo bueno2: ", correoS);
                    new DataAccess.insertarDb(Options.this, 0).execute(correoS,nombreS,apellidosS,despachoS);
                }
            });
    }


}

