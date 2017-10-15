package com.contratediarista.br.contratediarista.retrofit.service;

import com.contratediarista.br.contratediarista.entity.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by manga on 13/10/2017.
 */

public interface UsuarioService {

    @GET("usuario/buscar-uid/{uid}")
    Call<Usuario> buscarPorUid(@Path("uid") String uid);

    @POST("usuario/buscar-contatos-prestador/{uid}")
    Call<List<Usuario>> buscarContatosPrestador(@Path("uid") String uid);

    @POST("usuario/buscar-contatos-contratante/{uid}")
    Call<List<Usuario>> buscarContatosContratante(@Path("uid") String uid);

}
