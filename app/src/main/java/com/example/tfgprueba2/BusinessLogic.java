package com.example.tfgprueba2;

import android.content.Context;
import java.io.IOException;
import java.util.ArrayList;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

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
            String content = message.getContent().toString();
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



}
