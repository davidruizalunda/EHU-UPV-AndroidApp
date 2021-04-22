package com.example.tfgprueba2;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Options extends AppCompatActivity {

    private Dialog popupDocente, popupAsignatura, popupTutorias, popupClase;
    private Spinner spinerNombres, spinnerAsignaturas;
    private List<Docente> listaDocentes;
    private List<Asignatura> listaAsignaturas;
    private List<Clase> listaClases;
    private List<Tutoria> listaTutorias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        popupDocente = new Dialog(this);
        popupAsignatura = new Dialog(this);
        popupTutorias = new Dialog(this);
        popupClase = new Dialog(this);
    }

    public void onAddDocenteButtonClick(View view){

            popupDocente.setContentView(R.layout.popup_add_docente);
            popupDocente.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            EditText correo_EHU = popupDocente.findViewById(R.id.correo_editText);
            EditText nombre = popupDocente.findViewById(R.id.nombre_editText);
            EditText apellidos = popupDocente.findViewById(R.id.apellidos_editText);
            EditText despacho = popupDocente.findViewById(R.id.despacho_editText);
            TextView seleccion = popupDocente.findViewById(R.id.seleccionActualDocente_textView);
            Button addDocente_button = popupDocente.findViewById(R.id.addDocente_button);
            Button emptyButton = popupDocente.findViewById(R.id.emptyDocente_button);
            Button removeButton = popupDocente.findViewById(R.id.removeDocente_button);
            spinerNombres = popupDocente.findViewById(R.id.spinner5);
            new seleccionarDb(0).execute();
            popupDocente.show();

            spinerNombres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    correo_EHU.setText(listaDocentes.get(position).getCorreo_EHU());
                    nombre.setText(listaDocentes.get(position).getNombre());
                    apellidos.setText(listaDocentes.get(position).getApellidos());
                    despacho.setText(listaDocentes.get(position).getDespacho());
                    seleccion.setText(listaDocentes.get(position).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            emptyButton.setOnClickListener(v -> {
                correo_EHU.setText("");
                nombre.setText("");
                apellidos.setText("");
                despacho.setText("");
                seleccion.setText(R.string.nuevoDocente);
            });

            removeButton.setOnClickListener(v -> {
                String correoS = correo_EHU.getText().toString();
                String sentencia = "DELETE FROM docente WHERE correo_ehu='" + correoS + "';";
                new DataAccess.eliminarDb(Options.this, 0).execute(sentencia);
                correo_EHU.setText("");
                nombre.setText("");
                apellidos.setText("");
                despacho.setText("");
                new seleccionarDb(0).execute();
            });

            /*
            spinerNombres.setOnItemClickListener(new AdapterView.OnItemClickListener() { //NO FUNCIONA  Caused by: java.lang.RuntimeException: setOnItemClickListener cannot be used with a spinner.
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    correo_EHU.setText(listaDocentes.get(position).getCorreo_EHU());
                    nombre.setText(listaDocentes.get(position).getNombre());
                    apellidos.setText(listaDocentes.get(position).getApellidos());
                    despacho.setText(listaDocentes.get(position).getDespacho());
                }
            });

             */
            addDocente_button.setOnClickListener(v -> {
                String correoS = correo_EHU.getText().toString();
                Log.d("Correo bueno: ", correoS);
                String nombreS = nombre.getText().toString();
                String apellidosS = apellidos.getText().toString();
                String despachoS = despacho.getText().toString();

                Log.d("Correo bueno2: ", correoS);
                new DataAccess.insertarDb(Options.this, 0).execute(correoS,nombreS,apellidosS,despachoS);
                new seleccionarDb(0).execute();
            });

    }

    public void onAddAsignaturaButtonClick(View view){

        popupAsignatura.setContentView(R.layout.popup_add_asignatura);
        popupAsignatura.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText abreviatura = popupAsignatura.findViewById(R.id.abreviatura_EditText);
        EditText nombre = popupAsignatura.findViewById(R.id.nombreAsignatura_EditText);
        spinerNombres = popupAsignatura.findViewById(R.id.spinner);
        Button addAsignatura_button = popupAsignatura.findViewById(R.id.addAsignatura_button);
        new seleccionarDb(0).execute();
        popupAsignatura.show();

        addAsignatura_button.setOnClickListener(v -> {
            String abreviaturaS = abreviatura.getText().toString();
            String nombreS = nombre.getText().toString();
            String profesorS = listaDocentes.get(spinerNombres.getSelectedItemPosition()).getCorreo_EHU();

            new DataAccess.insertarDb(Options.this, 1).execute(abreviaturaS,nombreS,profesorS);
        });

    }

    public void onAddTutoriasButtonClick(View view){
        popupTutorias.setContentView(R.layout.popup_add_tutoria);
        popupTutorias.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText horaInicioTutoria = popupTutorias.findViewById(R.id.horaInicioTutoria_editTextTime);
        EditText horaFinTutoria = popupTutorias.findViewById(R.id.horaFinTutoria_editTextTime);
        spinerNombres = popupTutorias.findViewById(R.id.spinner);
        Spinner spinnerDias = popupTutorias.findViewById(R.id.spinner2);
        Button addTutorias_button = popupTutorias.findViewById(R.id.addTutoria_button);

        cargarSpinnerDias(spinnerDias);
        new seleccionarDb(0).execute();
        popupTutorias.show();

        addTutorias_button.setOnClickListener(v -> {
            String horaInicioTutoriaS = horaInicioTutoria.getText().toString();
            String horaFinTutoriaS = horaFinTutoria.getText().toString();
            String profesorS = listaDocentes.get(spinerNombres.getSelectedItemPosition()).getCorreo_EHU();
            String diaS = spinnerDias.getSelectedItem().toString();
            new DataAccess.insertarDb(Options.this, 3).execute(horaInicioTutoriaS,horaFinTutoriaS,profesorS,diaS);
        });

    }

    public void onAddClaseButtonClick(View view){
        popupClase.setContentView(R.layout.popup_add_clase);
        popupClase.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText horaInicioClase = popupClase.findViewById(R.id.horaInicioClase_editTextTime);
        EditText horaFinClase = popupClase.findViewById(R.id.horaFinClase_editTextTime);
        EditText aula = popupClase.findViewById(R.id.aula_editTextText);
        Spinner spinnerDias = popupClase.findViewById(R.id.spinner3);
        spinnerAsignaturas = popupClase.findViewById(R.id.spinner4);
        Button addClase_button = popupClase.findViewById(R.id.addClase_button);
        cargarSpinnerDias(spinnerDias);
        new seleccionarDb(1).execute();
        popupClase.show();
        addClase_button.setOnClickListener(v -> {
            String horaInicioClaseS = horaInicioClase.getText().toString();
            String horaFinClaseS = horaFinClase.getText().toString();
            String aulaS = aula.getText().toString();
            String asignaturaS = listaAsignaturas.get(spinnerAsignaturas.getSelectedItemPosition()).getAsig_ID() + "";
            String diaS = spinnerDias.getSelectedItem().toString();
            new DataAccess.insertarDb(Options.this, 2).execute(horaInicioClaseS,horaFinClaseS,aulaS,diaS,asignaturaS);
        });
    }


    private void cargarSpinnerDias(Spinner spinnerDias) {
        String[] dias = {"Lunes","Martes", "Miercoles", "Jueves", "Viernes"};
        ArrayAdapter<String> diasAdapter = new ArrayAdapter<>(popupAsignatura.getContext(), android.R.layout.simple_spinner_item, dias);
        diasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDias.setAdapter(diasAdapter);
    }

    private void cargarSpinnerAsignaturas() {
        ArrayAdapter<Asignatura> asignaturasAdapter = new ArrayAdapter<>(popupClase.getContext(), android.R.layout.simple_spinner_item, listaAsignaturas);
        asignaturasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        asignaturasAdapter.notifyDataSetChanged();
        spinnerAsignaturas.setAdapter(asignaturasAdapter);
    }

    private void cargarSpinnerDocentes() {
        ArrayAdapter<Docente> docentesAdapter = new ArrayAdapter<>(popupAsignatura.getContext(), android.R.layout.simple_spinner_item, listaDocentes);
        docentesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        docentesAdapter.notifyDataSetChanged();
        spinerNombres.setAdapter(docentesAdapter);
    }


    class seleccionarDb extends AsyncTask<String,String,String> {
        private final int tabla;

        public seleccionarDb(int tabla) {
            this.tabla = tabla;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            BusinessLogic businessLogic = new BusinessLogic();
            if (tabla == 0){
                if(businessLogic.obtenerDocentes()){
                    runOnUiThread(() -> {
                        listaDocentes = businessLogic.getListaDocentes();
                        cargarSpinnerDocentes();
                    });
                }
            } else if(tabla == 1){
                if(businessLogic.obtenerAsignaturas()){
                    runOnUiThread(() -> {
                        listaAsignaturas = businessLogic.getListaAsignaturas();
                        cargarSpinnerAsignaturas();
                    });
                }
            } else if(tabla == 2){
                if(businessLogic.obtenerAsignaturas()){
                    runOnUiThread(() -> {
                        listaClases = businessLogic.getListaClases();
                    });
                }
            } else if(tabla == 3){
                if(businessLogic.obtenerAsignaturas()){
                    runOnUiThread(() -> {
                        listaTutorias = businessLogic.getListaTutorias();
                    });
                }
            }


            return null;
        }
    }




}

