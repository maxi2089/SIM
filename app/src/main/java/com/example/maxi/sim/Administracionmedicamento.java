package com.example.maxi.sim;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yamila on 26/09/2015.
 */
public class Administracionmedicamento implements Serializable {


    private Integer idAdminMedic;
    private LibroReport libroreport;
    private Date fecha;
    private String dosis;
    private String medicamento;
    private String administracion;

    public Administracionmedicamento(Integer idAdminMedic, LibroReport libroreport, Date fecha, String dosis, String medicamento, String administracion) {
        this.idAdminMedic = idAdminMedic;
        this.libroreport = libroreport;
        this.fecha = fecha;
        this.dosis = dosis;
        this.medicamento = medicamento;
        this.administracion = administracion;
    }

    public Integer getIdAdminMedic() {
        return idAdminMedic;
    }

    public void setIdAdminMedic(Integer idAdminMedic) {
        this.idAdminMedic = idAdminMedic;
    }

    public LibroReport getLibroreport() {
        return libroreport;
    }

    public void setLibroreport(LibroReport libroreport) {
        this.libroreport = libroreport;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getAdministracion() {
        return administracion;
    }

    public void setAdministracion(String administracion) {
        this.administracion = administracion;
    }
}
