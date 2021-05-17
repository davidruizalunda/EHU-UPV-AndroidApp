package com.example.tfgprueba2;

import androidx.annotation.NonNull;

public class Tarea {
    private String textoTarea;
    private int asig_ID;
    private boolean esEnlace;
    private String url;

    public Tarea(String textoTarea, int asig_ID, boolean esEnlace, String url) {
        this.textoTarea = textoTarea;
        this.asig_ID = asig_ID;
        this.esEnlace = esEnlace;
        this.url = url;
    }

    public String getTextoTarea() {
        return textoTarea;
    }

    public void setTextoTarea(String textoTarea) {
        this.textoTarea = textoTarea;
    }

    public int getAsig_ID() {
        return asig_ID;
    }

    public void setAsig_ID(int asig_ID) {
        this.asig_ID = asig_ID;
    }

    public boolean isEsEnlace() {
        return esEnlace;
    }

    public void setEsEnlace(boolean esEnlace) {
        this.esEnlace = esEnlace;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
