package com.contratediarista.br.contratediarista.entity;

import com.contratediarista.br.contratediarista.enuns.DiasSemana;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Rotina implements Serializable {

	private int id;

	private Date data;

	private DiasSemana diaSemana;

	private Vaga vaga;

	private List<Usuario> prestadores;

	private Usuario prestadorSelecionado;

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

	public DiasSemana getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiasSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	public Vaga getVaga() {
		return vaga;
	}

	public void setVaga(Vaga vaga) {
		this.vaga = vaga;
	}

	public List<Usuario> getPrestadores() {
		return prestadores;
	}

	public void setPrestadores(List<Usuario> prestadores) {
		this.prestadores = prestadores;
	}

	public Usuario getPrestadorSelecionado() {
		return prestadorSelecionado;
	}

	public void setPrestadorSelecionado(Usuario prestadorSelecionado) {
		this.prestadorSelecionado = prestadorSelecionado;
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
		Rotina other = (Rotina) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
