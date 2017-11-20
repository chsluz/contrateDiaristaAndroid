package com.contratediarista.br.contratediarista.enuns;

import android.view.textservice.TextInfo;

/**
 * Created by manga on 13/10/2017.
 */

public enum TipoUsuario {
    ADMINISTRADOR(
            "Administrador"),
    MODERADOR(
            "Moderador"),
    PRESTADOR(
            "Prestador"),
    CONTRATANTE(
            "Contratante");

    private final String descricao;

    private TipoUsuario(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Obtem o valor de descricao.
     *
     * @return O valor de descricao.
     */
    public String getDescricao() {
        return descricao;
    }

    public static TipoUsuario getTipoUsuario(String descricao) {
        if(TipoUsuario.CONTRATANTE.descricao.equals(descricao)) {
            return TipoUsuario.CONTRATANTE;
        }
        else if(TipoUsuario.PRESTADOR.descricao.equals(descricao)) {
            return  TipoUsuario.PRESTADOR;
        }
        else if(TipoUsuario.MODERADOR.descricao.equals(descricao)) {
            return  TipoUsuario.MODERADOR;
        }
        else {
            return  null;
        }
    }
}
