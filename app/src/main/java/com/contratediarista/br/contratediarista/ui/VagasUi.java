package com.contratediarista.br.contratediarista.ui;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.VagaAdapter;
import com.contratediarista.br.contratediarista.entity.Rotina;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.VagaService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class VagasUi extends Fragment {

    private List<Rotina> rotinas;
    private ListView lvVagas;
    private String json;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vagas_ui, container, false);
        rotinas = new ArrayList<>();
        Bundle bundle = this.getArguments();
        lvVagas = (ListView) view.findViewById(R.id.lv_vagas);
        json = bundle.get("jsonObject").toString();
        RequestBody body = RequestBody.create(MediaType.parse("json"),json);
        Call call = new RetrofitInicializador()
                .getRetrofit().create(VagaService.class)
                .buscarVagasDisponiveisPorDataValor(body);
        RetrofitCallback callback =
                new RetrofitCallback(getActivity(),
                        getString(R.string.buscando_vagas),
                        getString(R.string.erro_buscar_vagas)) {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    rotinas = (List<Rotina>) response.body();
                    carregarListaVagas();
                }

                super.onResponse(call, response);

                if(response.code() == 204) {
                    Toast.makeText(getActivity(),
                            getString(R.string.nenhum_resultado_encontrado),
                            Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().replace(R.id.container,new ConsultarVagasUi()).commit();
                }
                else if(response.code() != 200 && response.code() != 204) {
                    Toast.makeText(getActivity(),
                            getString(R.string.erro_ao_pesquisar),
                            Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().replace(R.id.container,new ConsultarVagasUi()).commit();

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                Toast.makeText(getActivity(),
                        getString(R.string.erro_ao_pesquisar),Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.container,
                        new ConsultarVagasUi()).commit();

            }
        };
        call.enqueue(callback);


        lvVagas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rotina rotina = rotinas.get(position);
                Gson gson = new Gson();
                String json = gson.toJson(rotina);
                Bundle bundle = new Bundle();
                bundle.putString("rotina",json);
                Fragment fragment = new VisualizacaoVagaUi();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.container,fragment).commit();
            }
        });
        return view;
    }

    public void carregarListaVagas() {
        VagaAdapter adapter = new VagaAdapter(getActivity(),rotinas);
        lvVagas.setAdapter(adapter);
    }

}
