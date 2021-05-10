package com.example.tfgprueba2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.widget.Toast;

/*
Tipo de servidor: IMAP o POP  (Mejor utiliza IMAP)
Nombre de servidor: ikasle.ehu.eus
Puerto: imap 993, pop 995
Protocolo SSL
Usuario: Tu login LDAP
Contraseña: Tu contraseña
*/
public class HomeActivity extends AppCompatActivity {
    private TextView diaTextView;
    private ImageView calendario;
    private Dialog popupCorreow, popup_edit_user_asignaturas, popupAsignaturasUsuarios, popupCalendario, popupEditable, popupDocenteAsignaturaUsuario;
    private Spinner spinnerAsignaturasUsuario, spinnerAsignaturas;
    private final int TIEMPO = 60000;
    private Button editable1, editable2;
    private boolean terminado;
    Handler h = new Handler();
    private ScaleGestureDetector detector;
    private float xInicial = 0;
    private float yInicial = 0;
    int cont = 0;
    int dia;
    private List<Asignatura> listaAsignaturas, listaAsignaturasUsuario;
    private List<Clase>[] listaClasesPorDia = new List[5];
    private List<Clase> listaClasesUsuario;
    private List<Docente> listaDocentesUsuario;
    private List<Tutoria> listaTutoriasUsuario;
    private final Map<String, Asignatura> asignaturasMap = new HashMap<String, Asignatura>();
    private final Map<String, Docente> docentesMap = new HashMap<String, Docente>();
    private final Map<String, ArrayList<Tutoria>> tutoriasMap = new HashMap<String, ArrayList<Tutoria>>();

    private final Map<String, List<Clase>> clasesAlDiaMap = new HashMap<>();
    private String[] dias;
    private String editable1_title, editable2_title, editable1_url, editable2_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedPreferences = getSharedPreferences("accesosDirectos", Context.MODE_PRIVATE);
        editable1_title = sharedPreferences.getString("titulo_editable1", "");
        editable2_title = sharedPreferences.getString("titulo_editable2", "");

        editable1_url = sharedPreferences.getString("enlace_editable1", "");
        editable2_url = sharedPreferences.getString("enlace_editable2", "");

        editable1 = findViewById(R.id.editable1_button);
        editable2 = findViewById(R.id.editable2_button);

        editable1.setText(editable1_title);
        editable2.setText(editable2_title);

        spinnerAsignaturasUsuario = findViewById(R.id.spinner7);
        diaTextView = findViewById(R.id.dia_textView);
        limpiarArrayListaClasesPorDia();

        dias = new String[]{
                this.getResources().getString(R.string.lunes),
                this.getResources().getString(R.string.martes),
                this.getResources().getString(R.string.miercoles),
                this.getResources().getString(R.string.jueves),
                this.getResources().getString(R.string.viernes)
        };

        popup_edit_user_asignaturas = new Dialog(this);
        popupCorreow = new Dialog(this);
        popupAsignaturasUsuarios = new Dialog(this);
        popupCalendario = new Dialog(this);
        popupEditable = new Dialog(this);
        popupDocenteAsignaturaUsuario = new Dialog(this);

