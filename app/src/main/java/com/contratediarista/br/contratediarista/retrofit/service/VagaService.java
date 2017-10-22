package com.contratediarista.br.contratediarista.retrofit.service;

import com.contratediarista.br.contratediarista.entity.Rotina;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by manga on 15/10/2017.
 */

public interface VagaService {

    @POST("vaga/cadastrar-vaga")
    Call<Void> cadastrarVaga(@Body RequestBody json);

    @POST("vaga/buscar-vagas-disponiveis")
    Call<List<Rotina>>buscarVagasDisponiveisPorDataValor(@Body RequestBody json);
}
