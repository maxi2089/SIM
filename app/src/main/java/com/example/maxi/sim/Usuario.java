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


    public Usuario() {
    }

    public Usuario(String user, String password, String rol) {
        this.user = user;
        this.password = password;
        this.rol = rol;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
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
