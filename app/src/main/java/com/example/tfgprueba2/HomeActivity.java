package com.example.tfgprueba2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;

import org.w3c.dom.Text;

/*
Tipo de servidor: IMAP o POP  (Mejor utiliza IMAP)
Nombre de servidor: ikasle.ehu.eus
Puerto: imap 993, pop 995
Protocolo SSL
Usuario: Tu login LDAP
Contraseña: Tu contraseña
*/
public class HomeActivity extends AppCompatActivity {
    private TextView textView5;
    private Dialog popupCorreow, popup_edit_user_asignaturas, popupAsignaturasUsuarios;
    private Spinner spinnerAsignaturasUsuario, spinnerAsignaturas;
    private final int TIEMPO = 60000;
    private boolean terminado;
    Handler h = new Handler();
    int cont = 0;
    private List<Asignatura> listaAsignaturas, listaAsignaturasUsuario;
    private List<Clase> listaClasesUsuario;
    private final Map<String, Asignatura> asignaturasMap = new HashMap<String, Asignatura>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       // textView5 = findViewById(R.id.textView5);
        popup_edit_user_asignaturas = new Dialog(this);
        popupCorreow = new Dialog(this);
        popupAsignaturasUsuarios = new Dialog(this);
        spinnerAsignaturasUsuario = findViewById(R.id.spinner7);
        new seleccionarDb(this, 99, true).execute();
        Log.d("MAP: ", asignaturasMap.size()+"");

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
                    Log.d("update ", "//////////////////////////////");
                    updateDataCorreowsNews();
                    h.postDelayed(this, TIEMPO);
                }
            }

        }, 0);
    }

    public void updateDataCorreowsNews() {
        Thread thread = new Thread(() -> {
            RSSReader lectorRSS = new RSSReader(getApplicationContext());
            lectorRSS.execute();

            try {
                LogicForAdmin logicForAdmin = new LogicForAdmin();
                ArrayList<News> news = logicForAdmin.getEHUNews(getApplicationContext());
                Log.d("NEWS: ", news.size()+"");
                h.post(() -> {
                    updateAdapterNews(news);
                });

            }catch (Exception e){
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {

            try {
                LogicForAdmin logicForAdmin = new LogicForAdmin();
                Correow[] correows = logicForAdmin.getCorreows(10);
                h.post(() -> {
                    updateAdapterCorreows(correows);
                });

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
            Log.d("ASIG ID", asig_idS);
            logicForAdmin.eliminarAsignaturaUsuario(this, asig_idS);
            new seleccionarDb(this, 1, false).execute();
            new seleccionarDb(this, 1, true).execute();
            new seleccionarDb(this, 99, true).execute();
        });


    }

    public void onEgelaButtonClick(View view){
        Uri uri = Uri.parse("https://egela.ehu.eus/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onGaurButtonClick(View view){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.app.gaur");
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(intent);
    }

    public void onOptions(View view){
        terminado = true;
        Intent home = new Intent(this, Options.class);
        startActivity(home);
    }

    public void onPreferences(View view){
        terminado = true;
        Intent home = new Intent(this, PreferencesActivity.class);
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

    public void cargarListViewAsignaturas(){
        Log.d("MAP: ", asignaturasMap.size()+"");
        for(int i=0; i<listaAsignaturasUsuario.size(); i++){
            asignaturasMap.put(String.valueOf(listaAsignaturasUsuario.get(i).getAsig_ID()), listaAsignaturasUsuario.get(i));
        }
        Log.d("MAP: ", asignaturasMap.size()+"");
        MySubjectsViewListAdapter subjectsAdapter=new MySubjectsViewListAdapter(this, listaClasesUsuario, asignaturasMap);
        ListView subjectListView = findViewById(R.id.asignaturasListView);
        subjectListView.setAdapter(subjectsAdapter);

        subjectListView.setOnItemClickListener((parent, view, position, id) -> {
            popupInfoAsignatura(listaClasesUsuario.get(position));
        });
    }

    private void popupInfoAsignatura(Clase clase) {

        popupAsignaturasUsuarios.setContentView(R.layout.popup_ver_asignaturas_usuario);
        popupAsignaturasUsuarios.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView nombreAsignaturaUsuario = popupAsignaturasUsuarios.findViewById(R.id.nombreAsignaturaUsuario_textView);
        TextView horaInicioAsignaturaUsuario = popupAsignaturasUsuarios.findViewById(R.id.horaInicioAsignaturaUsuario_textView);
        TextView horaFinAsignaturaUsuario = popupAsignaturasUsuarios.findViewById(R.id.horaFinAsignaturaUsuario_textView);
        TextView dondeAsignaturaUsuario = popupAsignaturasUsuarios.findViewById(R.id.dondeAsignaturaUsuario_textView);

        nombreAsignaturaUsuario.setText(asignaturasMap.get(String.valueOf(clase.getAsig_id())).getNombreAsignatura());
        horaInicioAsignaturaUsuario.setText(clase.getHoraInicio());
        horaFinAsignaturaUsuario.setText(clase.getHoraFin());
        dondeAsignaturaUsuario.setText(clase.getAula());
        popupAsignaturasUsuarios.show();

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
                        cargarSpinnerAsignaturasUsuario();
                    });
                }
            }else if(table == 99 && usuario){
                if(logicForAdmin.obtenerAsignaturas(true) && logicForAdmin.obtenerClases(true)){
                    runOnUiThread(() -> {
                        listaAsignaturasUsuario = logicForAdmin.getListaAsignaturas();
                        listaClasesUsuario = logicForAdmin.getListaClases();
                        cargarListViewAsignaturas();

                    });
                }
            }

            return null;
        }

    }




}