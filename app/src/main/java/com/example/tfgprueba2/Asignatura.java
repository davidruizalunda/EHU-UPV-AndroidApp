package com.example.tfgprueba2;

import androidx.annotation.NonNull;

public class Asignatura {
    private int asig_ID;
    private String abreviatura;
    private String nombreAsignatura;
    private String docente1;

    public Asignatura(String abreviatura, String nombreAsignatura, String docente1, String asig_ID) {
        this.abreviatura = abreviatura;
        this.nombreAsignatura = nombreAsignatura;
        this.docente1 = docente1;
        this.asig_ID = Integer.parseInt(asig_ID);
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
        return nombreAsignatura;
    }
}
