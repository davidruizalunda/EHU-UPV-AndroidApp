package com.example.tfgprueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Options extends AppCompatActivity {

    private Dialog popupDocente, popupAsignatura;
    private static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        popupDocente = new Dialog(this);
        popupAsignatura = new Dialog(this);
        requestQueue = Volley.newRequestQueue(this);
    }


    public void onDocenteButtonClick(View view){

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


    public void onAsignaturaButtonClick(View view){

        popupAsignatura.setContentView(R.layout.asignatura_popup);
        popupAsignatura.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText abreviatura = popupAsignatura.findViewById(R.id.abreviatura_EditText);
        EditText nombre = popupAsignatura.findViewById(R.id.nombreAsignatura_EditText);
        Spinner spinerNombres = popupAsignatura.findViewById(R.id.spinner);
        Button addAsignatura_button = popupAsignatura.findViewById(R.id.addAsignatura_button);

        BusinessLogic businessLogic = new BusinessLogic();
        businessLogic.cargarSpinner(requestQueue, spinerNombres, popupAsignatura.getContext());

        popupAsignatura.show();

        addAsignatura_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String abreviaturaS = abreviatura.getText().toString();
                Log.d("Correo bueno: ", abreviaturaS);
                String nombreS = nombre.getText().toString();
                String profesorS = spinerNombres.getSelectedItem().toString();

                new DataAccess.insertarDb(Options.this, 1).execute(abreviaturaS,nombreS,profesorS);
            }
        });

    }


}

