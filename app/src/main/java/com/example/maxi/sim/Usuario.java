package com.example.maxi.sim;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;


public class Usuario {
    private String user;
    private String password;
    private String rol;

    public Usuario() {}
    public Usuario(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {

        return user;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void parserJsonUsuario( String usuario) throws JSONException {

        JSONObject objeto = new JSONObject(usuario);

        user = objeto.getString("DNI");
        System.out.println("Usuario "+user);
        rol = objeto.getString("Rol");
        System.out.println("Rol "+rol);
        password = objeto.getString("PASSWORD");


    }
}