        editable1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                popupEditable(1);
                return true;
            }
        });

        editable2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                popupEditable(2);
                return true;
            }
        });

        new seleccionarDb(this, 99, true).execute();

    }
    @Override
    public void onStart() {
        super.onStart();
        terminado = false;
        updateAutomatically();
    }

    public void updateAutomatically(){
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if(!terminado){
                    updateDataCorreowsNews();
                    h.postDelayed(this, TIEMPO);
                }
            }

        }, 0);
    }

    public void updateDataCorreowsNews() {
        LogicForAdmin logicForAdmin = new LogicForAdmin();
        Thread thread = new Thread(() -> {

            try {
                ArrayList<News> news = logicForAdmin.getEHUNews(getApplicationContext());
                Log.d("NEWS: ", news.size()+"");
                h.post(() -> updateAdapterNews(news));

            }
            catch (Exception e){
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {

            try {
                Correow[] correows = logicForAdmin.getCorreows(10);
                h.post(() -> updateAdapterCorreows(correows));

            }catch (Exception e){
                e.printStackTrace();
            }
        });
        thread.start();
        thread2.start();
    }

    public void updateAdapterCorreows(Correow[] correows){
            MyMailsListAdapter mailsAdapter=new MyMailsListAdapter(this, correows);
            ListView mailListView = findViewById(R.id.mailListView);
            mailListView.setAdapter(mailsAdapter);

            mailListView.setOnItemClickListener((parent, view, position, id) -> {
                popupCorreow.setContentView(R.layout.popup_view_correows);
                popupCorreow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView from = popupCorreow.findViewById(R.id.from_popup);
                TextView subject = popupCorreow.findViewById(R.id.subject_popup);
                TextView date = popupCorreow.findViewById(R.id.date_popup);
                TextView content = popupCorreow.findViewById(R.id.content_popup);

                from.setText(correows[position].getFrom());
                subject.setText(correows[position].getSubject());
                date.setText(correows[position].getDate());
                content.setText(correows[position].getContents());

                popupCorreow.show();
            });
    }

    public void updateAdapterNews(ArrayList<News> news){
        MyNewsListAdapter newsAdapter=new MyNewsListAdapter(this, news);
        ListView newsListView = findViewById(R.id.newsListView);
        newsListView.setAdapter(newsAdapter);

        newsListView.setOnItemClickListener((parent, view, position, id) -> {
            Uri uri = Uri.parse(news.get(position).getLink());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    public void onCalendarioButtonClick(View view){
        popupCalendario.setContentView(R.layout.popup_calendario);
        popupCalendario.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        calendario = popupCalendario.findViewById(R.id.calendario_imageView);
        /*
        xInicial = calendario.getScaleX();
        yInicial = calendario.getScaleY();
        detector = new ScaleGestureDetector(popupCalendario.getContext(), new ScaleListener(calendario));
         */
        popupCalendario.show();

    }

    public void onAsignaturasEditButtonClick(View view){
        popup_edit_user_asignaturas.setContentView(R.layout.popup_edit_user_asignaturas);
        popup_edit_user_asignaturas.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        spinnerAsignaturas = popup_edit_user_asignaturas.findViewById(R.id.spinner9);
        spinnerAsignaturasUsuario = popup_edit_user_asignaturas.findViewById(R.id.spinner8);
        Button buttonAddAsignaturaUsuario = popup_edit_user_asignaturas.findViewById(R.id.add_asignaturaUsuario_button);
        Button buttonRemoveAsignaturaUsuario = popup_edit_user_asignaturas.findViewById(R.id.remove_asignaturaUsuario_button);
        new seleccionarDb(this, 1, false).execute();
        new seleccionarDb(this, 1, true).execute();
        popup_edit_user_asignaturas.show();

        buttonAddAsignaturaUsuario.setOnClickListener(v -> {
            LogicForAdmin logicForAdmin = new LogicForAdmin();
            logicForAdmin.insertIntoUsuario(HomeActivity.this , String.valueOf(listaAsignaturas.get(spinnerAsignaturas.getSelectedItemPosition()).getAsig_ID()));
            new seleccionarDb(this, 1, false).execute();
            new seleccionarDb(this, 1, true).execute();
            new seleccionarDb(this, 99, true).execute();
        });

        buttonRemoveAsignaturaUsuario.setOnClickListener(v -> {
            String asig_idS = String.valueOf(listaAsignaturasUsuario.get(spinnerAsignaturasUsuario.getSelectedItemPosition()).getAsig_ID());
            LogicForAdmin logicForAdmin = new LogicForAdmin();
            logicForAdmin.eliminarAsignaturaUsuario(this, asig_idS);
            new seleccionarDb(this, 1, false).execute();
            new seleccionarDb(this, 1, true).execute();
            new seleccionarDb(this, 99, true).execute();
        });


    }

    public void onSiguienteButtonClick(View view){
        if(dia == 4){
            dia = 0;
        }else{
            dia++;
        }
        diaTextView.setText(dias[dia]);
        cargarListViewAsignaturas(dia);
    }

    public void onAnteriorButtonClick(View view){
        if(dia == 0){
            dia = 4;
        }else{
            dia--;
        }
        diaTextView.setText(dias[dia]);
        cargarListViewAsignaturas(dia);
    }

    public void onEgelaButtonClick(View view){
        Uri uri = Uri.parse("https://egela.ehu.eus/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onGaurButtonClick(View view){
        Uri uri = Uri.parse("https://gestion.ehu.es/gaur");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onOptions(View view){
        terminado = true;
        Intent home = new Intent(this, Options.class);
        startActivity(home);
    }

    private void cargarSpinnerAsignaturas() {
        ArrayAdapter<Asignatura> asignaturasAdapter = new ArrayAdapter<>(this , android.R.layout.simple_spinner_item, listaAsignaturas);
        asignaturasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        asignaturasAdapter.notifyDataSetChanged();
        spinnerAsignaturas.setAdapter(asignaturasAdapter);
    }

    private void cargarSpinnerAsignaturasUsuario() {
        ArrayAdapter<Asignatura> asignaturasUsuarioAdapter = new ArrayAdapter<>(this , android.R.layout.simple_spinner_item, listaAsignaturasUsuario);
        asignaturasUsuarioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        asignaturasUsuarioAdapter.notifyDataSetChanged();
        spinnerAsignaturasUsuario.setAdapter(asignaturasUsuarioAdapter);
    }

    public void cargarListViewAsignaturas(int dia){
        diaTextView.setText(dias[dia]);
        MySubjectsViewListAdapter subjectsAdapter=new MySubjectsViewListAdapter(this, listaClasesPorDia[dia], asignaturasMap);
        ListView subjectListView = findViewById(R.id.asignaturasListView);
        subjectListView.setAdapter(subjectsAdapter);

        subjectListView.setOnItemClickListener((parent, view, position, id) -> {
            popupInfoAsignatura(listaClasesPorDia[dia].get(position));
        });
    }

    private void popupInfoAsignatura(Clase clase) {

        popupAsignaturasUsuarios.setContentView(R.layout.popup_ver_asignaturas_usuario);
        popupAsignaturasUsuarios.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView nombreAsignaturaUsuario = popupAsignaturasUsuarios.findViewById(R.id.nombreAsignaturaUsuario_textView);
        TextView horaInicioAsignaturaUsuario = popupAsignaturasUsuarios.findViewById(R.id.horaInicioAsignaturaUsuario_textView);
        TextView horaFinAsignaturaUsuario = popupAsignaturasUsuarios.findViewById(R.id.horaFinAsignaturaUsuario_textView);
        TextView dondeAsignaturaUsuario = popupAsignaturasUsuarios.findViewById(R.id.dondeAsignaturaUsuario_textView);
        TextView docenteAsignaturaUsuario = popupAsignaturasUsuarios.findViewById(R.id.docenteAsignaturaUsuario_textView);
        Button docenteAsignaturaUsuario_button = popupAsignaturasUsuarios.findViewById(R.id.docenteAsignaturaUsuario_button);

        nombreAsignaturaUsuario.setText(asignaturasMap.get(String.valueOf(clase.getAsig_id())).getNombreAsignatura());
        horaInicioAsignaturaUsuario.setText(clase.getHoraInicio());
        horaFinAsignaturaUsuario.setText(clase.getHoraFin());
        dondeAsignaturaUsuario.setText(clase.getAula());
        docenteAsignaturaUsuario.setText(asignaturasMap.get(String.valueOf(clase.getAsig_id())).getDocente1());
        popupAsignaturasUsuarios.show();

        docenteAsignaturaUsuario_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAsignaturasUsuarios.hide();
                popupInfoDocente(asignaturasMap.get(String.valueOf(clase.getAsig_id())).getDocente1());
            }
        });

    }

    private void popupInfoDocente(String correo_EHU) {
        popupDocenteAsignaturaUsuario.setContentView(R.layout.popup_info_docente);
        popupDocenteAsignaturaUsuario.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Docente docente = docentesMap.get(correo_EHU);
        ArrayList<Tutoria> tutorias = tutoriasMap.get(correo_EHU);

        TextView nombre = popupDocenteAsignaturaUsuario.findViewById(R.id.nombreDocenteInfo_textView);
        TextView correo = popupDocenteAsignaturaUsuario.findViewById(R.id.correoDocenteInfo_textView2);
        TextView despacho = popupDocenteAsignaturaUsuario.findViewById(R.id.despachoDocenteInfo_textView);
        ListView subjectListView = popupDocenteAsignaturaUsuario.findViewById(R.id.tutoriasListView);

        MyTutoriasViewListAdapter subjectsAdapter=new MyTutoriasViewListAdapter(this, tutoriasMap.get(correo_EHU));
        subjectListView.setAdapter(subjectsAdapter);

        nombre.setText(docente.getNombre() + " " + docente.getApellidos());
        correo.setText(docente.getCorreo_EHU());
        despacho.setText(docente.getDespacho());
        popupDocenteAsignaturaUsuario.show();

    }

    private void popupEditable(int editable){
        popupEditable.setContentView(R.layout.popup_editable);
        popupEditable.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText tituloEditable = popupEditable.findViewById(R.id.tituloEditable_editText);
        EditText urlEditable = popupEditable.findViewById(R.id.urlEditable_editText);
        Button editable_Button = popupEditable.findViewById(R.id.addEditable_button);

        if (editable == 1){
            tituloEditable.setText(editable1_title);
            urlEditable.setText(editable1_url);
        }else{
            tituloEditable.setText(editable2_title);
            urlEditable.setText(editable2_url);
        }

        popupEditable.show();

        editable_Button.setOnClickListener(v -> {
            guardarEditable(tituloEditable.getText().toString(), urlEditable.getText().toString(), editable);
            popupEditable.hide();
        });


    }

    private void guardarEditable(String titulo_editable, String url_editable, int editable) {
        SharedPreferences sharedPreferences = getSharedPreferences("accesosDirectos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editable1_title = sharedPreferences.getString("titulo_editable1", "");
        editable2_title = sharedPreferences.getString("titulo_editable2", "");

        editable1_url = sharedPreferences.getString("enlace_editable1", "");
        editable2_url = sharedPreferences.getString("enlace_editable2", "");

        if (editable == 1){
            editor.putString("titulo_editable1", titulo_editable);
            editor.putString("enlace_editable1", url_editable);
        }else{
            editor.putString("titulo_editable2", titulo_editable);
            editor.putString("enlace_editable2", url_editable);
        }
        editor.commit();
    }

    public void onEditable1ButtonClick(View view){
        try{
            Uri uri = Uri.parse(editable1_url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(popupEditable.getContext(), "Introduzca una URL válida", Toast.LENGTH_SHORT).show();
            Toast.makeText(popupEditable.getContext(), "FORMATO: https://paginaweb.es/", Toast.LENGTH_LONG).show();
        }


    }

    public void onEditable2ButtonClick(View view){
        try {
            Uri uri = Uri.parse(editable2_url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(popupEditable.getContext(), "Introduzca una URL válida", Toast.LENGTH_SHORT).show();
            Toast.makeText(popupEditable.getContext(), "FORMATO: https://paginaweb.es/", Toast.LENGTH_LONG).show();
        }
    }


    class seleccionarDb extends AsyncTask<String,String,String> {
        private Context context;
        private int table;
        private boolean usuario;

        public
        seleccionarDb(Context context, int table, boolean usuario) {
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
                        cargarSpinnerAsignaturasUsuario();
                    });
                }
            }else if(table == 99 && usuario){
                if(logicForAdmin.obtenerAsignaturas(true) && logicForAdmin.obtenerClases(true) && logicForAdmin.obtenerDocentes(true) && logicForAdmin.obtenerTutorias(true)){
                    runOnUiThread(() -> {
                        limpiarArrayListaClasesPorDia();
                        listaAsignaturasUsuario = logicForAdmin.getListaAsignaturas();
                        listaClasesUsuario = logicForAdmin.getListaClases();
                        listaDocentesUsuario = logicForAdmin.getListaDocentes();
                        listaTutoriasUsuario = logicForAdmin.getListaTutorias();

                        for(int i=0; i<listaAsignaturasUsuario.size(); i++){
                            asignaturasMap.put(String.valueOf(listaAsignaturasUsuario.get(i).getAsig_ID()), listaAsignaturasUsuario.get(i));
                        }
                        for(int x=0; x<listaClasesUsuario.size(); x++){
                            listaClasesPorDia[Integer.parseInt(listaClasesUsuario.get(x).getDia())].add(listaClasesUsuario.get(x));
                        }

                        Log.d("ESPACIO1: ", listaDocentesUsuario.size()+"");
                        for(int z=0; z<listaDocentesUsuario.size(); z++){
                            docentesMap.put(listaDocentesUsuario.get(z).getCorreo_EHU(), listaDocentesUsuario.get(z));
                        }

                        Log.d("ESPACIO: ", listaTutoriasUsuario.size()+"");
                        for(int y = 0; y < listaTutoriasUsuario.size(); y ++){
                            if (tutoriasMap.get(listaTutoriasUsuario.get(y).getProfesor()) == null){
                                Log.d("Rellenando vacio: ", listaTutoriasUsuario.get(y).getProfesor());
                                tutoriasMap.put(listaTutoriasUsuario.get(y).getProfesor(), new ArrayList<>());
                            }
                            tutoriasMap.get(listaTutoriasUsuario.get(y).getProfesor()).add(listaTutoriasUsuario.get(y));
                            Log.d("Rellenando de: ", listaTutoriasUsuario.get(y).getHoraInicio());
                        }

                        cargarListViewAsignaturas(0);


                    });
                }
            }

            return null;
        }

    }

    private void limpiarArrayListaClasesPorDia() {
        for (int i=0; i<listaClasesPorDia.length; i++){
            listaClasesPorDia[i] = new ArrayList<>();
        }
    }


}