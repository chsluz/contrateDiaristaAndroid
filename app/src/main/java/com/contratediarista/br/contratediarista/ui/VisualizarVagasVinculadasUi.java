package com.contratediarista.br.contratediarista.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.VagaAdapter;
import com.contratediarista.br.contratediarista.entity.Rotina;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.RotinaService;
import com.google.android.gms.internal.lv;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class VisualizarVagasVinculadasUi extends Fragment {
    SimpleDateFormat formatJson = new SimpleDateFormat("yyyy-MM-dd");
    Date dataInicial;
    Date dataFinal;
    private String uidUsuario;
    SharedPreferences sharedPreferences;
    private ListView lvVagas;
    private List<Rotina> rotinas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.visualizar_vagas_vinculadas_ui, container, false);
        sharedPreferences = getActivity().getSharedPreferences("informacoes_usuario", Context.MODE_PRIVATE);
        uidUsuario = sharedPreferences.getString("uidUsuario", "");
        lvVagas = (ListView) view.findViewById(R.id.lv_vagas);
        rotinas = new ArrayList<>();
        Calendar calendarInicial = Calendar.getInstance();
        calendarInicial.add(Calendar.MONTH, -2);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        dataInicial = calendarInicial.getTime();
        dataFinal = calendar.getTime();

        Call call = new RetrofitInicializador().getRetrofit()
                .create(RotinaService.class).buscarVagasUsuarioPrestador(uidUsuario, formatJson.format(dataInicial), formatJson.format(dataFinal));
        RetrofitCallback callback = new RetrofitCallback(getActivity(),
                getString(R.string.buscando_vagas),
                getString(R.string.erro_buscar_vagas)) {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.code() == 200) {
                    rotinas = (List<Rotina>) response.body();
                    carregarListaVagas();
                }
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
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
                    Fragment fragment = new AvaliacaoContratanteUi();
                    Gson gson = new Gson();
                    String json = gson.toJson(rotina);
                    Bundle bundle = new Bundle();
                    bundle.putString("rotina",json);
                    bundle.putBoolean("contratante",false);
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager()
                           .beginTransaction().replace(R.id.container,fragment).commit();
                }
                else {
                    Bundle bundle = new Bundle();
                    Gson gson = new Gson();
                    String json = gson.toJson(rotina);
                    bundle.putString("rotina",json);
                    bundle.putBoolean("contratante",false);
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
        VagaAdapter adapter = new VagaAdapter(getActivity(),rotinas);
        lvVagas.setAdapter(adapter);
    }
}
