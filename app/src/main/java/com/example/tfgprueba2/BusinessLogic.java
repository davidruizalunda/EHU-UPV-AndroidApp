package com.example.tfgprueba2;

import android.content.Context;
import android.os.Build;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;

import com.android.volley.RequestQueue;

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

    public List<Docente> getListaDocentes() {
            return listaDocentes;
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
            //salida3.setText("Content: "+ message.getContent().toString() + "\n");

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

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }

    public void cargarSpinner(RequestQueue requestQueue, Spinner spinerNombres, Context context){
        DataAccess dataAccess = new DataAccess();
        dataAccess.seleccionarDocentes(requestQueue, spinerNombres, context);
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


}
