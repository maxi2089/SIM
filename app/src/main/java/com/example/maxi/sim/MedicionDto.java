package com.example.maxi.sim;

import java.util.Date;

/**
 * Created by yamila on 27/09/2015.
 */
public class MedicionDto {

    private Integer idMedicion;
    private LibroReport libroreport;
    private Date fecha;
    private String descripcion;
    private String glucosa;
    private String dosis;
    private Float temperatura;
    private String freceunciaRespiratoria;
    private Float oxigenoEnSangre;
    private Float tensionArterial;

    public MedicionDto() {
    }


    public MedicionDto(Integer idMedicion, LibroReport libroreport, Date fecha) {
        this.idMedicion = idMedicion;
        this.libroreport = libroreport;
        this.fecha = fecha;
    }
    public MedicionDto(Integer idMedicion, LibroReport libroreport, Date fecha, String descripcion) {
        this.idMedicion = idMedicion;
        this.libroreport = libroreport;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }


    public Integer getIdMedicion() {
        return this.idMedicion;
    }

    public void setIdMedicion(Integer idMedicion) {
        this.idMedicion = idMedicion;
    }

    public LibroReport getLibroreport() {
        return this.libroreport;
    }

    public void setLibroreport(LibroReport libroreport) {
        this.libroreport = libroreport;
    }


    public Date getFecha() {
        return this.fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the glucosa
     */
    public String getGlucosa() {
        return glucosa;
    }

    /**
     * @param glucosa the glucosa to set
     */
    public void setGlucosa(String glucosa) {
        this.glucosa = glucosa;
    }

    /**
     * @return the dosis
     */
    public String getDosis() {
        return dosis;
    }

    /**
     * @param dosis the dosis to set
     */
    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    /**
     * @return the temperatura
     */
    public Float getTemperatura() {
        return temperatura;
    }

    /**
     * @param temperatura the temperatura to set
     */
    public void setTemperatura(Float temperatura) {
        this.temperatura = temperatura;
    }

    /**
     * @return the freceunciaRespiratoria
     */
    public String getFreceunciaRespiratoria() {
        return freceunciaRespiratoria;
    }

    /**
     * @param freceunciaRespiratoria the freceunciaRespiratoria to set
     */
    public void setFreceunciaRespiratoria(String freceunciaRespiratoria) {
        this.freceunciaRespiratoria = freceunciaRespiratoria;
    }

    /**
     * @return the oxigenoEnSangre
     */
    public Float getOxigenoEnSangre() {
        return oxigenoEnSangre;
    }

    /**
     * @param oxigenoEnSangre the oxigenoEnSangre to set
     */
    public void setOxigenoEnSangre(Float oxigenoEnSangre) {
        this.oxigenoEnSangre = oxigenoEnSangre;
    }

    /**
     * @return the tensionArterial
     */
    public Float getTensionArterial() {
        return tensionArterial;
    }

    /**
     * @param tensionArterial the tensionArterial to set
     */
    public void setTensionArterial(Float tensionArterial) {
        this.tensionArterial = tensionArterial;
    }
}
