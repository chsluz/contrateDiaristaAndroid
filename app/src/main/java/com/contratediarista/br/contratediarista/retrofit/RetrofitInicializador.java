package com.contratediarista.br.contratediarista.retrofit;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by manga on 13/10/2017.
 */

public class RetrofitInicializador {
    private Retrofit retrofit;

    public RetrofitInicializador() {
        try {
            if(retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.25.210:8080/ContrateDiarista/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }catch (Exception e) {
            Log.e("erro", "RetrofitInicializador: ", e.getCause());
        }

    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
