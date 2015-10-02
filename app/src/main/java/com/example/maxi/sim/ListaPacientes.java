package com.example.maxi.sim;

import java.util.ArrayList;
import java.util.prefs.PreferenceChangeEvent;

/**
 * Created by yamila on 01/10/2015.
 */
public class ListaPacientes {

    private ArrayList<Paciente> lista;

    private static ListaPacientes instance;

    private ListaPacientes(){
        lista = new ArrayList<Paciente>();
    }

    public void setLista( Paciente Paciente){
        this.lista.add(Paciente);
    }

    public ArrayList<Paciente> getLista(){
        return this.lista;
    }

    public static synchronized ListaPacientes getInstance() {
        if (instance == null) {
            instance = new ListaPacientes();
        }
        return instance;
    }

}
