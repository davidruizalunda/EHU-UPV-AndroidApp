package com.example.tfgprueba2;

import androidx.annotation.NonNull;

public class Asignatura {
    private String abreviatura;
    private String nombreAsignatura;
    private String docente1;

    public Asignatura(String abreviatura, String nombreAsignatura, String docente1) {
        this.abreviatura = abreviatura;
        this.nombreAsignatura = nombreAsignatura;
        this.docente1 = docente1;
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
