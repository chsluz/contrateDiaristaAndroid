package com.contratediarista.br.contratediarista.entity;

import java.io.Serializable;

/**
 * Created by manga on 13/10/2017.
 */

public class Cidade implements Serializable{

    private int id;
    private String nome;
    private Estado estado;

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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cidade cidade = (Cidade) o;

        return id == cidade.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
