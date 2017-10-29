package com.zeus.agenda.Utils;

import android.graphics.Bitmap;

/**
 * Created by Juan on 29/10/2017.
 */

public class Contacto {
    private Bitmap bitmap;
    private String nombre;
    private String telefono;
    private String mail;
    private String id;

    public Contacto(Bitmap bitmap, String nombre, String telefono, String mail, String id) {
        this.bitmap = bitmap;
        this.nombre = nombre;
        this.telefono = telefono;
        this.mail = mail;
        this.id = id;
    }

    public Contacto() {
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
