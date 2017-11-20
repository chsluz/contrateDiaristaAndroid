package com.contratediarista.br.contratediarista.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.AprovacaoVagasAdapter;
import com.contratediarista.br.contratediarista.entity.Rotina;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.RotinaService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class VisualizarVagaCadastradaContratanteUi extends Fragment {
    private String uidUsuario;
    private SharedPreferences sharedPreferences;
    private ListView lvVagasCadastradasContratante;
    private String dataInicial;
    private String dataFinal;
    private List<Rotina> rotinas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visualizar_vaga_cadastrada_contratante_ui, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            dataInicial = bundle.getString("dataInicial");
            dataFinal = bundle.getString("dataFinal");
        }
        sharedPreferences = getActivity().getSharedPreferences("informacoes_usuario", Context.MODE_PRIVATE);
        uidUsuario = sharedPreferences.getString("uidUsuario", "");
        lvVagasCadastradasContratante = (ListView) view.findViewById(R.id.lv_vagas_cadastradas_contratante);

        lvVagasCadastradasContratante.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                final Rotina rotina = rotinas.get(position);
                final ProgressDialog.Builder builder = new ProgressDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setTitle("Excluir Vaga");
                builder.setMessage("Deseja realmente excluir esta vaga? ");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call call = new RetrofitInicializador()
                                .getRetrofit()
                                .create(RotinaService.class).excluirRotina(rotina.getId());
                        RetrofitCallback callback = new RetrofitCallback(getActivity(),
                                "Excluindo vaga", "Erro ao excluir vaga") {
                            @Override
                            public void onResponse(Call call, Response response) {
                                super.onResponse(call, response);
                                if (response.code() == 200) {
                                    Toast.makeText(getActivity(),
                                            "Excluído com sucesso.", Toast.LENGTH_SHORT).show();
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
        return view;
    }

    public void carregarListaVagasCadastradas() {
        rotinas = new ArrayList<>();
        Call call = new RetrofitInicializador()
                .getRetrofit()
                .create(RotinaService.class)
                .buscarRotinaPorUsuarioEData(uidUsuario, dataInicial, dataFinal);
        RetrofitCallback callback = new RetrofitCallback(getActivity(), "Buscando vagas cadastradas", "Erro ao buscar vagas cadastradas") {
            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);
                if (response.code() == 200) {
                    rotinas = (List<Rotina>) response.body();
                    mostrarLista();
                } else if (response.code() == 204) {
                    Toast.makeText(getActivity(), getString(R.string.nenhum_resultado_encontrado), Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new AprovacaoVagasUi()).commit();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new AprovacaoVagasUi()).commit();
            }
        };
        call.enqueue(callback);
    }


    public void mostrarLista() {
        AprovacaoVagasAdapter adapter = new AprovacaoVagasAdapter(getActivity(), rotinas);
        lvVagasCadastradasContratante.setAdapter(adapter);
    }
}
