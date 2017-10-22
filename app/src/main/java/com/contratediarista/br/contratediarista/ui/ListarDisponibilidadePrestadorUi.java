package com.contratediarista.br.contratediarista.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.DisponibilidadeAdapter;
import com.contratediarista.br.contratediarista.entity.Disponibilidade;
import com.contratediarista.br.contratediarista.entity.TipoAtividade;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.DisponibilidadeService;
import com.contratediarista.br.contratediarista.retrofit.service.TipoAtividadeService;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class ListarDisponibilidadePrestadorUi extends AppCompatActivity {
    private String json;
    private ListView lvDisponibilidades;
    private List<Disponibilidade> disponibilidades;
    private String uidUsuario;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_disponibilidade_prestador_ui);
        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            json = extra.getString("jsonObject");
        }
        sharedPreferences = getSharedPreferences("informacoes_usuario",MODE_PRIVATE);
        uidUsuario = sharedPreferences.getString("uidUsuario","");
        lvDisponibilidades = (ListView) findViewById(R.id.lv_disponibilidades);

        lvDisponibilidades.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final Disponibilidade disponibilidade = disponibilidades.get(position);
                final ProgressDialog.Builder builder = new ProgressDialog.Builder(ListarDisponibilidadePrestadorUi.this);
                builder.setCancelable(false);
                builder.setTitle("Contratação de usuário");
                builder.setMessage("Deseja contratar os serviços desse usuário? ");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        contratarUsuario(disponibilidade);
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


        carregarListaDisponibilidades();
    }

    public void contratarUsuario(Disponibilidade disponibilidade) {
        Call call = new RetrofitInicializador().getRetrofit().create(DisponibilidadeService.class).contratarUsuario(disponibilidade.getId(),uidUsuario);
        RetrofitCallback callback = new RetrofitCallback(ListarDisponibilidadePrestadorUi.this,"Contratando usuário","Erro ao contratar usuário"){
            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);
                if(response.code() == 200) {
                    Toast.makeText(ListarDisponibilidadePrestadorUi.this,"Usuário contratado com sucesso.",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else {
                    Toast.makeText(ListarDisponibilidadePrestadorUi.this,"Erro ao contratar usuário.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
            }
        };
        call.enqueue(callback);
    }

    public void carregarListaDisponibilidades() {
        RequestBody body = RequestBody.create(MediaType.parse("json"),json);
        Call call = new RetrofitInicializador().getRetrofit().create(DisponibilidadeService.class).buscarDisponibilidadesPrestador(body);
        RetrofitCallback callback = new RetrofitCallback(ListarDisponibilidadePrestadorUi.this,"Buscando disponibilidades dos prestadores","Erro ao buscar disponibilidades"){
            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);
                if(response.code() == 200) {
                    disponibilidades = (List<Disponibilidade>) response.body();
                    mostrarInformacoesDisponibilidade();
                }
                else if(response.code() == 204) {
                    Toast.makeText(ListarDisponibilidadePrestadorUi.this,"Nenhuma disponibilidade encontrada",Toast.LENGTH_SHORT).show();
                }
                else  {
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
            }
        };
        call.enqueue(callback);
    }

    private void mostrarInformacoesDisponibilidade() {
        DisponibilidadeAdapter adapter = new DisponibilidadeAdapter(this,disponibilidades);
        lvDisponibilidades.setAdapter(adapter);
    }

}
