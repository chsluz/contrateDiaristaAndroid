package com.contratediarista.br.contratediarista.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.UsuarioAdapter;
import com.contratediarista.br.contratediarista.entity.Rotina;
import com.contratediarista.br.contratediarista.entity.TipoAtividade;
import com.contratediarista.br.contratediarista.entity.Usuario;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.TipoAtividadeService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Response;

public class InformacoesAprovacaoUi extends AppCompatActivity {
    private Rotina rotina;
    private TextView tvPrestadorSelecionado;
    private ListView lvPrestadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_aprovacao_ui);
        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            Gson gson = new Gson();
            rotina = gson.fromJson(extra.getString("rotina"),Rotina.class);
        }
        tvPrestadorSelecionado = (TextView) findViewById(R.id.tv_prestador_selecionado);

        tvPrestadorSelecionado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog.Builder builder = new ProgressDialog.Builder(InformacoesAprovacaoUi.this);
                builder.setCancelable(false);
                builder.setTitle("Excluir usuário selecionado");
                builder.setMessage("Deseja desvincular o usuário selecionado? ");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
            }
        });



        lvPrestadores = (ListView) findViewById(R.id.lv_prestadores);

        if(rotina.getPrestadorSelecionado() != null) {
            tvPrestadorSelecionado.setText(rotina.getPrestadorSelecionado().getNome());
        }

        lvPrestadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Usuario usuario = rotina.getPrestadores().get(position);
                Intent intent = new Intent(InformacoesAprovacaoUi.this,InformacoesUsuarioUi.class);
                Gson gson = new Gson();
                String jsonRotina = gson.toJson(rotina);
                String jsonUsuario = gson.toJson(usuario);

                intent.putExtra("usuario",jsonUsuario);
                intent.putExtra("rotina",jsonRotina);
                startActivity(intent);
            }
        });

        carregarListaPrestadores();
    }

    public void carregarListaPrestadores() {
        UsuarioAdapter adapter = new UsuarioAdapter(this,rotina.getPrestadores());
        lvPrestadores.setAdapter(adapter);
    }
}
