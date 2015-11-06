package com.example.maxi.sim;

import java.sql.Time;
import java.util.Date;

/**
 * Created by yamila on 04/11/2015.
 */
public class Visita {

    private Date   fechaVisita;
    private String Observacion;


    public Visita(Date fechaVisita, String observacion) {
        this.fechaVisita = fechaVisita;
        Observacion = observacion;
    }

    public Date getFechaVisita() {
        return fechaVisita;
    }

    public void setFechaVisita(Date fechaVisita) {
        this.fechaVisita = fechaVisita;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String observacion) {
        Observacion = observacion;
    }
}
