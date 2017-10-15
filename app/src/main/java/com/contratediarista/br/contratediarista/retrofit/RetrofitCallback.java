package com.contratediarista.br.contratediarista.retrofit;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by manga on 15/10/2017.
 */

public class RetrofitCallback implements Callback {
    private ProgressDialog dialog;
    private String msgErro;
    private Context context;

    public RetrofitCallback(Context context, String msgAguarde, String msgErro) {
        this.context = context;
        this.msgErro = msgErro;
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage(msgAguarde);
        dialog.show();

    }

    @Override
    public void onResponse(Call call, Response response) {
        if (response.code() != javax.ws.rs.core.Response.Status.OK.getStatusCode()  &&
                response.code() != javax.ws.rs.core.Response.Status.NO_CONTENT.getStatusCode()) {
            Toast.makeText(context,msgErro,Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        Toast.makeText(context,msgErro,Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
