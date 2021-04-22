package com.example.tfgprueba2;

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

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class BusinessLogic {
    private final List<Docente> listaDocentes = new ArrayList<>();
    private final List<Asignatura> listaAsignaturas = new ArrayList<>();
    private final List<Clase> listaClases = new ArrayList<>();
    private final List<Tutoria> listaTutorias = new ArrayList<>();

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
        return correows;
    }

    public ArrayList<News> getEHUNews(Context context){
        RSSReader rssReader = new RSSReader(context);
        rssReader.execute();
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
    public boolean obtenerDocentes(){
        listaDocentes.clear();
        DataAccess dataAccess = new DataAccess();
        String data=dataAccess.seleccionarTabla(0);
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
                            null
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
    public boolean obtenerAsignaturas(){
        listaAsignaturas.clear();
        DataAccess dataAccess = new DataAccess();
        String data=dataAccess.seleccionarTabla(1);
        if(!data.equalsIgnoreCase("")){
            JSONObject json;
            try {
                json = new JSONObject(data);
                JSONArray jsonArray = json.optJSONArray("asignaturas");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonArrayChild = jsonArray.getJSONObject(i);
                    Asignatura asignatura=new Asignatura(
                            jsonArrayChild.optString("abreviatura"),
                            jsonArrayChild.optString("nombre"),
                            jsonArrayChild.optString("profesor1"),
                            jsonArrayChild.optString("asig_id")
                    );
                    Log.d("ASIGNATURA: ", jsonArrayChild.optString("nombre"));
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
    public boolean obtenerClases(){
        listaClases.clear();
        DataAccess dataAccess = new DataAccess();
        String data=dataAccess.seleccionarTabla(2);
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
    public boolean obtenerTutorias(){
        listaTutorias.clear();
        DataAccess dataAccess = new DataAccess();
        String data=dataAccess.seleccionarTabla(3);
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
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }


}
