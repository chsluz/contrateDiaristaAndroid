package com.contratediarista.br.contratediarista.retrofit.service;

import com.contratediarista.br.contratediarista.entity.Disponibilidade;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by manga on 20/10/2017.
 */

public interface DisponibilidadeService {

    @POST("disponibilidade/cadastrar-disponibilidade/{uid}")
    Call<Void> cadastrarDisponibilidade(@Path("uid") String uid, @Body RequestBody json);

    @POST("disponibilidade/buscar-por-usuario-data/{uid}/{dataInicial}/{dataFinal}")
    Call<List<Disponibilidade>> buscarPorUsuarioEData(@Path("uid") String uid,@Path("dataInicial") String dataInicial,@Path("dataFinal") String dataFinal);

    @POST("disponibilidade/excluir-diponibilidade/{id}")
    Call<Void> excluirDisponibilidade(@Path("id") int id);

    @POST("disponibilidade/buscar-disponibilidades")
    Call<List<Disponibilidade>> buscarDisponibilidadesPrestador(@Body RequestBody json);

    @POST("disponibilidade/contratar-usuario/{idDisponibilidade}/{uidUsuario}")
    Call<Void> contratarUsuario(@Path("idDisponibilidade") int idDisponibilidade,@Path("uidUsuario") String uidUsuario);


}
