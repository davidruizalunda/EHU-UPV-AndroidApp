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
/**
 * Funciones de acceso a los datos.
 *
 * Data: 21/05/2021
 * @author David Ruiz Alunda
 * @version 1.0
 */
public class DataAccess {
    private static Store store;
    private static Folder emailFolder;
    private static String ldap;

    /** ////////////////////INFO////////////////////
     * Las diferentes tablas de la base de datos se numeran de esta forma:
     * 0: Docentes
     * 1: Asignatura
     * 2: Clase
     * 3: Tutoria
     * 4: Usuario
     * 5: Tarea
     * /////////////////////////////////////////////
     */

    /**
     * Función de comprobación y logueo de un alumno.
     *
     * nombre del servidor: ikasle.ehu.eus
     * puerto: imap 993 o pop 995
     * protocolo SSL
     *
     * @param ldap ldap del alumno
     * @param password contraseña del alumno
     * @return si todo sale correctamente devolverá true.
     */
    public boolean login(String ldap, String password) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Properties properties = new Properties();
            properties.put("mail.imap.host", privateData.getImapHost());
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            store = emailSession.getStore("imaps");
            store.connect("ikasle.ehu.eus", ldap, password);
            DataAccess.ldap = ldap;
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

    /**
     * Función que una vez abierta la conexión devuelve los correos.
     * @return devuelve una array de mensajes.
     */
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

    /**
     * Cierra la carpeta de correo abierta.
     */
    public void closeFolderCorreow() throws MessagingException {
        if (emailFolder != null && emailFolder.isOpen()){
            emailFolder.close(true);
            Log.d("Estado: ", "Close");
        }
    }

    /**
     * Método para obtener la información de las entidades de la bd
     * @param tabla tabla a la que se quiere acceder para obtener sus datos
     * @param usuario booleano por si se quiere acceder solo a la infomación de el usuario logeado.
     * @return la data obtenida
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String seleccionarTabla(int tabla, Boolean usuario){
        String sele_url;
        if (tabla == 0) {
            sele_url = privateData.getSeleccionarDocentes();
        } else if (tabla == 1) {
            sele_url = privateData.getSeleccionarAsignaturas();
        }else  if (tabla == 2) {
            sele_url = privateData.getSeleccionarClases();
        }else  if (tabla == 3) {
            sele_url = privateData.getSeleccionarTutorias();
        }else  if (tabla == 5) {
            sele_url = privateData.getSeleccionarTareas();
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

    /**
     * Método para eliminar una Asignatura de usuario
     * @param context contexto de la aplicación
     * @param asig_idS id de la asignatura a eliminar
     */
    public void eliminarAsignaturaUsuario(Context context, String asig_idS) {
        String sentencia = "DELETE FROM usuario WHERE asig_id=" + asig_idS + " and ldap = '" + ldap + "';";
        new eliminarDb(context).execute(sentencia);
    }

    /**
     * Método para eliminar un docente
     * @param context contexto de la aplicación
     */
    public void eliminarDocente(Context context, String correoS) {
        String sentencia = "DELETE FROM docente WHERE correo_ehu='" + correoS + "';";
        new eliminarDb(context).execute(sentencia);
    }

    /**
     * Método para eliminar una asignatura
     * @param context contexto de la aplicación
     * @param asig_idS id de la asignatura a eliminar
     */
    public void eliminarAsignatura(Context context, String asig_idS) {
        String sentencia = "DELETE FROM asignatura WHERE asig_id=" + asig_idS + ";";
        new eliminarDb(context).execute(sentencia);
    }

    /**
     * Método para eliminar una clase
     * @param context contexto de la aplicación
     * @param horaInicio
     * @param diaS
     * @param asig_idS id de la asignatura a eliminar
     */
    public void eliminarClase(Context context, String horaInicio, String aulaS, String diaS, String asig_idS) {
        String sentencia = "DELETE FROM clase WHERE horaInicio='" + horaInicio + "' AND aula ='" + aulaS + "'AND dia ='" + diaS + "' AND asig_id=" + asig_idS + ";";
        new eliminarDb(context).execute(sentencia);
    }

    /**
     * Método para eliminar una tutoría
     * @param context contexto de la aplicación
     */
    public void eliminarTutoria(Context context, String horaInicio, String profesorS, String diaS) {
        String sentencia = "DELETE FROM tutoria WHERE horaInicio='" + horaInicio + "' AND profesor ='" + profesorS + "'AND dia='" + diaS + "';";
        new DataAccess.eliminarDb(context).execute(sentencia);
    }



    /**
     * Método para poblar la base de datos
     */
    public static class insertarDb extends AsyncTask<String, Void, String> {

        private final WeakReference<Context> context;
        private final int tabla;

        /**
         * Constructora
         * @param context contexto de la aplicación
         * @param tabla tabla de la bd a la que se quiere insertar
         */
        public insertarDb(Context context, int tabla) {
            this.context = new WeakReference<>(context);
            this.tabla = tabla;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(String... strings) {
            String insertar_url;

            if(tabla==0){ //Insertar en docente
                insertar_url = privateData.getInsertarDocentes();
            }else if(tabla==1){ //Insertar en Asignatura
                insertar_url = privateData.getInsertarAsignaturas();
            }else if(tabla==2){//Insertar en Clase
                insertar_url = privateData.getInsertarClases();
            }else if(tabla==3){//Insertar en Tutoria
                insertar_url = privateData.getInsertarTutorias();
            }else if(tabla==4){//Insertar en Usuario
                insertar_url = privateData.getInsertarUsuarios();
            }else if(tabla==5){//Insertar en Tarea
                insertar_url = privateData.getInsertarTutorias();
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

                }else if(tabla==5) {
                    String tareaTextoS = strings[0];
                    String asignaturaIDS = strings[1];
                    String esLinkS = strings[2];
                    String urlLinkS = strings[3];

                    data =
                            URLEncoder.encode("tarea", "UTF-8") + "=" + URLEncoder.encode(tareaTextoS, "UTF-8") + "&" +
                            URLEncoder.encode("asig_id", "UTF-8") + "=" + URLEncoder.encode(asignaturaIDS, "UTF-8") + "&" +
                            URLEncoder.encode("eslink", "UTF-8") + "=" + URLEncoder.encode(esLinkS, "UTF-8") + "&" +
                            URLEncoder.encode("link", "UTF-8") + "=" + URLEncoder.encode(urlLinkS, "UTF-8");


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

        public eliminarDb(Context context) {
            this.context = new WeakReference<>(context);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(String... strings) {
            String eliminar_url;
            eliminar_url = privateData.getImapHost();
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
