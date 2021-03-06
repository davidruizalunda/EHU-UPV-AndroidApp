package com.example.tfgprueba2;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class Logic {
    private final List<Docente> listaDocentes = new ArrayList<>();
    private final List<Asignatura> listaAsignaturas = new ArrayList<>();
    private final List<Clase> listaClases = new ArrayList<>();
    private final List<Tutoria> listaTutorias = new ArrayList<>();
    private final List<Tarea> listaTareas = new ArrayList<>();

    public List<Docente> getListaDocentes() {
            return listaDocentes;
    }

    public List<Asignatura> getListaAsignaturas() {
        return listaAsignaturas;
    }

    public List<Clase> getListaClases() {
        return listaClases;
    }

    public List<Tutoria> getListaTutorias() {
        return listaTutorias;
    }

    public List<Tarea> getListaTareas() {
        return listaTareas;
    }

    public Correow[] getCorreows(int numMails) throws MessagingException, IOException {

        DataAccess dataAccess = new DataAccess();
        Message[] messages = dataAccess.getMessages();
        Correow[] correows = new Correow[numMails];

        int x = numMails-1;
        for (int i = messages.length - numMails ; i < messages.length; i++) {
            Message message = messages[i];

            String from = message.getFrom()[0].toString();
            String data = message.getSentDate().toString();
            String content = getContentFromMessage(message);
            String subject = message.getSubject();

            Correow correow = new Correow();
            correow.setFrom(MimeUtility.decodeText(from).split("<")[0]);
            correow.setDate(data.substring(4,10));
            correow.setSubject(subject);
            if(content.startsWith("javax.mail.internet")){
                correow.setContents(null);
            }
            correow.setContents(content);

            correows[x] = correow;

            x--;

        }
        dataAccess.closeFolderCorreow();
        return correows;
    }

    public ArrayList<News> getEHUNews(Context applicationContext) throws ExecutionException, InterruptedException {
        RSSReader rssReader = new RSSReader(applicationContext);
        rssReader.execute().get();
        return rssReader.getNoticias();
    }

    private String getContentFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append("\n").append(bodyPart.getContent());
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result.append("\n").append(org.jsoup.Jsoup.parse(html).text());
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean obtenerDocentes(boolean usuario){
        listaDocentes.clear();
        DataAccess dataAccess = new DataAccess();
        String data=dataAccess.seleccionarTabla(0, usuario);
        if(!data.equalsIgnoreCase("")){
            JSONObject json;
            try {
                json = new JSONObject(data);
                JSONArray jsonArray = json.optJSONArray("docentes");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonArrayChild = jsonArray.getJSONObject(i);
                    Docente docente=new Docente(
                            jsonArrayChild.optString("correo_EHU"),
                            jsonArrayChild.optString("nombre"),
                            jsonArrayChild.optString("apellidos"),
                            jsonArrayChild.optString("n_despacho")
                    );
                    listaDocentes.add(docente);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean obtenerAsignaturas(boolean usuario){
        listaAsignaturas.clear();
        DataAccess dataAccess = new DataAccess();
        String data=dataAccess.seleccionarTabla(1, usuario);
        if(!data.equalsIgnoreCase("")){
            JSONObject json;
            try {
                json = new JSONObject(data);
                JSONArray jsonArray = json.optJSONArray("asignaturas");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonArrayChild = jsonArray.getJSONObject(i);
                    Asignatura asignatura=new Asignatura(
                            jsonArrayChild.optString("asig_id"),
                            jsonArrayChild.optString("abreviatura"),
                            jsonArrayChild.optString("nombre"),
                            jsonArrayChild.optString("profesor1"),
                            jsonArrayChild.optString("url1"),
                            jsonArrayChild.optString("url2")
                    );
                    listaAsignaturas.add(asignatura);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean obtenerClases(boolean usuario){
        listaClases.clear();
        DataAccess dataAccess = new DataAccess();
        String data=dataAccess.seleccionarTabla(2, usuario);
        if(!data.equalsIgnoreCase("")){
            JSONObject json;
            try {
                json = new JSONObject(data);
                JSONArray jsonArray = json.optJSONArray("clases");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonArrayChild = jsonArray.getJSONObject(i);
                    Clase clase=new Clase(
                            jsonArrayChild.optString("horaInicio"),
                            jsonArrayChild.optString("horaFin"),
                            jsonArrayChild.optString("aula"),
                            jsonArrayChild.optString("dia"),
                            jsonArrayChild.optString("asig_id")
                    );
                    listaClases.add(clase);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean obtenerTutorias(boolean usuario){
        listaTutorias.clear();
        DataAccess dataAccess = new DataAccess();
        String data=dataAccess.seleccionarTabla(3, usuario);
        if(!data.equalsIgnoreCase("")){
            JSONObject json;
            try {
                json = new JSONObject(data);
                JSONArray jsonArray = json.optJSONArray("tutorias");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonArrayChild = jsonArray.getJSONObject(i);
                    Tutoria tutoria = new Tutoria(
                            jsonArrayChild.optString("horaInicio"),
                            jsonArrayChild.optString("horaFin"),
                            jsonArrayChild.optString("profesor"),
                            jsonArrayChild.optString("dia")
                    );
                    listaTutorias.add(tutoria);
                }
                return true;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean obtenerTareas(boolean usuario){
        listaTareas.clear();
        DataAccess dataAccess = new DataAccess();
        String data=dataAccess.seleccionarTabla(5, usuario);
        if(!data.equalsIgnoreCase("")){
            JSONObject json;
            try {
                json = new JSONObject(data);
                JSONArray jsonArray = json.optJSONArray("tareas");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonArrayChild = jsonArray.getJSONObject(i);
                    Tarea tarea = new Tarea(
                            jsonArrayChild.optString("tarea"),
                            jsonArrayChild.optInt("asig_id"),
                            jsonArrayChild.optInt("eslink") == 1,
                            jsonArrayChild.optString("link")
                    );
                    listaTareas.add(tarea);
                }
                return true;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * M??todo para eliminar una Asignatura de usuario
     * @param context contexto de la aplicaci??n
     * @param asig_idS id de la asignatura a eliminar
     */
    public void eliminarAsignaturaUsuario(Context context, String asig_idS) {
        DataAccess dataAccess = new DataAccess();
        dataAccess.eliminarAsignaturaUsuario(context, asig_idS);
    }


    public void eliminarDocente(Context context, String correoS) {
        DataAccess dataAccess = new DataAccess();
        dataAccess.eliminarDocente(context, correoS);
    }


    public void eliminarAsignatura(Context context, String asig_idS) {
        DataAccess dataAccess = new DataAccess();
        dataAccess.eliminarAsignatura(context, asig_idS);
    }


    public void eliminarClase(Context context, String horaInicio, String aulaS, String diaS, String asig_idS) {
        DataAccess dataAccess = new DataAccess();
        dataAccess.eliminarClase(context, horaInicio, aulaS, diaS, asig_idS);
    }


    public void eliminarTutoria(Context context, String horaInicio, String profesorS, String diaS) {
        DataAccess dataAccess = new DataAccess();
        dataAccess.eliminarTutoria(context, horaInicio, profesorS, diaS);
    }



    public int finDocenteSpinnerPosition(String docenteSeleccionado, List<Docente> listaDocentesCargada) {
        int i;
        for(i=0; i<listaDocentesCargada.size(); i++){
            if (docenteSeleccionado.equals(listaDocentesCargada.get(i).getCorreo_EHU())){
                break;
            }
        }
        return i;
    }

    public int findDiaPosition(String diaSeleccionado, String[] spinnerDias) {
        int i;
        for(i=0; i<spinnerDias.length; i++){
            if (diaSeleccionado.equals(spinnerDias[i])){
                break;
            }
        }
        return i;
    }

    public int asignaturaSpinnerPosition(String asignaturaSeleccionado, List<Asignatura> listaAsignaturas) {
        int i;
        for(i=0; i<listaAsignaturas.size(); i++){
            if (asignaturaSeleccionado.equals(String.valueOf(listaAsignaturas.get(i).getAsig_ID()))){
                break;
            }
        }
        return i;
    }

    public void insertIntoUsuario(Activity activity, String asig_ID) {
        new DataAccess.insertarDb(activity, 4).execute(asig_ID);
    }

}
