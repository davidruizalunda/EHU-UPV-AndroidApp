package com.example.tfgprueba2;

import androidx.annotation.NonNull;

public class Clase {
    private String horaInicio;
    private String horaFin;
    private String aula;
    private String dia;
    private String asig_id;

    public Clase(String horaInicio, String horaFin, String aula, String dia, String asig_id) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.aula = aula;
        this.dia = dia;
        this.asig_id = asig_id;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getAsig_id() {
        return asig_id;
    }

    public void setAsig_id(String asig_id) {
        this.asig_id = asig_id;
    }

    @NonNull
    @Override
    public String toString() {
        return horaInicio + "-" + horaFin;
    }
}
