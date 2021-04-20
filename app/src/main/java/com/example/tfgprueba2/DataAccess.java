package com.example.tfgprueba2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class DataAccess {
    private static Store store;
    private static Folder emailFolder;
    private List<Docente> listaDocentes;
    private List<Asignatura> listaAsignaturas;

    public boolean login(String ldap, String password) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Properties properties = new Properties();
            properties.put("mail.imap.host", "ikasle.ehu.eus");
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            store = emailSession.getStore("imaps");
            store.connect("ikasle.ehu.eus", "877955", "0k9Hj88s0");
            Log.d("LOGEADO", "sds");

            return true;

        }catch (AuthenticationFailedException e){
            Log.d("Autenti", "sds");
            e.printStackTrace();
            return false;
        }catch (MessagingException e) { //Investigar nested exception
            e.printStackTrace();
            return false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public Message[] getMessages() {
        try{
            if (emailFolder != null && emailFolder.isOpen()){
                emailFolder.close(true);
                Log.d("Estado: ", "Close");
            }
            emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            Log.d("Estado: ", "Open");
            return emailFolder.getMessages();


        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Docente> seleccionarDocente(){
        return null;
    }



    public void seleccionarDocentes(RequestQueue requestQueue, Spinner spinner, Context context){
        ArrayList<Docente> listaDocentes1 = new ArrayList<>();
        String url = "https://ehu-upv-androidapp-database.000webhostapp.com/seleccionarDocentes.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("docentes");
                    System.out.println("Data: " + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Docente docente = new Docente(jsonObject.optString("correo_EHU"), "no importa", "no importa", "no importa");
                        listaDocentes1.add(docente);

                        for(int x=0 ; x < listaDocentes1.size(); x++){
                            System.out.println(listaDocentes1.get(x).getCorreo_EHU() + " " + x);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for(int x=0 ; x < listaDocentes1.size(); x++){
                    System.out.println(listaDocentes1.get(x).getCorreo_EHU() + " " + x+x);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
        System.out.println("//////////SASDASDA////////");
        for(int x=0 ; x < listaDocentes1.size(); x++){
            System.out.println(listaDocentes1.get(x).getCorreo_EHU() + " " + x+x);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String seleccionarTabla(int tabla){
        String sele_url;
        if (tabla == 0) {
            sele_url = "https://ehu-upv-androidapp-database.000webhostapp.com/seleccionarDocentes.php";
        } else if (tabla == 1) {
            sele_url = "https://ehu-upv-androidapp-database.000webhostapp.com/seleccionarAsignaturas.php";
        }else{
            sele_url = "";
        }

        String resultado="";
        try {
            URL url = new URL(sele_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
            resultado = stringBuilder.toString();
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            Log.d("RESULTADO: ", resultado);
            return resultado;

        }catch (Exception e){
            e.printStackTrace();
            return resultado;
        }
    }

    public static class insertarDb extends AsyncTask<String, Void, String> {

        private WeakReference<Context> context;
        private int tabla;

        public insertarDb(Context context, int tabla) {
            this.context = new WeakReference<>(context);
            this.tabla = tabla;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(String... strings) {
            String insertar_url;

            if(tabla==0){ //Insertar en docente
                insertar_url = "https://ehu-upv-androidapp-database.000webhostapp.com/insertarDocente.php";
            }else if(tabla==1){ //Insertar en Asignatura
                insertar_url = "https://ehu-upv-androidapp-database.000webhostapp.com/insertarAsignatura.php";
            }else if(tabla==2){
                insertar_url = "https://ehu-upv-androidapp-database.000webhostapp.com/insertarClase.php";
            }else if(tabla==3){
                insertar_url = "https://ehu-upv-androidapp-database.000webhostapp.com/insertarTutoria.php";
            }else{
                insertar_url = "https://ehu-upv-androidapp-database.000webhostapp.com/insertarClase.php";
            }

            String resultado;
            try{
                URL url = new URL(insertar_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                String data;
                if (tabla==0){
                    String correoS = strings[0];
                    Log.d("Correo: ", strings[0]);
                    String nombreS = strings[1];
                    String apellidosS = strings[2];
                    String despachoS = strings[3];

                    data =
                            URLEncoder.encode("correo_EHU", "UTF-8") + "=" + URLEncoder.encode(correoS, "UTF-8") + "&" +
                            URLEncoder.encode("nombre", "UTF-8") + "=" + URLEncoder.encode(nombreS, "UTF-8") + "&" +
                            URLEncoder.encode("apellidos", "UTF-8") + "=" + URLEncoder.encode(apellidosS, "UTF-8") + "&" +
                            URLEncoder.encode("n_despacho", "UTF-8") + "=" + URLEncoder.encode(despachoS, "UTF-8");

                }
                else if(tabla==1) { //Insertar en Asignatura
                    String abreviaturaS = strings[0];
                    String nombreS = strings[1];
                    String profesorS = strings[2];

                    data =
                            URLEncoder.encode("abreviatura", "UTF-8") + "=" + URLEncoder.encode(abreviaturaS, "UTF-8") + "&" +
                            URLEncoder.encode("nombre", "UTF-8") + "=" + URLEncoder.encode(nombreS, "UTF-8") + "&" +
                            URLEncoder.encode("profesor", "UTF-8") + "=" + URLEncoder.encode(profesorS, "UTF-8");
                }
                else if(tabla==2){
                    String horaInicioS = strings[0];
                    String horaFinS = strings[1];
                    String aulaS = strings[2];
                    String diaS = strings[3];
                    String asig_idS = strings[4];

                    data =
                            URLEncoder.encode("horaInicio", "UTF-8") + "=" + URLEncoder.encode(horaInicioS, "UTF-8") + "&" +
                            URLEncoder.encode("horaFin", "UTF-8") + "=" + URLEncoder.encode(horaFinS, "UTF-8") + "&" +
                            URLEncoder.encode("aula", "UTF-8") + "=" + URLEncoder.encode(aulaS, "UTF-8") + "&" +
                            URLEncoder.encode("dia", "UTF-8") + "=" + URLEncoder.encode(diaS, "UTF-8") + "&" +
                            URLEncoder.encode("asig_id", "UTF-8") + "=" + URLEncoder.encode(asig_idS, "UTF-8");
                }
                else if(tabla==3) {
                    String horaInicioS = strings[0];
                    String horaFinS = strings[1];
                    String profesorS = strings[2];
                    String diaS = strings[3];

                    data =
                            URLEncoder.encode("horaInicio", "UTF-8") + "=" + URLEncoder.encode(horaInicioS, "UTF-8") + "&" +
                            URLEncoder.encode("horaFin", "UTF-8") + "=" + URLEncoder.encode(horaFinS, "UTF-8") + "&" +
                            URLEncoder.encode("profesor", "UTF-8") + "=" + URLEncoder.encode(profesorS, "UTF-8") + "&" +
                            URLEncoder.encode("dia", "UTF-8") + "=" + URLEncoder.encode(diaS, "UTF-8");

                }
                else{
                    data = "";
                }

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine())!=null){
                    stringBuilder.append(line);
                }
                resultado = stringBuilder.toString();
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            }catch (Exception e){
                e.printStackTrace();
                resultado = "error";
            }
            return resultado;
        }

        protected void onPostExecute(String resultado){
            Toast.makeText(context.get(), resultado, Toast.LENGTH_LONG).show();
        }
    }


}
