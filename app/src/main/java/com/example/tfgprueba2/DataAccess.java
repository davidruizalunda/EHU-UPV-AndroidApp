package com.example.tfgprueba2;

import android.os.StrictMode;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class DataAccess {
    private static Store store;
    
    public boolean login(String ldap, String password){
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

            return true;
        /*
        } catch (NoSuchProviderException e) {

        } catch (MessagingException e) {
            button.setText("Entrar");
            Toast.makeText(this, "Comprueba tu usuario y/o contraseña y vuelve a intentarlo.", Toast.LENGTH_LONG).show();
            e.printStackTrace();

         */
        } catch (Exception e) {
            return false;
        }

    }

    public Message[] getMessages() throws MessagingException {
        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);
        return emailFolder.getMessages();
    }
}
