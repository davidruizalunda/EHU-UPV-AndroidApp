package com.example.tfgprueba2;
/**
 * Este es el objeto news.
 *
 * title: título de la noticia
 * link: link de acceso a la noticia completa
 * author: autor de la noticia
 * date: fecha de la noticia
 *
 * Data: 21/05/2021
 * @author David Ruiz Alunda
 * @version 1.0
 */
public class News {
    private String title;
    private String link;
    private String author;
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
