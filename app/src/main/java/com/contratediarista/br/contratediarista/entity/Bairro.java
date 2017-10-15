package com.contratediarista.br.contratediarista.entity;

/**
 * Created by manga on 13/10/2017.
 */

public class Bairro {
    private int id;
    private String descricao;
    private Cidade cidade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bairro bairro = (Bairro) o;

        return id == bairro.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
