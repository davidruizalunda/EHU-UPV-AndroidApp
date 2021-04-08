package com.example.tfgprueba2;

import android.os.StrictMode;
import android.util.Log;
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
}
