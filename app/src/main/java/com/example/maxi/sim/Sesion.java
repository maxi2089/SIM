package com.example.maxi.sim;

import java.util.Date;

public class Sesion {
    private int sessionID;
    private Usuario user;
    private Date   fechaConexion;

    private static Sesion instance;


    private Sesion(int sessionID, Usuario user, Date fechaConexion) {

        this.sessionID = sessionID;
        this.user = user;
        this.fechaConexion = fechaConexion;

    }


    public static synchronized Sesion getInstance(int sessionID, Usuario user, Date fechaConexion) {
        if (instance == null) {
            instance = new Sesion(sessionID,user,fechaConexion);
        }
        return instance;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public Date getFechaConexion() {
        return fechaConexion;
    }

    public void setFechaConexion(Date fechaConexion) {
        this.fechaConexion = fechaConexion;
    }

    public static Sesion getInstance() {
        return instance;
    }

    public static void setInstance(Sesion instance) {
        Sesion.instance = instance;
    }
}
