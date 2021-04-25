package com.example.tfgprueba2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PreferencesActivity extends AppCompatActivity {
    private Spinner spinnerAsignaturas;
    private List<Asignatura> listaAsignaturas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        spinnerAsignaturas = findViewById(R.id.spinner7);

    }

    @Override
    protected void onStart() {
        super.onStart();
        new seleccionarDb(this).execute();
    }

    public void onAddAsignaturaButton(View view) {
        BusinessLogic businessLogic = new BusinessLogic();
        businessLogic.insertIntoUsuario(PreferencesActivity.this , String.valueOf(listaAsignaturas.get(spinnerAsignaturas.getSelectedItemPosition()).getAsig_ID()));
    }

    public void cargarListViewAsignaturas(Context context){

        MySubjectsListAdapter subjectsAdapter=new MySubjectsListAdapter(this, listaAsignaturas);
        ListView subjectListView = findViewById(R.id.asignaturasListView);
        subjectListView.setAdapter(subjectsAdapter);


    }

    private void cargarSpinnerAsignaturas(Context context) {
        ArrayAdapter<Asignatura> asignaturasAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, listaAsignaturas);
        asignaturasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        asignaturasAdapter.notifyDataSetChanged();
        spinnerAsignaturas.setAdapter(asignaturasAdapter);
    }

    class seleccionarDb extends AsyncTask<String,String,String> {
        private Context context;

        public seleccionarDb(Context context) {
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            BusinessLogic businessLogic = new BusinessLogic();
            if(businessLogic.obtenerAsignaturas()){
                runOnUiThread(() -> {
                    listaAsignaturas = businessLogic.getListaAsignaturas();
                    cargarSpinnerAsignaturas(context);
                    cargarListViewAsignaturas(context);
                });
            }
            return null;
        }

    }
}