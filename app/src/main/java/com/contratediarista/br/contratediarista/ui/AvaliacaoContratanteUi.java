package com.contratediarista.br.contratediarista.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.entity.Rotina;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.AvaliacaoService;
import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Response;

public class AvaliacaoContratanteUi extends AppCompatActivity {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat formatJson = new SimpleDateFormat("yyyy-MM-dd");
    private Gson gson = new Gson();
    private Rotina rotina;
    private String uidUsuario;
    private SharedPreferences sharedPreferences;
    private TextView tvNome;
    private TextView tvData;
    private EditText etNota;
    private EditText etObservacao;
    private Button btnAvaliar;
    private boolean contratante;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avaliacao_contratante_ui);
        sharedPreferences = getSharedPreferences("informacoes_usuario",MODE_PRIVATE);
        uidUsuario = sharedPreferences.getString("uidUsuario","");
        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            rotina = gson.fromJson(extra.get("rotina").toString(),Rotina.class);
            contratante = extra.getBoolean("contratante");
        }
        tvNome = (TextView) findViewById(R.id.tv_nome_contratante);
        tvData = (TextView) findViewById(R.id.tv_data);
        etNota = (EditText) findViewById(R.id.et_nota);
        etObservacao = (EditText) findViewById(R.id.et_observacao);
        btnAvaliar = (Button) findViewById(R.id.btn_avaliar);

        if(contratante) {
            tvNome.setText("Nome: " + rotina.getVaga().getContratante().getNome());
        }
        else {
            tvNome.setText("Nome: " + rotina.getPrestadorSelecionado().getNome());
        }
        tvData.setText("Data: "+ sdf.format(rotina.getData()));


        btnAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNota.getText().equals("")) {
                    Toast.makeText(AvaliacaoContratanteUi.this,"Preenchar o campo nota.",Toast.LENGTH_SHORT).show();
                }
                else {
                    int nota = Integer.parseInt(etNota.getText().toString());
                    String dataJson = formatJson.format(rotina.getData());
                    String uidUsuarioAvaliador;
                    String uidUsuarioAvaliado;
                    if(contratante) {
                        uidUsuarioAvaliador = uidUsuario;
                        uidUsuarioAvaliado = rotina.getPrestadorSelecionado().getUid();
                    }
                    else {
                        uidUsuarioAvaliador = uidUsuario;
                        uidUsuarioAvaliado = rotina.getVaga().getContratante().getUid();

                    }
                    Call call = new RetrofitInicializador().getRetrofit()
                            .create(AvaliacaoService.class)
                            .avaliarUsuario(uidUsuarioAvaliador,uidUsuarioAvaliado,
                                    nota,dataJson,etObservacao.getText().toString());
                    RetrofitCallback callback = new RetrofitCallback(AvaliacaoContratanteUi.this,"Salvando Avaliação","Erro ao salvar avaliação"){
                        @Override
                        public void onResponse(Call call, Response response) {
                            if(response.code() == 200) {
                                Toast.makeText(AvaliacaoContratanteUi.this,"Avaliação salva com sucesso.",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(AvaliacaoContratanteUi.this,"Erro ao salvar avaliação.",Toast.LENGTH_SHORT).show();
                            }
                            super.onResponse(call, response);
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Toast.makeText(AvaliacaoContratanteUi.this,"Erro ao salvar avaliação.",Toast.LENGTH_SHORT).show();
                            super.onFailure(call, t);
                        }
                    };
                    call.enqueue(callback);
                }
            }
        });

    }
}
