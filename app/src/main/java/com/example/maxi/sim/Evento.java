package com.example.maxi.sim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by yamila on 25/09/2015.
 */
public class Evento {
    private String fecha;
    private String registro;
    private String responsable;

    public Evento(){}
    public Evento(String fecha, String registro, String responsable) {
        this.fecha = fecha;
        this.registro = registro;
        this.responsable = responsable;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
    public void parserJsonEventoTemperatura(JSONObject objeto, String fecha, String registro,String descripcion) throws JSONException {

        System.out.println("En parsert wuachoo!!");
//       JSONObject objeto2 = medicion.getJSONObject(0);

        fecha = objeto.getString(fecha) ;
        registro =objeto.getString(registro) ;
        responsable = objeto.getString(descripcion);

    }
}
