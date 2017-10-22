package com.contratediarista.br.contratediarista.retrofit.service;

import com.contratediarista.br.contratediarista.entity.Rotina;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by manga on 21/10/2017.
 */

public interface RotinaService  {

    @POST("rotina/candidatar-vaga/{uidUsuario}/{idRotina}")
    Call<Void> candidatarVaga(@Path("uidUsuario") String uidUsuario,@Path("idRotina") int idRotina);

    @POST("rotina/buscar-vagas-usuario/{uid}/{dataInicial}/{dataFinal}")
    Call<List<Rotina>> buscarVagasUsuarioPrestador(@Path("uid") String uid, @Path("dataInicial") String dataInicial, @Path("dataFinal") String dataFinal);

    @POST("rotina/buscar-rotina-por-usuario-data/{uid}/{dataInicial}/{dataFinal}")
    Call <List<Rotina>> buscarRotinaPorUsuarioEData(@Path("uid") String uid,@Path("dataInicial") String dataInicial,@Path("dataFinal") String dataFinal);

    @POST("rotina/excluir-rotina/{id}")
    Call<Void> excluirRotina(@Path("id") int id);

    @POST("rotina/remover-prestador-selecionado/{idRotina}")
    Call<Void> removerPrestadorSelecionado(@Path("idRotina") int idRotina);

    @POST("rotina/alterar-prestador-selecionado/{idRotina}/{uidUsuario}")
    Call<Void> alterarPrestadorSelecionado(@Path("idRotina") int idRotina,@Path("uidUsuario") String uidUsuario);

    @POST("rotina/remover-lista-prestador/{idRotina}/{uidUsuario}")
    Call<Void> removerListarPrestador(@Path("idRotina") int idRotina,@Path("uidUsuario") String uidUsuario);
}
