package com.example.tfgprueba2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class Options extends AppCompatActivity {
    private EditText correo_EHU, nombre, apellidos, despacho;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        correo_EHU = findViewById(R.id.correo_EHU);
        nombre  = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        despacho = findViewById(R.id.despacho);

    }

    public void onButtonClick(View view){
        String correoS = correo_EHU.getText().toString();
        String nombreS = nombre.getText().toString();
        String apellidosS = apellidos.getText().toString();
        String despachoS = despacho.getText().toString();
        new insertarDocente(Options.this).execute(correoS,nombreS,apellidosS,despachoS);

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