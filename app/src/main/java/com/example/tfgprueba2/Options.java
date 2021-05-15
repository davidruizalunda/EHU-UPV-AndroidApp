
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
    private Spinner spinerNombres, spinnerAsignaturas, spinnerClases, spinnerTutorias;
    private List<Docente> listaDocentes;
    private List<Asignatura> listaAsignaturas;
    private List<Clase> listaClases;
    private List<Tutoria> listaTutorias;
    private String[] dias ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        popupDocente = new Dialog(this);
        popupAsignatura = new Dialog(this);
        popupTutorias = new Dialog(this);
        popupClase = new Dialog(this);

        dias = new String[]{
                this.getResources().getString(R.string.lunes),
                this.getResources().getString(R.string.martes),
                this.getResources().getString(R.string.miercoles),
                this.getResources().getString(R.string.jueves),
                this.getResources().getString(R.string.viernes)
        };

    }

    public void onAddDocenteButtonClick(View view){

            popupDocente.setContentView(R.layout.popup_add_docente);
            popupDocente.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            EditText correo_EHU = popupDocente.findViewById(R.id.correoDocente_editText);
            EditText nombre = popupDocente.findViewById(R.id.nombreDocente_editText);
            EditText apellidos = popupDocente.findViewById(R.id.apellidosDocente_editText);
            EditText despacho = popupDocente.findViewById(R.id.despachoDocente_editText);
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

        EditText abreviatura = popupAsignatura.findViewById(R.id.abreviaturaAsignatura_editText);
        EditText nombre = popupAsignatura.findViewById(R.id.nombreAsignatura_editText);
        EditText url1 = popupAsignatura.findViewById(R.id.url1_editText);
        EditText url2 = popupAsignatura.findViewById(R.id.url2_editText);
        TextView seleccion = popupAsignatura.findViewById(R.id.seleccionActualAsignatura_textView);

        spinerNombres = popupAsignatura.findViewById(R.id.spinner10);
        spinnerAsignaturas = popupAsignatura.findViewById(R.id.spinner6);

        Button emptyAsignatura_button = popupAsignatura.findViewById(R.id.emptyAsignatura_button);
        Button removeAsignatura_button = popupAsignatura.findViewById(R.id.removeAsignatura_button);
        Button addAsignatura_button = popupAsignatura.findViewById(R.id.addAsignatura_button);

        new seleccionarDb(0).execute();
        new seleccionarDb(1).execute();
        popupAsignatura.show();

        spinnerAsignaturas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                abreviatura.setText(listaAsignaturas.get(position).getAbreviatura());
                nombre.setText(listaAsignaturas.get(position).getNombreAsignatura());


                String docenteSeleccionado = listaAsignaturas.get(position).getDocente1();
                LogicForAdmin logicForAdmin = new LogicForAdmin();
                spinerNombres.setSelection(logicForAdmin.finDocenteSpinnerPosition(docenteSeleccionado, listaDocentes));
                seleccion.setText(spinnerAsignaturas.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        emptyAsignatura_button.setOnClickListener(v -> {
            abreviatura.setText("");
            nombre.setText("");
            seleccion.setText(R.string.nuevaAsignatura);
            url1.setText("");
            url2.setText("");
        });

        removeAsignatura_button.setOnClickListener(v -> {
            String asig_idS = String.valueOf(listaAsignaturas.get(spinnerAsignaturas.getSelectedItemPosition()).getAsig_ID());
            String sentencia = "DELETE FROM asignatura WHERE asig_id=" + asig_idS + ";";
            new DataAccess.eliminarDb(Options.this, 0).execute(sentencia);
            abreviatura.setText("");
            nombre.setText("");
            url1.setText("");
            url2.setText("");
            new seleccionarDb(0).execute();
            new seleccionarDb(1).execute();
        });

        addAsignatura_button.setOnClickListener(v -> {
            String abreviaturaS = abreviatura.getText().toString();
            String nombreS = nombre.getText().toString();
            String profesorS = listaDocentes.get(spinerNombres.getSelectedItemPosition()).getCorreo_EHU();
            String url1S  = url1.getText().toString();
            String url2S = url2.getText().toString();
            new DataAccess.insertarDb(Options.this, 1).execute(abreviaturaS,nombreS,profesorS, url1S, url2S);
            new seleccionarDb(0).execute();
            new seleccionarDb(1).execute();
        });

    }

    public void onAddClaseButtonClick(View view){
        popupClase.setContentView(R.layout.popup_add_clase);
        popupClase.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText horaInicioClase = popupClase.findViewById(R.id.horaInicioClase_editTextTime);
        EditText horaFinClase = popupClase.findViewById(R.id.horaFinClase_editTextTime);
        EditText aula = popupClase.findViewById(R.id.dondeClase_editText);
        TextView seleccion = popupClase.findViewById(R.id.seleccionActualClase_textView);

        Spinner spinnerDias = popupClase.findViewById(R.id.spinner3);
        spinnerAsignaturas = popupClase.findViewById(R.id.spinner11);
        spinnerClases = popupClase.findViewById(R.id.spinnerlistaClasesClase);

        Button emptyClase_button = popupClase.findViewById(R.id.emptyClase_button);
        Button removeClase_button = popupClase.findViewById(R.id.removeClase_button);
        Button addClase_button = popupClase.findViewById(R.id.addClase_button);

        cargarSpinnerDias(spinnerDias);
        new seleccionarDb(1).execute();
        new seleccionarDb(2).execute();
        popupClase.show();

        spinnerClases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                horaInicioClase.setText(listaClases.get(position).getHoraInicio());
                horaFinClase.setText(listaClases.get(position).getHoraFin());
                aula.setText(listaClases.get(position).getAula());

                LogicForAdmin logicForAdmin = new LogicForAdmin();

                String diaSeleccionado = listaClases.get(position).getDia();
                spinnerDias.setSelection(Integer.parseInt(diaSeleccionado));

                String asignaturaSeleccionado = listaClases.get(position).getAsig_id();
                spinnerAsignaturas.setSelection(logicForAdmin.asignaturaSpinnerPosition(asignaturaSeleccionado, listaAsignaturas));

                seleccion.setText(listaClases.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        emptyClase_button.setOnClickListener(v -> {
            horaInicioClase.setText("");
            horaFinClase.setText("");
            aula.setText("");
            seleccion.setText(R.string.nuevaClase);
        });

        removeClase_button.setOnClickListener(v -> {
            String horaInicio = horaInicioClase.getText().toString();
            String horaFin = horaFinClase.getText().toString();
            String aulaS = aula.getText().toString();
            int asig_idS = listaAsignaturas.get(spinnerAsignaturas.getSelectedItemPosition()).getAsig_ID();

            String sentencia = "DELETE FROM clase WHERE horaInicio='" + horaInicio + "' AND aula ='" + aulaS + "'AND asig_id=" + asig_idS + ";";
            new DataAccess.eliminarDb(Options.this, 0).execute(sentencia);
            horaInicioClase.setText("");
            horaFinClase.setText("");
            aula.setText("");
            new seleccionarDb(1).execute();
            new seleccionarDb(2).execute();
        });

        addClase_button.setOnClickListener(v -> {
            String horaInicioClaseS = horaInicioClase.getText().toString();
            String horaFinClaseS = horaFinClase.getText().toString();
            String aulaS = aula.getText().toString();
            String asignaturaS = listaAsignaturas.get(spinnerAsignaturas.getSelectedItemPosition()).getAsig_ID() + "";
            String diaS = String.valueOf(spinnerDias.getSelectedItemPosition());
            new DataAccess.insertarDb(Options.this, 2).execute(horaInicioClaseS,horaFinClaseS,aulaS,diaS,asignaturaS);
            new seleccionarDb(1).execute();
            new seleccionarDb(2).execute();
        });
    }

    public void onAddTutoriasButtonClick(View view){
        popupTutorias.setContentView(R.layout.popup_add_tutoria);
        popupTutorias.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText horaInicioTutoria = popupTutorias.findViewById(R.id.horaInicioTutoria_editTextTime);
        EditText horaFinTutoria = popupTutorias.findViewById(R.id.horaFinTutoria_editTextTime);
        TextView seleccion = popupTutorias.findViewById(R.id.seleccionActualTutoria_textView);

        spinerNombres = popupTutorias.findViewById(R.id.spinner);
        spinnerTutorias = popupTutorias.findViewById(R.id.spinner6);
        Spinner spinnerDias = popupTutorias.findViewById(R.id.spinner2);

        Button addTutoria_button = popupTutorias.findViewById(R.id.addTutoria_button);
        Button emptyTutoria_button = popupTutorias.findViewById(R.id.emptyTutoria_button);
        Button removeTutoria_button = popupTutorias.findViewById(R.id.removeTutoria_button);


        cargarSpinnerDias(spinnerDias);
        new seleccionarDb(0).execute();
        new seleccionarDb(3).execute();
        popupTutorias.show();

        spinnerTutorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                horaInicioTutoria.setText(listaTutorias.get(position).getHoraInicio());
                horaFinTutoria.setText(listaTutorias.get(position).getHoraFin());

                LogicForAdmin logicForAdmin = new LogicForAdmin();

                String diaSeleccionado = listaTutorias.get(position).getDia();
                spinnerDias.setSelection(Integer.parseInt(diaSeleccionado));

                String docenteSeleccionado = listaTutorias.get(position).getProfesor();
                spinerNombres.setSelection(logicForAdmin.finDocenteSpinnerPosition(docenteSeleccionado, listaDocentes));

                seleccion.setText(listaTutorias.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        emptyTutoria_button.setOnClickListener(v -> {
            horaInicioTutoria.setText("");
            horaFinTutoria.setText("");
            seleccion.setText(R.string.nuevaTutoria);
        });

        removeTutoria_button.setOnClickListener(v -> {
            String horaInicio = horaInicioTutoria.getText().toString();
            String horaFin = horaFinTutoria.getText().toString();
            String profesorS = listaDocentes.get(spinerNombres.getSelectedItemPosition()).getCorreo_EHU();
            String diaS = String.valueOf(spinnerDias.getSelectedItemPosition());

            String sentencia = "DELETE FROM tutoria WHERE horaInicio='" + horaInicio + "' AND profesor ='" + profesorS + "'AND dia='" + diaS + "';";
            new DataAccess.eliminarDb(Options.this, 0).execute(sentencia);
            horaInicioTutoria.setText("");
            horaFinTutoria.setText("");
            new seleccionarDb(0).execute();
            new seleccionarDb(3).execute();
        });

        addTutoria_button.setOnClickListener(v -> {
            String horaInicioTutoriaS = horaInicioTutoria.getText().toString();
            String horaFinTutoriaS = horaFinTutoria.getText().toString();
            String profesorS = listaDocentes.get(spinerNombres.getSelectedItemPosition()).getCorreo_EHU();
            String diaS = String.valueOf(spinnerDias.getSelectedItemPosition());
            Log.d("horaInicioTutoria: ", horaInicioTutoriaS);
            Log.d("horaFinTutoriaS: ", horaFinTutoriaS);
            Log.d("profesorS: ", profesorS);
            Log.d("diaS: ", diaS);
            new DataAccess.insertarDb(Options.this, 3).execute(horaInicioTutoriaS,horaFinTutoriaS,profesorS,diaS);
            new seleccionarDb(0).execute();
            new seleccionarDb(3).execute();
        });

    }

    private void cargarSpinnerDias(Spinner spinnerDias) {

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

    private void cargarSpinnerTutorias() {
        ArrayAdapter<Tutoria> tutoriaAdapter = new ArrayAdapter<>(popupTutorias.getContext(), android.R.layout.simple_spinner_item, listaTutorias);
        tutoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tutoriaAdapter.notifyDataSetChanged();
        spinnerTutorias.setAdapter(tutoriaAdapter);
    }

    private void cargarSpinnerClases() {
        ArrayAdapter<Clase> claseAdapter = new ArrayAdapter<>(popupClase.getContext(), android.R.layout.simple_spinner_item, listaClases);
        claseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        claseAdapter.notifyDataSetChanged();
        spinnerClases.setAdapter(claseAdapter);
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
            LogicForAdmin logicForAdmin = new LogicForAdmin();
            if (tabla == 0){
                if(logicForAdmin.obtenerDocentes(false)){
                    runOnUiThread(() -> {
                        listaDocentes = logicForAdmin.getListaDocentes();
                        cargarSpinnerDocentes();
                    });
                }
            } else if(tabla == 1){
                if(logicForAdmin.obtenerAsignaturas(false)){
                    runOnUiThread(() -> {
                        listaAsignaturas = logicForAdmin.getListaAsignaturas();
                        cargarSpinnerAsignaturas();
                    });
                }
            } else if(tabla == 2){
                if(logicForAdmin.obtenerClases(false)){
                    runOnUiThread(() -> {
                        listaClases = logicForAdmin.getListaClases();
                        cargarSpinnerClases();
                    });
                }
            } else if(tabla == 3){
                if(logicForAdmin.obtenerTutorias(false)){
                    runOnUiThread(() -> {
                        listaTutorias = logicForAdmin.getListaTutorias();
                        cargarSpinnerTutorias();
                    });
                }
            }


            return null;
        }
    }




}

