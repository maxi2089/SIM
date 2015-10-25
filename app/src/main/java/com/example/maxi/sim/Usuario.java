package com.example.maxi.sim;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class Usuario implements Serializable {
    private Integer idUsuario;
    private Rol rol;
    private int dni;
    private String nombre;
    private String usuario;
    private String password;
    private Set<Paciente> pacientes = new HashSet<>();

    public Usuario() {
    }

    public Usuario(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }

    public Usuario(Integer idUsuario, Rol rol, int dni, String nombre, String usuario, String password) {
        this.idUsuario = idUsuario;
        this.rol = rol;
        this.dni = dni;
        this.nombre = nombre;
        this.usuario = usuario;
        this.password = password;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
