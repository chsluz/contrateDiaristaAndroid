package com.contratediarista.br.contratediarista.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.SpinnerTipoPeriodoAdapter;
import com.contratediarista.br.contratediarista.entity.TipoAtividade;
import com.contratediarista.br.contratediarista.entity.Usuario;
import com.contratediarista.br.contratediarista.enuns.TipoPeriodo;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.DisponibilidadeService;
import com.contratediarista.br.contratediarista.retrofit.service.TipoAtividadeService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class CadastrarDisponibilidadeUi extends Fragment {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat formatoJson = new SimpleDateFormat("yyyy-MM-dd");

    private Usuario usuario;
    private LinearLayout linearLayout;
    private List<CheckBox> listaCheckboxAtividades;
    private List<TipoAtividade> tiposAtividade;
    private List<TipoPeriodo> tiposPeriodo;
    private List<TipoAtividade> atividadesSelecionadas;
    private TipoPeriodo tipoPeriodo;
    private Date data;
    private EditText etData;
    private EditText etValor;
    private Spinner spTipoPeriodo;
    private String uidUsuario;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cadastrar_disponibilidade_ui, container, false);
        sharedPreferences = getActivity().getSharedPreferences("informacoes_usuario", Context.MODE_PRIVATE);
        uidUsuario = sharedPreferences.getString("uidUsuario","");
        listaCheckboxAtividades = new ArrayList<>();
        linearLayout = (LinearLayout) view.findViewById(R.id.linear_cadastrar_disponibilidade);
        carregarListaTipoAtividade();
        data = new Date();
        return view;
    }


    public void carregarListaTipoAtividade() {
        tiposAtividade = new ArrayList<>();
        Call call = new RetrofitInicializador().getRetrofit().create(TipoAtividadeService.class).listarTodos();
        RetrofitCallback callback =
                new RetrofitCallback(getActivity(),
                        getString(R.string.carregando_lista_atividades),
                        getString(R.string.erro_carregar_lista_atividades)) {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.code() == 200) {
                    JsonArray array = (JsonArray) response.body();
                    montarResultado(array);
                    carregarLayout();
                }
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
            }
        };
        call.enqueue(callback);
    }

    public void montarResultado(JsonArray array) {
        for(int i = 0; i < array.size(); i++ ) {
            TipoAtividade tipo = TipoAtividade.toTipoAtividadeGson(array.get(i).getAsJsonObject());
            tiposAtividade.add(tipo);
        }
    }

    public void carregarLayout() {
        float scale = getResources().getDisplayMetrics().density;
        int i = 1;
        LinearLayout.LayoutParams param =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.setMargins((int) (15*scale),(int) (3*scale),0,0);

        TextView tvData = new TextView(getActivity());
        tvData.setText(getString(R.string.data));
        tvData.setLayoutParams(param);

        linearLayout.addView(tvData);

        etData = new EditText(getActivity());
        etData.setText(sdf.format(data));
        etData.setLayoutParams(param);
        etData.setInputType(InputType.TYPE_CLASS_DATETIME);
        etData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        linearLayout.addView(etData);

        TextView tvTipoPeriodo = new TextView(getActivity());
        tvTipoPeriodo.setLayoutParams(param);
        tvTipoPeriodo.setText(getString(R.string.tipo_periodo));

        linearLayout.addView(tvTipoPeriodo);

        tiposPeriodo = Arrays.asList(TipoPeriodo.values());

        spTipoPeriodo = new Spinner(getActivity());
        spTipoPeriodo.setLayoutMode(Spinner.MODE_DIALOG);
        LinearLayout.LayoutParams paramSpinner = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        paramSpinner.setMargins((int) (15*scale),(int) (3*scale),0,0);
        spTipoPeriodo.setLayoutParams(paramSpinner);

        SpinnerTipoPeriodoAdapter adapter = new SpinnerTipoPeriodoAdapter(getActivity(),tiposPeriodo);
        spTipoPeriodo.setAdapter(adapter);
        spTipoPeriodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoPeriodo = tiposPeriodo.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        linearLayout.addView(spTipoPeriodo);

        TextView tvValor = new TextView(getActivity());
        tvValor.setLayoutParams(param);
        tvValor.setText(getString(R.string.valor_periodo));
        linearLayout.addView(tvValor);

        etValor = new EditText(getActivity());
        etValor.setInputType(InputType.TYPE_CLASS_NUMBER);
        etValor.setLayoutParams(param);
        linearLayout.addView(etValor);


        LinearLayout linearCheckbox = new LinearLayout(getActivity());
        linearCheckbox.setId(View.generateViewId());
        linearCheckbox.setOrientation(LinearLayout.HORIZONTAL);

        for(TipoAtividade tipo : tiposAtividade) {
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins((int) (15*scale),(int)(3*scale),0,0);
            CheckBox checkBox = new CheckBox(getActivity());
            checkBox.setId(View.generateViewId());
            checkBox.setText(tipo.getDescricao());
            checkBox.setLayoutParams(params);
            listaCheckboxAtividades.add(checkBox);
            if(i <= 2) {
                linearCheckbox.addView(checkBox);
            }
            else {
                i = 1;
                linearLayout.addView(linearCheckbox);
                linearCheckbox = new LinearLayout(getActivity());
                linearCheckbox.setId(View.generateViewId());
                linearCheckbox.setOrientation(LinearLayout.HORIZONTAL);
                linearCheckbox.addView(checkBox);
            }
            i++;
        }
        linearLayout.addView(linearCheckbox);

        Button btnCadastrar = new Button(getActivity());
        LinearLayout.LayoutParams buttonParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParam.setMargins((int) (10*scale),0,(int)(10*scale),0);
        btnCadastrar.setLayoutParams(buttonParam);
        btnCadastrar.setText(getString(R.string.cadastrar_disponibilidade));
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validacao = true;
                verificarListaAtividadesSelecionados();
                if(tipoPeriodo == null) {
                    Toast.makeText(getActivity(),
                            getString(R.string.selecione_tipo_periodo),Toast.LENGTH_SHORT).show();
                    validacao = false;
                }
                if(etValor.getText().equals("")) {
                    Toast.makeText(getActivity(),
                            getString(R.string.digite_valor_periodo),Toast.LENGTH_SHORT).show();
                    validacao = false;
                }
                if(atividadesSelecionadas.isEmpty()) {
                    Toast.makeText(getActivity(),
                            getString(R.string.selecione_pelo_menos_uma_atividade),Toast.LENGTH_SHORT).show();
                    validacao = false;
                }
                if(validacao) {
                    JsonObject jsonObjet = new JsonObject();
                    jsonObjet.addProperty("valorPeriodo",etValor.getText().toString());
                    jsonObjet.addProperty("tipoPeriodo",tipoPeriodo.ordinal());
                    jsonObjet.addProperty("data",formatoJson.format(data));
                    JsonArray arrayAtividade = new JsonArray();
                    for(TipoAtividade tipoAtividade : atividadesSelecionadas) {
                        JsonObject objectAtividade = new JsonObject();
                        objectAtividade.addProperty("id",tipoAtividade.getId());
                        arrayAtividade.add(objectAtividade);
                    }
                    jsonObjet.add("tiposAtividade",arrayAtividade);

                    RequestBody body = RequestBody.create(MediaType.parse("json"),jsonObjet.toString());
                    Call call = new RetrofitInicializador().getRetrofit()
                            .create(DisponibilidadeService.class).cadastrarDisponibilidade(uidUsuario,body);
                    RetrofitCallback callback =
                            new RetrofitCallback(getActivity(),getString(R.string.cadastrando_disponibilidade),getString(R.string.erro_cadastrar_disponibilidade)) {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if(response.code() == javax.ws.rs.core.Response.Status.OK.getStatusCode()) {
                                Toast.makeText(getActivity(),
                                        getActivity().getString(R.string.disponibilidade_cadastrada_sucesso),Toast.LENGTH_SHORT).show();
                            }
                            super.onResponse(call, response);
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            super.onFailure(call, t);
                        }
                    };
                    call.enqueue(callback);
                }
            }
        });

        linearLayout.addView(btnCadastrar);

    }


    public void verificarListaAtividadesSelecionados() {
        atividadesSelecionadas = new ArrayList<>();
        for(CheckBox check : listaCheckboxAtividades) {
            for(TipoAtividade atividade : tiposAtividade) {
                if(check.getText().equals(atividade.getDescricao())) {
                    if(check.isChecked()) {
                        atividadesSelecionadas.add(atividade);
                    }
                }
            }
        }
    }

    public void showDatePickerDialog(View view) {
        final DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR,year);
                    calendar.set(Calendar.MONTH,month);
                    calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    data = calendar.getTime();
                    etData.setText(sdf.format(data));
                }catch (Exception e) {

                }
            }
        };
        Calendar calendario = Calendar.getInstance();
        int year = calendario.get(Calendar.YEAR);
        int month = calendario.get(Calendar.MONTH);
        int day = calendario.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),datePicker,year,month,day);
        dialog.show();
    }
}
