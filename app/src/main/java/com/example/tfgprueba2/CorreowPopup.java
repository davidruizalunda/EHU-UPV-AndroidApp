package com.example.tfgprueba2;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.os.Bundle;
/*
Tipo de servidor: IMAP o POP  (Mejor utiliza IMAP)
Nombre de servidor: ikasle.ehu.eus
Puerto: imap 993, pop 995
Protocolo SSL
Usuario: Tu login LDAP
Contraseña: Tu contraseña
*/
public class CorreowPopup extends AppCompatActivity {
    private TextView from, subject, date, content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.correows_popup);
        from = findViewById(R.id.from_popup);
        subject = findViewById(R.id.subject_popup);
        date = findViewById(R.id.date_popup);
        content = findViewById(R.id.content_popup);
    }

    public void setPopup(String fromS, String subjectS, String dateS, String contentS){

        this.from.setText("hola");
        this.date.setText("hola");
        this.subject.setText("hola");
        this.content.setText("hola");
    }


}