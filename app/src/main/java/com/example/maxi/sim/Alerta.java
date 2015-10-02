package com.example.maxi.sim;

import java.util.Date;

/**
 * Created by yamila on 26/09/2015.
 */
public class Alerta {

    private Integer idAlerta;
    private LibroReport libroreport;
    private String criticidad;
    private Date fecha;
    private Mensaje mensaje;

    public Alerta(Integer idAlerta, LibroReport libroreport, String criticidad, Date fecha, Mensaje mensaje) {
        this.idAlerta = idAlerta;
        this.libroreport = libroreport;
        this.criticidad = criticidad;
        this.fecha = fecha;
        this.mensaje = mensaje;
    }

    public Integer getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Integer idAlerta) {
        this.idAlerta = idAlerta;
    }

    public LibroReport getLibroreport() {
        return libroreport;
    }

    public void setLibroreport(LibroReport libroreport) {
        this.libroreport = libroreport;
    }

    public String getCriticidad() {
        return criticidad;
    }

    public void setCriticidad(String criticidad) {
        this.criticidad = criticidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    public void setMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
    }
}
