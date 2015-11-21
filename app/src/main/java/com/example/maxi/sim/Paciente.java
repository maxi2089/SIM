package com.example.maxi.sim;

import java.io.Serializable;

/**
 * Created by yamila on 26/09/2015.
 */
public class Paciente  extends CommonDto implements Serializable {
    private Integer idPaciente;
    private Integer dni;
    private String nombre;
    private String apellido;
    private Integer edad;
    private Double altura;
    private Double peso;
//    private LibroReport libroreport;
    //private String diagnostico;//FALTA AGREGAR SET Y GETTER Y EN TODOS LOS FRAGMENT QUE SE USA



    public Paciente() {
    }


    public Paciente(Integer idPaceinte, String nombre, String apellido, Integer dni, Integer edad, Double altura, Double peso) {
        this.idPaciente = idPaceinte;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.edad = edad;
        this.altura = altura;
        this.peso = peso;
        //this.diagnostico =  diagnostico;
    }

   /* public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
   */
    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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

}
