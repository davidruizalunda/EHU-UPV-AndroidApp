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
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Options extends AppCompatActivity {

    private Dialog popupDocente, popupAsignatura, popupTutorias, popupClase;
    private static RequestQueue requestQueue;
    Handler h = new Handler();
    private Spinner spinerNombres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        popupDocente = new Dialog(this);
        popupAsignatura = new Dialog(this);
        popupTutorias = new Dialog(this);
        popupClase = new Dialog(this);

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
        spinerNombres = popupAsignatura.findViewById(R.id.spinner);
        Button addAsignatura_button = popupAsignatura.findViewById(R.id.addAsignatura_button);
        new Seleccionar().execute();
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

    public void onTutoriasButtonClick(View view){
        popupTutorias.setContentView(R.layout.tutoria_popup);
        popupTutorias.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText horaInicio = popupTutorias.findViewById(R.id.horaInicio_editTextTime);
        spinerNombres = popupTutorias.findViewById(R.id.spinner);
        Spinner spinnerDias = popupTutorias.findViewById(R.id.spinner2);
        cargarSpinnerDias(spinnerDias);
        new Seleccionar().execute();
        popupTutorias.show();

    }

    private void cargarSpinnerDias(Spinner spinnerDias) {
        String[] dias = {"Lunes","Martes", "Miercoles", "Jueves", "Viernes"};
        ArrayAdapter<String> diasAdapter = new ArrayAdapter<>(popupAsignatura.getContext(), android.R.layout.simple_spinner_item, dias);
        diasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDias.setAdapter(diasAdapter);
    }

    public void onClaseButtonClick(View view){
        popupClase.setContentView(R.layout.clase_popup);
        popupClase.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Spinner spinnerDias = popupClase.findViewById(R.id.spinner3);
        cargarSpinnerDias(spinnerDias);
        popupClase.show();
    }

    class Seleccionar extends AsyncTask<String,String,String> {
        private List<Docente> docentes;
        private int tabla;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            BusinessLogic businessLogic = new BusinessLogic();

            if(businessLogic.obtenerDocentes()){
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        cargarSpinnerNombres(businessLogic.getListaDocentes());
                    }
                });
            }
            return null;
        }
    }

    private void cargarSpinnerNombres(List<Docente> listaDocentes) {
        ArrayList<String> correos = new ArrayList<>();
        for(int x=0; x<listaDocentes.size(); x++){
            correos.add(listaDocentes.get(x).getApellidos() + ", " + listaDocentes.get(x).getNombre());

        }
        ArrayAdapter<String> docentesAdapter = new ArrayAdapter<>(popupAsignatura.getContext(), android.R.layout.simple_spinner_item, correos);
        docentesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        docentesAdapter.notifyDataSetChanged();
        spinerNombres.setAdapter(docentesAdapter);
    }


}

