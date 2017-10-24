package com.contratediarista.br.contratediarista.retrofit;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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
                OkHttpClient cliente = new OkHttpClient.Builder()
                        .connectTimeout(1, TimeUnit.MINUTES)
                        .readTimeout(1,TimeUnit.MINUTES)
                        .writeTimeout(1,TimeUnit.MINUTES).build();
                retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.1.12:8080/ContrateDiarista/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(cliente)
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
