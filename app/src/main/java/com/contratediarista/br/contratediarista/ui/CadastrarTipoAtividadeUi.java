package com.contratediarista.br.contratediarista.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.ListaAtividadesAdapter;
import com.contratediarista.br.contratediarista.entity.TipoAtividade;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.DisponibilidadeService;
import com.contratediarista.br.contratediarista.retrofit.service.TipoAtividadeService;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CadastrarTipoAtividadeUi extends AppCompatActivity {
    private EditText etDescricao;
    private Button btnCadastrar;
    private ListView lvTiposAtividade;
    private List<TipoAtividade> tiposAtividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_tipo_atividade_ui);

        etDescricao = (EditText) findViewById(R.id.et_descricao);
        btnCadastrar = (Button) findViewById(R.id.btn_cadastrar);
        lvTiposAtividade = (ListView) findViewById(R.id.lv_tipos_atividade);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etDescricao.getText().equals("")) {
                    Toast.makeText(CadastrarTipoAtividadeUi.this,getString(R.string.digite_descricao),Toast.LENGTH_SHORT).show();
                }
                else {
                    Call call = new RetrofitInicializador().getRetrofit().create(TipoAtividadeService.class).cadastrarTipoAtividade(etDescricao.getText().toString());
                    etDescricao.setText("");
                    RetrofitCallback callback = new RetrofitCallback(CadastrarTipoAtividadeUi.this,getString(R.string.cadastrando_tipo_atividade),getString(R.string.erro_ao_cadastrar_tipo_atividade)) {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if(response.code() == 200) {
                                Toast.makeText(CadastrarTipoAtividadeUi.this,getString(R.string.tipo_atividade_cadastrado_sucesso),Toast.LENGTH_SHORT).show();
                            }
                            super.onResponse(call, response);
                            carregarListaAtividade();
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            super.onFailure(call, t);
                            carregarListaAtividade();
                        }
                    };
                    call.enqueue(callback);
                }
            }
        });

        lvTiposAtividade.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final TipoAtividade tipo = tiposAtividade.get(position);
                final ProgressDialog.Builder builder = new ProgressDialog.Builder(CadastrarTipoAtividadeUi.this);
                builder.setCancelable(false);
                builder.setTitle("Excluir tipo de atividade");
                builder.setMessage("Deseja realmente excluir esta atividade? ");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call call = new RetrofitInicializador().getRetrofit().create(TipoAtividadeService.class).excluirTipoAtividade(tipo.getId());
                        RetrofitCallback callback = new RetrofitCallback(CadastrarTipoAtividadeUi.this,getString(R.string.excluindo_atividade),getString(R.string.erro_excluir_atividade)) {
                            @Override
                            public void onResponse(Call call, Response response) {
                                if(response.code() == 200) {
                                    Toast.makeText(CadastrarTipoAtividadeUi.this,getString(R.string.excluido_sucesso),Toast.LENGTH_SHORT).show();
                                    carregarListaAtividade();
                                }
                                super.onResponse(call, response);
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
        carregarListaAtividade();
    }

    public void carregarListaAtividade() {
        Call call = new RetrofitInicializador().getRetrofit().create(TipoAtividadeService.class).listarTodos();
        RetrofitCallback callback = new RetrofitCallback(this,getString(R.string.buscando_tipos_atividade),getString(R.string.erro_buscar_tipos_atividade)) {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.code() == 200) {
                    montarResultado((JsonArray) response.body());
                    carregarListaTipoAtividade();
                }
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
            }
        };
        call.enqueue(callback);

    }

    public void montarResultado(JsonArray array) {
        tiposAtividade = new ArrayList<>();
        for(int i = 0; i < array.size(); i++ ) {
            TipoAtividade tipo = TipoAtividade.toTipoAtividadeGson(array.get(i).getAsJsonObject());
            tiposAtividade.add(tipo);
        }
    }

    public void carregarListaTipoAtividade() {
        ListaAtividadesAdapter adapter = new ListaAtividadesAdapter(this,tiposAtividade);
        lvTiposAtividade.setAdapter(adapter);
    }
}
