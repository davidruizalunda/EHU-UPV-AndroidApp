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


    public static class insertarDocente extends AsyncTask<String, Void, String> {

        private WeakReference<Context> context;

        public insertarDocente(Context context) {
            this.context = new WeakReference<>(context);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(String... strings) {
            String insertarDocente_url = "https://ehu-upv-androidapp-database.000webhostapp.com/insertarDocente.php";
            String resultado;
            try{
                URL url = new URL(insertarDocente_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

                Log.d("Correo: ", strings[0]);
                String correoS = strings[0];
                String nombreS = strings[1];
                String apellidosS = strings[2];
                String despachoS = strings[3];

                String data =
                        URLEncoder.encode("correo_EHU", "UTF-8") + "=" + URLEncoder.encode(correoS, "UTF-8") + "&" +
                                URLEncoder.encode("nombre", "UTF-8") + "=" + URLEncoder.encode(nombreS, "UTF-8") + "&" +
                                URLEncoder.encode("apellidos", "UTF-8") + "=" + URLEncoder.encode(apellidosS, "UTF-8") + "&" +
                                URLEncoder.encode("n_despacho", "UTF-8") + "=" + URLEncoder.encode(despachoS, "UTF-8");
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
