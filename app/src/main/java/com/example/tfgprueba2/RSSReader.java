package com.example.tfgprueba2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RSSReader extends AsyncTask<Void,Void,Void> {
    private static ArrayList<News> noticias;
    private Context context;
    private URL url;

    public ArrayList<News> getNoticias() {
        return noticias;
    }

    public RSSReader(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        procesarXML(obtenerDatos());
        return null;
    }

    private void procesarXML(Document data) {
        if(data != null){

            Element root = data.getDocumentElement();
            NodeList entrys = root.getChildNodes(); //Lista de entrys

            noticias = new ArrayList<>();
            for (int i=0; i < entrys.getLength(); i++) {
                Node hijoActual = entrys.item(i);

                if(hijoActual.getNodeName().equalsIgnoreCase("entry")){

                    NodeList entryChilds = hijoActual.getChildNodes();
                    News noticia = new News();


                    for(int j= 0 ; j < entryChilds.getLength(); j++){

                        Node actual = entryChilds.item(j);
                        if(actual.getNodeName().equalsIgnoreCase("title")){
                            noticia.setTitle(actual.getTextContent());
                        }
                        if(actual.getNodeName().equalsIgnoreCase("link")){
                            noticia.setLink(actual.getAttributes().item(1).getTextContent()); //href
                        }
                        if(actual.getNodeName().equalsIgnoreCase("author")){
                            if (actual.getChildNodes().item(1).getNodeName().equalsIgnoreCase("name")){
                                noticia.setAuthor(actual.getChildNodes().item(1).getTextContent());
                            }

                        }
                        if(actual.getNodeName().equalsIgnoreCase("published")){
                            /*
                            String pubDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                            SimpleDateFormat pubDateFormat = new SimpleDateFormat(pubDatePattern);
                            Date pubDate = pubDateFormat.parse(actual.getTextContent());
                            //"yyyy-MM-dd'T'HH:mm:ss'Z'"*/
                            noticia.setDate(actual.getTextContent());
                        }
                    }
                    noticias.add(noticia);

                }

            }

        }
    }

    public Document obtenerDatos(){
        try{
            url = new URL("https://www.ehu.eus/es/campusa/noticias/-/asset_publisher/MICMqglnC3fZ/rss");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET"); //GET POS
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDoc = builder.parse(inputStream); //Hacer petición a la url que creamos con esa dirección
            return xmlDoc;

        }catch(Exception e){
          e.printStackTrace();
          return null;
        }
    }
}
