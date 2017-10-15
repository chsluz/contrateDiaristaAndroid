package com.contratediarista.br.contratediarista.entity;

/**
 * Created by manga on 14/10/2017.
 */

public class Mensagem {

    private String idUsuario;
    private String mensagem;
    
    public Mensagem() {

    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
