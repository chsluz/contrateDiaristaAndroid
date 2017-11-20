package com.contratediarista.br.contratediarista.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

public class VisualizarVagasAprovadasUi extends Fragment {
    SimpleDateFormat formatJson = new SimpleDateFormat("yyyy-MM-dd");
    private ListView lvVagas;
    private TextView tvNenhumResultado;
    private List<Rotina> rotinas;
    private Date dataInicial;
    private Date dataFinal;
    private String uidUsuario;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.visualizar_vagas_aprovadas_ui, container, false);

        lvVagas = (ListView) view.findViewById(R.id.lv_vagas);
        tvNenhumResultado = (TextView) view.findViewById(R.id.tv_nenhum_resultado);
        tvNenhumResultado.setText("Nenhuma vaga na sua agenda");
        lvVagas.setVisibility(View.GONE);
        tvNenhumResultado.setVisibility(View.VISIBLE);
        sharedPreferences = getActivity()
                .getSharedPreferences("informacoes_usuario", Context.MODE_PRIVATE);
        uidUsuario = sharedPreferences.getString("uidUsuario","");

        rotinas = new ArrayList<>();
        Calendar calendarInicial = Calendar.getInstance();
        calendarInicial.add(Calendar.MONTH, -2);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        dataInicial = calendarInicial.getTime();
        dataFinal = calendar.getTime();

        Call call = new RetrofitInicializador()
                .getRetrofit().create(RotinaService.class)
                .buscarRotinasVinculadasContratante(uidUsuario,
                        formatJson.format(dataInicial), formatJson.format(dataFinal));
        RetrofitCallback callback =
                new RetrofitCallback(getActivity(),
                        getString(R.string.buscando_vagas),
                        getString(R.string.erro_buscar_vagas)) {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.code() == 200) {
                    rotinas = (List<Rotina>) response.body();
                    carregarListaVagas();
                }
                else{
                    lvVagas.setVisibility(View.GONE);
                    tvNenhumResultado.setVisibility(View.VISIBLE);
                }
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                lvVagas.setVisibility(View.GONE);
                tvNenhumResultado.setVisibility(View.VISIBLE);
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
                    Gson gson = new Gson();
                    String json = gson.toJson(rotina);
                    Bundle bundle = new Bundle();
                    bundle.putString("rotina",json);
                    bundle.putBoolean("contratante",true);
                    Fragment fragment = new AvaliacaoContratanteUi();
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().replace(R.id.container,fragment).commit();
                }
                else {
                    Gson gson = new Gson();
                    String json = gson.toJson(rotina);
                    Bundle bundle = new Bundle();
                    bundle.putString("rotina",json);
                    bundle.putBoolean("contratante",true);
                    Fragment fragment = new VisualizacaoVagaUi();
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().replace(R.id.container,fragment).commit();
                }
            }
        });
        return view;
    }


    public void carregarListaVagas() {
        lvVagas.setVisibility(View.VISIBLE);
        tvNenhumResultado.setVisibility(View.GONE);
        VagaAdapter adapter = new VagaAdapter(getActivity(),rotinas);
        lvVagas.setAdapter(adapter);
    }
}
