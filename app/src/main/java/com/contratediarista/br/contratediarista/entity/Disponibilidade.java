package com.contratediarista.br.contratediarista.entity;

import com.contratediarista.br.contratediarista.enuns.TipoPeriodo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Disponibilidade implements Serializable {

	private int id;
	private Date data;
	private Usuario prestador;
	private Endereco endereco;
	private TipoPeriodo tipoPeriodo;
	private Integer valorPeriodo;
	private List<TipoAtividade> tiposAtividade;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Usuario getPrestador() {
		return prestador;
	}

	public void setPrestador(Usuario prestador) {
		this.prestador = prestador;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public TipoPeriodo getTipoPeriodo() {
		return tipoPeriodo;
	}

	public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}

	public Integer getValorPeriodo() {
		return valorPeriodo;
	}

	public void setValorPeriodo(Integer valorPeriodo) {
		this.valorPeriodo = valorPeriodo;
	}

	public List<TipoAtividade> getTiposAtividade() {
		return tiposAtividade;
	}

	public void setTiposAtividade(List<TipoAtividade> tiposAtividade) {
		this.tiposAtividade = tiposAtividade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Disponibilidade other = (Disponibilidade) obj;
		if (id != other.id)
			return false;
		return true;
	}
	

}
