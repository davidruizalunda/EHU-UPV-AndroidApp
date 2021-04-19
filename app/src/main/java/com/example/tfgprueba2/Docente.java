package com.example.tfgprueba2;

import androidx.annotation.NonNull;

public class Docente {
    private String correo_EHU;
    private String nombre;
    private String apellidos;
    private String despacho;

    public Docente(String correo_EHU, String nombre, String apellidos, String despacho) {
        this.correo_EHU = correo_EHU;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.despacho = despacho;
    }

    public String getCorreo_EHU() {
        return correo_EHU;
    }

    public void setCorreo_EHU(String correo_EHU) {
        this.correo_EHU = correo_EHU;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDespacho() {
        return despacho;
    }

    public void setDespacho(String despacho) {
        this.despacho = despacho;
    }

    @NonNull
    @Override
    public String toString() {
        return apellidos + ", " + nombre;
    }
}
