package com.example.tfgprueba2;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import java.io.IOException;
import java.util.ArrayList;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class BusinessLogic {
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

    public ArrayList<String> cargarSpinner(RequestQueue requestQueue, Spinner spinerNombres, Context context){
        DataAccess dataAccess = new DataAccess();
        return dataAccess.seleccionarDocentes(requestQueue, spinerNombres, context);
    }
}
