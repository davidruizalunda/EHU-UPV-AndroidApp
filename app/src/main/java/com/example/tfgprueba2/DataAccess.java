package com.example.tfgprueba2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

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
    private static String ldap, password;

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
            store.connect("ikasle.ehu.eus", ldap, password);
            this.ldap = ldap;
            this.password = password;
            return true;

        }catch (AuthenticationFailedException e){
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
            emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            Log.d("Estado: ", "Open");
            return emailFolder.getMessages();


        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeFolderCorreow() throws MessagingException {
        if (emailFolder != null && emailFolder.isOpen()){
            emailFolder.close(true);
            Log.d("Estado: ", "Close");
        }
    }

    /*
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

     */


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String seleccionarTabla(int tabla, Boolean usuario){
        String sele_url;
        if (tabla == 0) {
            sele_url = "https://ehu-upv-androidapp-database.000webhostapp.com/seleccionarDocentes.php";
        } else if (tabla == 1) {
            sele_url = "https://ehu-upv-androidapp-database.000webhostapp.com/seleccionarAsignaturas.php";
        }else  if (tabla == 2) {
            sele_url = "https://ehu-upv-androidapp-database.000webhostapp.com/seleccionarClases.php";
        }else  if (tabla == 3) {
            sele_url = "https://ehu-upv-androidapp-database.000webhostapp.com/seleccionarTutorias.php";
        }else{
            sele_url = "";
        }

        String resultado="";
        try {
            URL url = new URL(sele_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            if(usuario){
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                String data;
                data = URLEncoder.encode("ldap", "UTF-8") + "=" + URLEncoder.encode(ldap, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
            }

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

            return resultado;

        }catch (Exception e){
            e.printStackTrace();
            return resultado;
        }
    }

    public void eliminarAsignaturaUsuario(Context context, String asig_idS) {
        String sentencia = "DELETE FROM usuario WHERE asig_id=" + asig_idS + " and ldap = '" + ldap + "';";
        new eliminarDb(context, 0).execute(sentencia);
    }

    public static class insertarDb extends AsyncTask<String, Void, String> {

        private final WeakReference<Context> context;
        private final int tabla;

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
            }else if(tabla==2){//Insertar en Clase
                insertar_url = "https://ehu-upv-androidapp-database.000webhostapp.com/insertarClase.php";
            }else if(tabla==3){//Insertar en Tutoria
                insertar_url = "https://ehu-upv-androidapp-database.000webhostapp.com/insertarTutoria.php";
            }else if(tabla==4){//Insertar en Usuario
                insertar_url = "https://ehu-upv-androidapp-database.000webhostapp.com/insertarUsuario.php";
            }else{
                insertar_url = "";
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
                    String url1S = strings[3];
                    String url2S = strings[4];

                    data =
                            URLEncoder.encode("abreviatura", "UTF-8") + "=" + URLEncoder.encode(abreviaturaS, "UTF-8") + "&" +
                            URLEncoder.encode("nombre", "UTF-8") + "=" + URLEncoder.encode(nombreS, "UTF-8") + "&" +
                            URLEncoder.encode("profesor", "UTF-8") + "=" + URLEncoder.encode(profesorS, "UTF-8")  + "&" +
                            URLEncoder.encode("url1", "UTF-8") + "=" + URLEncoder.encode(url1S, "UTF-8")  + "&" +
                            URLEncoder.encode("url2", "UTF-8") + "=" + URLEncoder.encode(url2S, "UTF-8") ;
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
                else if(tabla==4) {

                    String asig_IDS = strings[0];
                    data =
                            URLEncoder.encode("ldap", "UTF-8") + "=" + URLEncoder.encode(ldap, "UTF-8") + "&" +
                            URLEncoder.encode("asig_ID", "UTF-8") + "=" + URLEncoder.encode(asig_IDS, "UTF-8");

                }else{
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

    public static class eliminarDb extends AsyncTask<String, Void, String> {

        private final WeakReference<Context> context;
        private final int tabla;

        public eliminarDb(Context context, int tabla) {
            this.context = new WeakReference<>(context);
            this.tabla = tabla;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(String... strings) {
            String eliminar_url;
            eliminar_url = "https://ehu-upv-androidapp-database.000webhostapp.com/eliminar.php";
            String resultado;
            try{
                URL url = new URL(eliminar_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                String data;
                String sentenciaS = strings[0];

                data = URLEncoder.encode("sentencia", "UTF-8") + "=" + URLEncoder.encode(sentenciaS, "UTF-8");

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
