package com.example.tfgprueba2;

import androidx.annotation.NonNull;
/**
 * Este es el objeto Asignatura.
 *
 * Data: 21/05/2021
 * @author David Ruiz Alunda
 * @version 1.0
 */
public class Asignatura {
    private int asig_ID;
    private String abreviatura;
    private String nombreAsignatura;
    private String docente1;
    private String url1;
    private String url2;


    /**
     * Constructor de Asignatura
     * @param asig_ID id de la asignatura
     * @param abreviatura abreviatura del nombre de la asignatura (generalmente las iniciales de cada palabra)
     * @param nombreAsignatura nombre completo de la asignatura
     * @param docente1 profesor principal que imparte la asignatura
     * @param url1 una url opcional
     * @param url2 una url opcional
     */
    public Asignatura(String asig_ID, String abreviatura, String nombreAsignatura, String docente1, String url1, String url2) {
        this.asig_ID = Integer.parseInt(asig_ID);
        this.abreviatura = abreviatura;
        this.nombreAsignatura = nombreAsignatura;
        this.docente1 = docente1;
        this.url1 = url1;
        this.url2 = url2;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public int getAsig_ID() {
        return asig_ID;
    }

    public void setAsig_ID(int asig_ID) {
        this.asig_ID = asig_ID;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getNombreAsignatura() {
        return nombreAsignatura;
    }

    public void setNombreAsignatura(String nombreAsignatura) {
        this.nombreAsignatura = nombreAsignatura;
    }

    public String getDocente1() {
        return docente1;
    }

    public void setDocente1(String docente1) {
        this.docente1 = docente1;
    }

    @NonNull
    @Override
    public String toString() {
        return abreviatura + ": " + nombreAsignatura;
    }
}
