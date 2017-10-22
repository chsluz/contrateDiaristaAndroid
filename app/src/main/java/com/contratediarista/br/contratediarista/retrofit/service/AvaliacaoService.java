package com.contratediarista.br.contratediarista.retrofit.service;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by manga on 21/10/2017.
 */

public interface AvaliacaoService {

    @POST("avaliacao/avaliar-usuario/{uidAvaliador}/{uidAvaliado}/{nota}/{data}/{observacao}")
    Call<Void> avaliarUsuario(@Path("uidAvaliador") String uidAvaliador,@Path("uidAvaliado") String uidAvaliado,@Path("nota") int nota,@Path("data") String data,@Path("observacao") String observacao);
}
