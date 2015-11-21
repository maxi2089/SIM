package com.example.maxi.sim;

import java.sql.Time;
import java.util.Date;

/**
 * Created by yamila on 04/11/2015.
 */
public class Visita  extends CommonDto{

    private String   fechaVisita;
    private String Observacion;


    public Visita(String fechaVisita, String observacion) {
        this.fechaVisita = fechaVisita;
        Observacion = observacion;
    }

    public String getFechaVisita() {
        return fechaVisita;
    }

    public void setFechaVisita(String fechaVisita) {
        this.fechaVisita = fechaVisita;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String observacion) {
        Observacion = observacion;
    }
}
