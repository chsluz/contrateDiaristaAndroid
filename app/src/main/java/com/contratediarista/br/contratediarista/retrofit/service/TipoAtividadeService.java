package com.contratediarista.br.contratediarista.retrofit.service;

import com.contratediarista.br.contratediarista.entity.TipoAtividade;
import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by manga on 20/10/2017.
 */

public interface TipoAtividadeService {

    @GET("tipo-atividade/listar-todos")
    Call<JsonArray> listarTodos();

    @POST("tipo-atividade/cadastrar-tipo-atividade/{descricao}")
    Call<Void> cadastrarTipoAtividade(@Path("descricao") String descricao);

    @POST("tipo-atividade/excluir-tipo-atividade/{id}")
    Call<Void> excluirTipoAtividade(@Path("id") int id);
}
