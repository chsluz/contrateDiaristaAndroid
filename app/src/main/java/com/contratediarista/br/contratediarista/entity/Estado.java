package com.contratediarista.br.contratediarista.entity;

import java.io.Serializable;

/**
 * Created by manga on 13/10/2017.
 */

public class Estado implements Serializable {
    private int id;
    private String nome;
    private String sigla;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Estado estado = (Estado) o;

        return id == estado.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
