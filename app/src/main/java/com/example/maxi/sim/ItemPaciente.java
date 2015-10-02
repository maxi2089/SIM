package com.example.maxi.sim;

import android.widget.ImageView;

/**
 * Created by yamila on 04/09/2015.
 */
public class ItemPaciente {

    public int imagen;
    public String NombreyApellido;
    public Integer edad;
    public Double altura,peso;
    public Integer dni;
    public String Diagnostico;

    public ItemPaciente() {
    }

    public ItemPaciente(int imagen, String nombreyApellido, Integer edad, Double altura, Double peso, Integer dni,String Diag) {
        this.imagen = imagen;
        NombreyApellido = nombreyApellido;
        this.edad = edad;
        this.altura = altura;
        this.peso = peso;
        this.dni = dni;
        this.Diagnostico = Diag;
    }

    public String getDiagnostico() {
        return Diagnostico;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getNombreyApellido() {
        return NombreyApellido;
    }

    public void setNombreyApellido(String nombreyApellido) {
        NombreyApellido = nombreyApellido;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }
}
