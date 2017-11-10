package com.contratediarista.br.contratediarista.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.contratediarista.br.contratediarista.retrofit.service.RotinaService;
import com.contratediarista.br.contratediarista.retrofit.service.TipoAtividadeService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Response;

public class InformacoesAprovacaoUi extends Fragment {
    private Rotina rotina;
    private TextView tvPrestadorSelecionado;
    private Button btnRemoverPrestadorSelecionado;
    private ListView lvPrestadores;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_informacoes_aprovacao_ui, container, false);
        Bundle extra = this.getArguments();
        if (extra != null) {
            Gson gson = new Gson();
            rotina = gson.fromJson(extra.getString("rotina"), Rotina.class);
        }
        tvPrestadorSelecionado = (TextView) view.findViewById(R.id.tv_prestador_selecionado);
        btnRemoverPrestadorSelecionado = (Button) view.findViewById(R.id.btn_remover_prestador_selecionado);
        if (rotina.getPrestadorSelecionado() != null) {
            tvPrestadorSelecionado.setText(rotina.getPrestadorSelecionado().getNome());
        } else {
            tvPrestadorSelecionado.setText("Nenhum");
            btnRemoverPrestadorSelecionado.setVisibility(View.GONE);
        }

        btnRemoverPrestadorSelecionado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog.Builder builder = new ProgressDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setTitle("Excluir usuário selecionado");
                builder.setMessage("Deseja desvincular o usuário selecionado? ");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call call = new RetrofitInicializador().getRetrofit().create(RotinaService.class).removerPrestadorSelecionado(rotina.getId());
                        RetrofitCallback callback = new RetrofitCallback(getActivity(),"Removendo prestador selecionado","Erro ao remover prestador selecionado"){
                            @Override
                            public void onResponse(Call call, Response response) {
                                super.onResponse(call, response);
                                if(response.code() == 200) {
                                    Fragment fragment = new AprovacaoVagasUi();
                                    Toast.makeText(getActivity(),"Prestador selecionado removido com sucesso",Toast.LENGTH_SHORT).show();
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
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
            }
        });

        lvPrestadores = (ListView) view.findViewById(R.id.lv_prestadores);

        lvPrestadores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Usuario usuario = rotina.getPrestadores().get(position);
                Gson gson = new Gson();
                String jsonRotina = gson.toJson(rotina);
                String jsonUsuario = gson.toJson(usuario);
                Fragment fragment = new InformacoesUsuarioUi();
                Bundle bundle = new Bundle();
                bundle.putString("usuario",jsonUsuario);
                bundle.putString("rotina",jsonRotina);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            }
        });

        carregarListaPrestadores();
        return view;
    }

    public void carregarListaPrestadores() {
        UsuarioAdapter adapter = new UsuarioAdapter(getActivity(), rotina.getPrestadores());
        lvPrestadores.setAdapter(adapter);
    }
}
