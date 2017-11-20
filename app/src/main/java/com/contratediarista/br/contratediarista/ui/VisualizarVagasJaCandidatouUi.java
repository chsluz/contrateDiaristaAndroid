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
import android.widget.TextView;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.VagasJaCandidatouAdapter;
import com.contratediarista.br.contratediarista.entity.Rotina;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.RotinaService;
import com.contratediarista.br.contratediarista.retrofit.service.TipoAtividadeService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class VisualizarVagasJaCandidatouUi extends Fragment {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private TextView tvNenhumResultado;
    private ListView lvVagasQueSeCandidatou;
    private Date data;
    private SharedPreferences sharedPreferences;
    private List<Rotina> rotinas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.visualizar_vagas_ja_candidatou_ui, container, false);
        data = new Date();
        sharedPreferences = getActivity().getSharedPreferences("informacoes_usuario", Context.MODE_PRIVATE);
        lvVagasQueSeCandidatou = (ListView) view.findViewById(R.id.lv_vagas_que_se_candidatou);
        lvVagasQueSeCandidatou.setVisibility(View.GONE);
        tvNenhumResultado = (TextView) view.findViewById(R.id.tv_nenhuma_vaga);
        tvNenhumResultado.setVisibility(View.VISIBLE);
        carregarVagasJaCandidatou();

        lvVagasQueSeCandidatou.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final Rotina rotina = rotinas.get(position);
                final ProgressDialog.Builder builder = new ProgressDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setTitle("Vaga que se candidatou");
                builder.setMessage("o que deseja fazer com a vaga?");
                builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call call = new RetrofitInicializador()
                                .getRetrofit()
                                .create(RotinaService.class).removerPrestadorSelecionado(rotina.getId());
                        RetrofitCallback callback =
                                new RetrofitCallback(getActivity(),"Excluindo vaga que se candidatou",
                                        "Erro ao excluir vaga que se candidatou") {
                                    @Override
                                    public void onResponse(Call call, Response response) {
                                        if(response.code() == 200) {
                                            Toast.makeText(getActivity(),
                                                    getString(R.string.excluido_sucesso),
                                                    Toast.LENGTH_SHORT).show();
                                            carregarVagasJaCandidatou();
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
                builder.setNegativeButton("Visualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Gson gson = new Gson();
                        String json = gson.toJson(rotina);
                        Bundle bundle = new Bundle();
                        bundle.putString("rotina",json);
                        Fragment fragment = new VisualizacaoVagaUi();
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction().replace(R.id.container,fragment).commit();
                        dialog.dismiss();
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });
        return view;
    }

    public void carregarVagasJaCandidatou() {
        String dataFormatada = sdf.format(data);
        String uidUsuario = sharedPreferences.getString("uidUsuario","");
        Call call = new RetrofitInicializador().
                getRetrofit().create(RotinaService.class).listarRotinasJaCandidatou(dataFormatada,uidUsuario);
        RetrofitCallback callback = new RetrofitCallback(getActivity()
                ,"Buscando vagas que já candidatou-se",
                "Erro ao buscar vagas que já se candidatou"){
            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);
                if(response.code() == 200) {
                    rotinas = (List<Rotina>) response.body();
                    carregarLista();
                }
                else {
                    if(response.code() == 204) {
                        Toast.makeText(getActivity(),
                                "Você não se candidatou a nenhuma vaga",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(),
                                "Erro ao buscar vagas que já se candidatou",Toast.LENGTH_SHORT).show();
                    }
                    lvVagasQueSeCandidatou.setVisibility(View.GONE);
                    tvNenhumResultado.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                Toast.makeText(getActivity(),
                        "Erro ao buscar vagas que já se candidatou",Toast.LENGTH_SHORT).show();
                lvVagasQueSeCandidatou.setVisibility(View.GONE);
                tvNenhumResultado.setVisibility(View.VISIBLE);
            }
        };
        call.enqueue(callback);
    }

    public void carregarLista() {
        lvVagasQueSeCandidatou.setVisibility(View.VISIBLE);
        tvNenhumResultado.setVisibility(View.GONE);
        VagasJaCandidatouAdapter adapter = new VagasJaCandidatouAdapter(getActivity(),rotinas);
        lvVagasQueSeCandidatou.setAdapter(adapter);
    }

}
