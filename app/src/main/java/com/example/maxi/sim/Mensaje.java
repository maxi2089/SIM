package com.example.maxi.sim;

/**
 * Created by yamila on 26/09/2015.
 */
public class Mensaje {

    private Integer idMensaje;
    private Alerta alerta;
    private Usuario usuarioByIdUsuarioRemitente;
    private Usuario usuarioByIdUsuaruiDestinatario;
    private String texto;

    public Mensaje(Integer idMensaje, Alerta alerta, Usuario usuarioByIdUsuarioRemitente, Usuario usuarioByIdUsuaruiDestinatario, String texto) {
        this.idMensaje = idMensaje;
        this.alerta = alerta;
        this.usuarioByIdUsuarioRemitente = usuarioByIdUsuarioRemitente;
        this.usuarioByIdUsuaruiDestinatario = usuarioByIdUsuaruiDestinatario;
        this.texto = texto;
    }

    public Integer getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(Integer idMensaje) {
        this.idMensaje = idMensaje;
    }

    public Alerta getAlerta() {
        return alerta;
    }

    public void setAlerta(Alerta alerta) {
        this.alerta = alerta;
    }

    public Usuario getUsuarioByIdUsuarioRemitente() {
        return usuarioByIdUsuarioRemitente;
    }

    public void setUsuarioByIdUsuarioRemitente(Usuario usuarioByIdUsuarioRemitente) {
        this.usuarioByIdUsuarioRemitente = usuarioByIdUsuarioRemitente;
    }

    public Usuario getUsuarioByIdUsuaruiDestinatario() {
        return usuarioByIdUsuaruiDestinatario;
    }

    public void setUsuarioByIdUsuaruiDestinatario(Usuario usuarioByIdUsuaruiDestinatario) {
        this.usuarioByIdUsuaruiDestinatario = usuarioByIdUsuaruiDestinatario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
