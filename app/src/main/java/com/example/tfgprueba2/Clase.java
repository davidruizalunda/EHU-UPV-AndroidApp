package com.example.tfgprueba2;

import androidx.annotation.NonNull;
/**
 * Este es el objeto Clase.
 * Una clase, en este contexto, es un margen de tiempo en el que se imparte una materia.
 *
 * Data: 21/05/2021
 * @author David Ruiz Alunda
 * @version 1.0
 */
public class Clase {
    private String horaInicio;
    private String horaFin;
    private String aula;
    private String dia;
    private String asig_id;

    /**
     * Constructor de Clase
     * @param horaInicio  La hora a la que empieza la clase.
     * @param horaFin La hora a la que acaba la clase.
     * @param aula Nombre del Aula o laboratorio en el que se realiza la clase.
     * @param dia  Día en el que se imparte la clase.
     * @param asig_id  id de la asignatura que se imparte en esa clase.
     */
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
