package com.example.maxi.sim;

import java.io.Serializable;

/**
 * Created by yamila on 16/10/2015.
 */
public class Rol implements Serializable {

    private Integer idRol;
    private String nombreRol;


    public Rol() {
    }


    public Rol(int idRol) {
        this.idRol = idRol;
    }


    public Integer getIdRol() {
        return this.idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }


    public String getNombreRol() {
        return this.nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }
}
