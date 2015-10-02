package com.example.maxi.sim;

import java.util.Date;

/**
 * Created by yamila on 26/09/2015.
 */
public class Nota {
    private Integer idNota;
    private LibroReport libroreport;
    private Date fecha;
    private String texto;
    private String adjunto;

    public Nota(Integer idNota, LibroReport libroreport, Date fecha, String texto, String adjunto) {
        this.idNota = idNota;
        this.libroreport = libroreport;
        this.fecha = fecha;
        this.texto = texto;
        this.adjunto = adjunto;
    }

    public Integer getIdNota() {
        return idNota;
    }

    public void setIdNota(Integer idNota) {
        this.idNota = idNota;
    }

    public LibroReport getLibroreport() {
        return libroreport;
    }

    public void setLibroreport(LibroReport libroreport) {
        this.libroreport = libroreport;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }
}
