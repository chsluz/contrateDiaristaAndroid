package com.contratediarista.br.contratediarista.entity;

import java.io.Serializable;

import com.google.gson.JsonObject;

public class TipoAtividade implements Serializable{

	private static final long serialVersionUID = 1L;

	private int id;

	private String descricao;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
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
		TipoAtividade other = (TipoAtividade) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		return true;
	}

	public static TipoAtividade toTipoAtividadeGson(JsonObject jsonObject) {
		TipoAtividade tipo = new TipoAtividade();
		if(jsonObject.get("id") != null) {
			tipo.setId(jsonObject.get("id").getAsInt());
		}
		if(jsonObject.get("descricao") != null) {
			tipo.setDescricao(jsonObject.get("descricao").getAsString());
		}
		return tipo;
	}
	
	
	
	

}
