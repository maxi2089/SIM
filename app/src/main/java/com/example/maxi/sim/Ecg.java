package com.example.maxi.sim;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yamila on 26/09/2015.
 */
public class Ecg implements Serializable {
    private Integer idEcg;
    private LibroReport libroreport;
    private String estado;
    private Date fecha;
    private String captura;
    private Float ppm;

    public Ecg() {
    }


    public Ecg(LibroReport libroreport, String estado, Date fecha, Float ppm) {
        this.libroreport = libroreport;
        this.estado = estado;
        this.fecha = fecha;
        this.ppm = ppm;
    }
    public Ecg(LibroReport libroreport, String estado, Date fecha, String captura, Float ppm) {
        this.libroreport = libroreport;
        this.estado = estado;
        this.fecha = fecha;
        this.captura = captura;
        this.ppm = ppm;
    }

    public Integer getIdEcg() {
        return idEcg;
    }

    public void setIdEcg(Integer idEcg) {
        this.idEcg = idEcg;
    }

    public LibroReport getLibroreport() {
        return libroreport;
    }

    public void setLibroreport(LibroReport libroreport) {
        this.libroreport = libroreport;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCaptura() {
        return captura;
    }

    public void setCaptura(String captura) {
        this.captura = captura;
    }

    public Float getPpm() {
        return ppm;
    }

    public void setPpm(Float ppm) {
        this.ppm = ppm;
    }
}
