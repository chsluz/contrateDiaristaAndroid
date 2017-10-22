package com.contratediarista.br.contratediarista.entity;

import com.contratediarista.br.contratediarista.enuns.TipoUsuario;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by manga on 13/10/2017.
 */

public class Usuario implements Serializable{
    private String uid;
    private String nome;
    private String cpf;
    private TipoUsuario tipoUsuario;
    private Endereco endereco;
    private Date dataNascimento;
    private String telefone;
    private List<Avaliacao> avaliacoes;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;

        return uid.equals(usuario.uid);

    }

    public Float getMediaAprovacaoUsuario() {
        if (avaliacoes != null && !avaliacoes.isEmpty()) {
            int av = 0;
            for (Avaliacao avaliacao : avaliacoes) {
                av = av + avaliacao.getNota();
            }
            return (float) (av / avaliacoes.size());
        } else {
            return (float) 0;
        }
    }

    public int getQuantidadeAvaliacoesUsuario() {
        return (avaliacoes != null && !avaliacoes.isEmpty()) ? avaliacoes.size() : 0;
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}
