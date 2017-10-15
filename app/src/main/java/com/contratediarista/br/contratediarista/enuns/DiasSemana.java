package com.contratediarista.br.contratediarista.enuns;

import com.google.gson.JsonObject;

public enum DiasSemana {
	DOMINGO("Domingo"), SEGUNDA("Segunda"), TERCA("Terça"), QUARTA("Quarta"), QUINTA("Quinta"), SEXTA("Sexta"), SABADO(
			"Sábado");

	private final String descricao;

	private DiasSemana(String descricao) {
		this.descricao = descricao;
	}

	public static DiasSemana getValor(int ordem) {
		if (7 == ordem) {
			return DiasSemana.DOMINGO;
		}
		if (SEGUNDA.ordinal() == ordem) {
			return DiasSemana.SEGUNDA;
		}
		if (TERCA.ordinal() == ordem) {
			return DiasSemana.TERCA;
		}
		if (QUARTA.ordinal() == ordem) {
			return DiasSemana.QUARTA;
		}
		if (QUINTA.ordinal() == ordem) {
			return DiasSemana.QUINTA;
		}
		if (SEXTA.ordinal() == ordem) {
			return DiasSemana.SEXTA;
		}
		if (SABADO.ordinal() == ordem) {
			return DiasSemana.SABADO;
		}
		return null;
	}

	/**
	 * Obtem o valor de descricao.
	 *
	 * @return O valor de descricao.
	 */
	public String getDescricao() {
		return descricao;
	}

	public static DiasSemana toDiaSemanaJson(JsonObject jsonObject) {
		if(jsonObject.get("diaSemana") != null) {
			return getValor(jsonObject.get("diaSemana").getAsInt());
		}
		return null;
	}
}
