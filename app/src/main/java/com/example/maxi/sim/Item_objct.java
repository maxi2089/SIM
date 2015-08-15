package com.example.maxi.sim;

/**
 * Created by yamila on 09/08/2015.
 */
public class Item_objct {
    private String titulo;
    private int icono;

    public Item_objct(String titulo, int icono) {
        this.titulo = titulo;
        this.icono = icono;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }
}
