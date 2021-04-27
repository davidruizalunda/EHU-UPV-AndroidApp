package com.example.tfgprueba2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.List;

public class PreferencesActivity extends AppCompatActivity {
    private Spinner spinnerAsignaturas;
    private List<Asignatura> listaAsignaturas, listaAsignaturasUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        spinnerAsignaturas = findViewById(R.id.spinner7);

    }

    @Override
    protected void onStart() {
        super.onStart();
        new seleccionarDb(this, 1, false).execute();
        new seleccionarDb(this, 1, true).execute();
    }

    public void onAddAsignaturaButton(View view) {
        LogicForAdmin logicForAdmin = new LogicForAdmin();
        logicForAdmin.insertIntoUsuario(PreferencesActivity.this , String.valueOf(listaAsignaturas.get(spinnerAsignaturas.getSelectedItemPosition()).getAsig_ID()));
        new seleccionarDb(this, 1, false).execute();
        new seleccionarDb(this, 1, true).execute();
    }

    public void cargarListViewAsignaturas(List<Asignatura> listaAsignaturasUsuario){

        //MySubjectsViewListAdapter subjectsAdapter=new MySubjectsViewListAdapter(this, listaAsignaturasUsuario);
       // ListView subjectListView = findViewById(R.id.asignaturasListView);
        //subjectListView.setAdapter(subjectsAdapter);
    }

    private void cargarSpinnerAsignaturas() {
        ArrayAdapter<Asignatura> asignaturasAdapter = new ArrayAdapter<>(this , android.R.layout.simple_spinner_item, listaAsignaturas);
        asignaturasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        asignaturasAdapter.notifyDataSetChanged();
        spinnerAsignaturas.setAdapter(asignaturasAdapter);
    }

    class seleccionarDb extends AsyncTask<String,String,String> {
        private Context context;
        private int table;
        private boolean usuario;

        public seleccionarDb(Context context, int table, boolean usuario) {
            this.context = context;
            this.table = table;
            this.usuario = usuario;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            LogicForAdmin logicForAdmin = new LogicForAdmin();
            if(table == 1 && !usuario){
                if(logicForAdmin.obtenerAsignaturas(false)){
                    runOnUiThread(() -> {
                        listaAsignaturas = logicForAdmin.getListaAsignaturas();
                        cargarSpinnerAsignaturas();

                    });
                }
            }else if(table == 1 && usuario){
                if(logicForAdmin.obtenerAsignaturas(true)){
                    runOnUiThread(() -> {
                        listaAsignaturasUsuario = logicForAdmin.getListaAsignaturas();
                        cargarListViewAsignaturas(listaAsignaturasUsuario);
                    });
                }
            }else if(table == 2 && usuario){
                if(logicForAdmin.obtenerClases(true)){
                    runOnUiThread(() -> {
                        listaAsignaturasUsuario = logicForAdmin.getListaAsignaturas();
                        cargarListViewAsignaturas(listaAsignaturasUsuario);
                    });
                }
            }

            return null;
        }

    }


}