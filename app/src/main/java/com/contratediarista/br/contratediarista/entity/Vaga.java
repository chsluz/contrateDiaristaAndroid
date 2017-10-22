package com.contratediarista.br.contratediarista.entity;

import com.contratediarista.br.contratediarista.enuns.TipoPeriodo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Vaga implements Serializable{

	private int id;
	
	private Date dataCadastrada;
	
	private Usuario contratante;
	
	private Endereco endereco;
	
	private TipoPeriodo tipoPeriodo;
	
	private Integer valorPeriodo;
	
	private List<TipoAtividade> tiposAtividade;

	private List<Rotina> rotinas;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDataCadastrada() {
		return dataCadastrada;
	}

	public void setDataCadastrada(Date dataCadastrada) {
		this.dataCadastrada = dataCadastrada;
	}

	public Usuario getContratante() {
		return contratante;
	}

	public void setContratante(Usuario contratante) {
		this.contratante = contratante;
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

	public List<TipoAtividade> getTiposAtividade() {
		return tiposAtividade;
	}

	public void setTiposAtividade(List<TipoAtividade> tiposAtividade) {
		this.tiposAtividade = tiposAtividade;
	}

	public List<Rotina> getRotinas() {
		return rotinas;
	}

	public void setRotinas(List<Rotina> rotinas) {
		this.rotinas = rotinas;
	}
	
	public Integer getValorPeriodo() {
		return valorPeriodo;
	}

	public void setValorPeriodo(Integer valorPeriodo) {
		this.valorPeriodo = valorPeriodo;
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
		Vaga other = (Vaga) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
