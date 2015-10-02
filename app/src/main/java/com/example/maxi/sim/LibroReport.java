package com.example.maxi.sim;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yamila on 26/09/2015.
 */
public class LibroReport implements java.io.Serializable {
    private Integer idLibroReport;
    private Date fechaAlta;
    private Date fechaBaja;
    private String estado;
    private Paciente paciente;
    private Set<MedicionDto> medicions = new HashSet<MedicionDto>(0);

/*
    private Integer idLibroReport;
    private Paciente paciente;
    private Date fechaAlta;
    private Date fechaBaja;
    private String estado;
    private Set<Medicion> medicions = new HashSet<Medicion>(0);
    private Set<Alerta> alertas = new HashSet<Alerta>(0);
    private Set<Administracionmedicamento> administracionmedicamentos = new HashSet<Administracionmedicamento>(0);
    private Set<Nota> notas = new HashSet<Nota>(0);
    private Set<Ecg> ecgs = new HashSet<Ecg>(0);
*/
    public LibroReport() {
    }


    public LibroReport(Integer idLibroReport, Date fechaAlta, Date fechaBaja, String estado, Paciente paciente) {

        this.idLibroReport=idLibroReport;
        this.fechaAlta = fechaAlta;
        this.fechaBaja = fechaBaja;
        this.estado = estado;
        this.paciente = paciente;

    }

    public LibroReport(Date fechaAlta, String estado) {
        this.fechaAlta = fechaAlta;
        this.estado = estado;
    }

    public Integer getIdLibroReport() {
        return idLibroReport;
    }

    public void setIdLibroReport(Integer idLibroReport) {
        this.idLibroReport = idLibroReport;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Set<MedicionDto> getMedicions() {
        return medicions;
    }

    public void setMedicions(Set<MedicionDto> medicions) {
        this.medicions = medicions;
    }
}
