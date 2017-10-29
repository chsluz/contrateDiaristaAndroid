package com.contratediarista.br.contratediarista.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.VagaAdapter;
import com.contratediarista.br.contratediarista.entity.Rotina;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.VagaService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class VagasUi extends AppCompatActivity {

    private List<Rotina> rotinas;
    private ListView lvVagas;
    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vagas_ui);
        rotinas = new ArrayList<>();

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            lvVagas = (ListView) findViewById(R.id.lv_vagas);
            json = extra.get("jsonObject").toString();
            RequestBody body = RequestBody.create(MediaType.parse("json"),json);
            Call call = new RetrofitInicializador().getRetrofit().create(VagaService.class).buscarVagasDisponiveisPorDataValor(body);
            RetrofitCallback callback = new RetrofitCallback(VagasUi.this, getString(R.string.buscando_vagas), getString(R.string.erro_buscar_vagas)) {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.code() == 200) {
                        rotinas = (List<Rotina>) response.body();
                        carregarListaVagas();
                    }

                    super.onResponse(call, response);

                    if(response.code() == 204) {
                        onBackPressed();
                        Toast.makeText(VagasUi.this,getString(R.string.nenhum_resultado_encontrado),Toast.LENGTH_SHORT).show();
                    }
                    else if(response.code() != 200 && response.code() != 204) {
                        onBackPressed();
                        Toast.makeText(VagasUi.this,getString(R.string.erro_ao_pesquisar),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    super.onFailure(call, t);
                    onBackPressed();
                    Toast.makeText(VagasUi.this,getString(R.string.erro_ao_pesquisar),Toast.LENGTH_SHORT).show();
                }
            };
            call.enqueue(callback);


            lvVagas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Rotina rotina = rotinas.get(position);
                    Gson gson = new Gson();
                    String json = gson.toJson(rotina);
                    Intent intent = new Intent(VagasUi.this,VisualizacaoVagaUi.class);
                    intent.putExtra("rotina",json);
                    startActivity(intent);
                }
            });
        }
    }

    public void carregarListaVagas() {
        VagaAdapter adapter = new VagaAdapter(this,rotinas);
        lvVagas.setAdapter(adapter);
    }

}
