package com.example.tfgprueba2;

import androidx.annotation.NonNull;
/**
 * Este es el objeto Tutoria.
 *
 * Data: 21/05/2021
 * @author David Ruiz Alunda
 * @version 1.0
 */
public class Tutoria {
    private String horaInicio;
    private String horaFin;
    private String profesor;
    private String dia;

    /**
     * Constructora de Tutoria
     *
     * @param horaInicio hora de comienzo de las tutorías de un profesor
     * @param horaFin hora de fin de las tutorías de un profesor
     * @param profesor correo del profesor
     * @param dia dia de este periodo de tutorías
     */
    public Tutoria(String horaInicio, String horaFin, String profesor, String dia) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.profesor = profesor;
        this.dia = dia;
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

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    @NonNull
    @Override
    public String toString() {
        return horaInicio + "-" + horaFin + " ";
    }
}
