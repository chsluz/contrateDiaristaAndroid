package com.contratediarista.br.contratediarista.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.AprovacaoVagasAdapter;
import com.contratediarista.br.contratediarista.entity.Disponibilidade;
import com.contratediarista.br.contratediarista.entity.Rotina;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.DisponibilidadeService;
import com.contratediarista.br.contratediarista.retrofit.service.RotinaService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class VisualizarAprovacaoVagaUi extends AppCompatActivity {
    private String uidUsuario;
    private SharedPreferences sharedPreferences;
    private ListView lvAprovacaoVagas;
    private String dataInicial;
    private String dataFinal;
    private List<Rotina> rotinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizar_aprovacao_vaga_ui);
        sharedPreferences = getSharedPreferences("informacoes_usuario",MODE_PRIVATE);
        uidUsuario = sharedPreferences.getString("uidUsuario","");
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            dataInicial = extra.get("dataInicial").toString();
            dataFinal = extra.getString("dataFinal");
        }
        lvAprovacaoVagas = (ListView) findViewById(R.id.lv_aprovacao_vagas);

        lvAprovacaoVagas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rotina rotina = rotinas.get(position);
                if(rotina.getPrestadores()  == null || rotina.getPrestadores().isEmpty()) {
                    Toast.makeText(VisualizarAprovacaoVagaUi.this,"Nenhum prestador se candidatou a essa vaga",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(VisualizarAprovacaoVagaUi.this,InformacoesAprovacaoUi.class);
                    Gson gson = new Gson();
                    String json = gson.toJson(rotina);
                    intent.putExtra("rotina",json);
                    startActivity(intent);
                }
            }
        });


        lvAprovacaoVagas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Rotina rotina = rotinas.get(position);
                final ProgressDialog.Builder builder = new ProgressDialog.Builder(VisualizarAprovacaoVagaUi.this);
                builder.setCancelable(false);
                builder.setTitle("Excluir Vaga");
                builder.setMessage("Deseja realmente excluir esta vaga? ");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call call = new RetrofitInicializador().getRetrofit().create(RotinaService.class).excluirRotina(rotina.getId());
                        RetrofitCallback callback = new RetrofitCallback(VisualizarAprovacaoVagaUi.this,"Excluindo vaga","Erro ao excluir vaga"){
                            @Override
                            public void onResponse(Call call, Response response) {
                                super.onResponse(call, response);
                                if(response.code() == 200) {
                                    Toast.makeText(VisualizarAprovacaoVagaUi.this,"Excluído com sucesso.",Toast.LENGTH_SHORT).show();
                                    carregarListaVagasCadastradas();
                                }
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                super.onFailure(call, t);
                            }
                        };
                        call.enqueue(callback);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });

        carregarListaVagasCadastradas();
    }


    public void carregarListaVagasCadastradas() {
        rotinas = new ArrayList<>();
        Call call = new RetrofitInicializador().getRetrofit().create(RotinaService.class).buscarRotinaPorUsuarioEData(uidUsuario,dataInicial,dataFinal);
        RetrofitCallback callback = new RetrofitCallback(VisualizarAprovacaoVagaUi.this,"Buscando vagas cadastradas","Erro ao buscar vagas cadastradas") {
            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);
                if(response.code() == 200) {
                    rotinas =  (List<Rotina>) response.body();
                    mostrarLista();
                }
                else if(response.code() == 204) {
                    Toast.makeText(VisualizarAprovacaoVagaUi.this,getString(R.string.nenhum_resultado_encontrado),Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                onBackPressed();
            }
        };
        call.enqueue(callback);
    }


    public void mostrarLista() {
        AprovacaoVagasAdapter adapter = new AprovacaoVagasAdapter(this,rotinas);
        lvAprovacaoVagas.setAdapter(adapter);
    }
}
