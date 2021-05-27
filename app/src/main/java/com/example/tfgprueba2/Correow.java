package com.example.tfgprueba2;
/**
 * Esta es el objeto Correow.
 *
 * from:  Remitente del mail
 * subject: Asunto del mail
 * date: Fecha del mail
 * contents: Cuerpo del mail (contenido).
 *
 * Data: 21/05/2021
 * @author David Ruiz Alunda
 * @version 1.0
 */
public class Correow {
    private String from;
    private String subject;
    private String date;
    private String contents;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
