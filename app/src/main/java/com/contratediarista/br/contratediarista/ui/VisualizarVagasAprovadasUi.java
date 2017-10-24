package com.contratediarista.br.contratediarista.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.VagaAdapter;
import com.contratediarista.br.contratediarista.entity.Rotina;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.RotinaService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class VisualizarVagasAprovadasUi extends AppCompatActivity {
    SimpleDateFormat formatJson = new SimpleDateFormat("yyyy-MM-dd");
    private ListView lvVagas;
    private List<Rotina> rotinas;
    private Date dataInicial;
    private Date dataFinal;
    private String uidUsuario;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizar_vagas_aprovadas_ui);

        lvVagas = (ListView) findViewById(R.id.lv_vagas);
        sharedPreferences = getSharedPreferences("informacoes_usuario",MODE_PRIVATE);
        uidUsuario = sharedPreferences.getString("uidUsuario","");

        rotinas = new ArrayList<>();
        Calendar calendarInicial = Calendar.getInstance();
        calendarInicial.add(Calendar.MONTH, -2);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        dataInicial = calendarInicial.getTime();
        dataFinal = calendar.getTime();

        Call call = new RetrofitInicializador().getRetrofit().create(RotinaService.class).buscarRotinasVinculadasContratante(uidUsuario, formatJson.format(dataInicial), formatJson.format(dataFinal));
        RetrofitCallback callback = new RetrofitCallback(VisualizarVagasAprovadasUi.this, getString(R.string.buscando_vagas), getString(R.string.erro_buscar_vagas)) {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.code() == 200) {
                    rotinas = (List<Rotina>) response.body();
                    carregarListaVagas();
                }
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
            }
        };
        call.enqueue(callback);

        lvVagas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rotina rotina = rotinas.get(position);
                Date data = new Date();
                if(rotina.getData().before(data)) {
                    Intent intent = new Intent(VisualizarVagasAprovadasUi.this,AvaliacaoContratanteUi.class);
                    Gson gson = new Gson();
                    String json = gson.toJson(rotina);
                    intent.putExtra("rotina",json);
                    intent.putExtra("contratante",true);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(VisualizarVagasAprovadasUi.this,VisualizacaoVagaUi.class);
                    Gson gson = new Gson();
                    String json = gson.toJson(rotina);
                    intent.putExtra("rotina",json);
                    intent.putExtra("contratante",true);
                    startActivity(intent);
                }
            }
        });
    }

    public void carregarListaVagas() {
        VagaAdapter adapter = new VagaAdapter(this,rotinas);
        lvVagas.setAdapter(adapter);
    }
}
