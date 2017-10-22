package com.contratediarista.br.contratediarista.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.DisponibilidadeAdapter;
import com.contratediarista.br.contratediarista.entity.Disponibilidade;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.DisponibilidadeService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ConsultarDisponibilidade extends AppCompatActivity {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private ListView lvDisponibilidade;
    private Date dataInicial;
    private Date dataFinal;
    private String uidUsuario;
    private SharedPreferences sharedPreferences;
    private List<Disponibilidade> disponibilidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_disponibilidade);
        lvDisponibilidade = (ListView) findViewById(R.id.lv_disponibilidade);
        sharedPreferences = getSharedPreferences("informacoes_usuario", MODE_PRIVATE);
        uidUsuario = sharedPreferences.getString("uidUsuario", "");
        dataInicial = new Date();
        Calendar dtFinal = Calendar.getInstance();
        dtFinal.add(Calendar.MONTH, 1);
        dataFinal = dtFinal.getTime();
        disponibilidades = new ArrayList<>();



        lvDisponibilidade.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Disponibilidade disponibilidade = disponibilidades.get(position);
                final ProgressDialog.Builder builder = new ProgressDialog.Builder(ConsultarDisponibilidade.this);
                builder.setCancelable(false);
                builder.setTitle("Excluir Disponibilidade");
                builder.setMessage("Deseja realmente excluir esta disponibilidade? ");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call call = new RetrofitInicializador().getRetrofit().create(DisponibilidadeService.class).excluirDisponibilidade(disponibilidade.getId());

                        RetrofitCallback callback = new RetrofitCallback(ConsultarDisponibilidade.this,"Excluindo Disponibilidade","Erro ao excluir disponibilidade"){
                            @Override
                            public void onResponse(Call call, Response response) {
                                if(response.code() == 200) {
                                    Toast.makeText(ConsultarDisponibilidade.this,"Disponibilidade excluída com sucesso.",Toast.LENGTH_SHORT).show();
                                    carregarDisponibilidades();
                                }
                                else {
                                        Toast.makeText(ConsultarDisponibilidade.this,"Erro ao excluir disponibilidade.",Toast.LENGTH_SHORT).show();
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
        carregarDisponibilidades();
    }

    private void carregarDisponibilidades() {
        Call call = new RetrofitInicializador().getRetrofit().create(DisponibilidadeService.class)
                .buscarPorUsuarioEData(uidUsuario, sdf.format(dataInicial), sdf.format(dataFinal));
        RetrofitCallback callback = new RetrofitCallback(ConsultarDisponibilidade.this, "Buscando disponibilidades cadastradas", "Erro ao buscar disponibilidades cadastradas") {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    disponibilidades = (List<Disponibilidade>) response.body();
                    carregarListaDisponibilidades();
                }
                super.onResponse(call, response);
                if (response.code() != 200) {
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

    public void carregarListaDisponibilidades() {
        DisponibilidadeAdapter adapter = new DisponibilidadeAdapter(this, disponibilidades);
        lvDisponibilidade.setAdapter(adapter);

    }
}
