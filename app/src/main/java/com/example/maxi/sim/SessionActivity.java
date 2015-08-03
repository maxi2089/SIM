package com.example.maxi.sim;

import java.util.Date;

public class SessionActivity {
    private int sessionID;
    private UserActivity user;
    private Date   fechaConexion;

    public Date getFechaConexion(){
        return fechaConexion;
    }

    public void setFechaConexion(Date fechaConexion) {
        this.fechaConexion = fechaConexion;
    }

    public SessionActivity(int sessionID, UserActivity user,Date fechaConexion) {

        this.sessionID = sessionID;
        this.user = user;
        this.fechaConexion = fechaConexion;

    }

    public int getSessionID(){
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public UserActivity getUser() {
        return user;
    }

    public void setUser(UserActivity user) {
        this.user = user;
    }


}
