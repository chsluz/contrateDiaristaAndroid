package com.contratediarista.br.contratediarista.ui;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.SpinnerTipoPeriodoAdapter;
import com.contratediarista.br.contratediarista.enuns.DiasSemana;
import com.contratediarista.br.contratediarista.enuns.TipoPeriodo;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.VagaService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@TargetApi(24)
public class CadastrarVagaUi extends AppCompatActivity {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatoJson = new SimpleDateFormat("yyyy-MM-dd");
    String uidUsuario = "";
    private EditText etDataInicial;
    private EditText etDataFinal;
    private EditText etValorPeriodo;
    private Spinner spTipoPeriodo;
    private CheckBox checkSeg;
    private CheckBox checkTer;
    private CheckBox checkQua;
    private CheckBox checkQui;
    private CheckBox checkSex;
    private CheckBox checkSab;
    private TipoPeriodo tipoPeriodo;
    private Date dataInicial;
    private Date dataFinal;
    private List<TipoPeriodo> tiposPeriodo;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_vaga_ui);

        etDataInicial = (EditText) findViewById(R.id.et_data_inicial);
        etDataFinal = (EditText) findViewById(R.id.et_data_final);
        etValorPeriodo = (EditText) findViewById(R.id.et_valor_periodo);

        dataInicial = new Date();
        dataFinal = new Date();

        etDataInicial.setText(sdf.format(dataInicial));
        etDataFinal.setText(sdf.format(dataFinal));


        final DatePickerDialog.OnDateSetListener dateInicial = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                try {
                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    calendar.set(java.util.Calendar.YEAR,year);
                    calendar.set(java.util.Calendar.MONTH,month);
                    calendar.set(java.util.Calendar.DAY_OF_MONTH,dayOfMonth);
                    dataInicial = calendar.getTime();
                    etDataInicial.setText(sdf.format(dataInicial));
                }catch (Exception e) {

                }
            }
        };

        final DatePickerDialog.OnDateSetListener dateFinal = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR,year);
                    calendar.set(Calendar.MONTH,month);
                    calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    dataFinal = calendar.getTime();
                    etDataFinal.setText(sdf.format(dataFinal));
                }catch (Exception e) {

                }
            }
        };

        etDataInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendario = Calendar.getInstance();
                int year = calendario.get(Calendar.YEAR);
                int month = calendario.get(Calendar.MONTH);
                int day = calendario.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(CadastrarVagaUi.this,dateInicial,year,month,day);
                dialog.show();
            }
        });

        etDataFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendario = Calendar.getInstance();
                int year = calendario.get(Calendar.YEAR);
                int month = calendario.get(Calendar.MONTH);
                int day = calendario.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(CadastrarVagaUi.this,dateFinal,year,month,day);
                dialog.show();
            }
        });

        tiposPeriodo = Arrays.asList(TipoPeriodo.values());
        spTipoPeriodo = (Spinner) findViewById(R.id.sp_tipo_periodo);
        SpinnerTipoPeriodoAdapter adapter = new SpinnerTipoPeriodoAdapter(this,tiposPeriodo);
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

        checkSeg = (CheckBox) findViewById(R.id.check_seg);
        checkTer = (CheckBox) findViewById(R.id.check_ter);
        checkQua = (CheckBox) findViewById(R.id.check_qua);
        checkQui = (CheckBox) findViewById(R.id.check_qui);
        checkSex = (CheckBox) findViewById(R.id.check_sex);
        checkSab = (CheckBox) findViewById(R.id.check_sab);


        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            uidUsuario = extra.get("uidUsuario").toString();
        }

    }

    public void cadastrarVaga(View v) {
        boolean validacao = true;
        boolean seg = checkSeg.isChecked();
        boolean ter = checkTer.isChecked();
        boolean qua = checkQua.isChecked();
        boolean qui = checkQui.isChecked();
        boolean sex = checkSex.isChecked();
        boolean sab = checkSab.isChecked();
        if(uidUsuario.isEmpty()) {
            Toast.makeText(this,"Usuário não encontrado",Toast.LENGTH_SHORT).show();
            return;
        }
        if(etValorPeriodo.getText().equals("")) {
            Toast.makeText(this,"Preencha o valor do período",Toast.LENGTH_SHORT).show();
            validacao = false;
        }
        if(tipoPeriodo == null) {
            Toast.makeText(this,"Selecione um período",Toast.LENGTH_SHORT).show();
            validacao = false;
        }
        if(!seg && !ter && !qua && !qui && !sex && !sab) {
            Toast.makeText(this,"Selecione pelo menos um dia da semana",Toast.LENGTH_SHORT).show();
            validacao = false;
        }

        if(validacao) {
            JsonObject jsonObjet = new JsonObject();
            jsonObjet.addProperty("uid",uidUsuario);
            jsonObjet.addProperty("valorPeriodo",etValorPeriodo.getText().toString());
            jsonObjet.addProperty("tipoPeriodo",tipoPeriodo.ordinal());
            jsonObjet.addProperty("dataInicial",formatoJson.format(dataInicial));
            jsonObjet.addProperty("dataFinal",formatoJson.format(dataFinal));

            JsonArray arrayDias = new JsonArray();
            if(seg) {
                JsonObject dia = new JsonObject();
                dia.addProperty("id", DiasSemana.SEGUNDA.ordinal());
                arrayDias.add(dia);
            }
            if(ter) {
                JsonObject dia = new JsonObject();
                dia.addProperty("id", DiasSemana.TERCA.ordinal());
                arrayDias.add(dia);
            }
            if(qua) {
                JsonObject dia = new JsonObject();
                dia.addProperty("id", DiasSemana.QUARTA.ordinal());
                arrayDias.add(dia);
            }
            if(qui) {
                JsonObject dia = new JsonObject();
                dia.addProperty("id", DiasSemana.QUINTA.ordinal());
                arrayDias.add(dia);
            }
            if(sex) {
                JsonObject dia = new JsonObject();
                dia.addProperty("id", DiasSemana.SEXTA.ordinal());
                arrayDias.add(dia);
            }
            if(sab) {
                JsonObject dia = new JsonObject();
                dia.addProperty("id", DiasSemana.SABADO.ordinal());
                arrayDias.add(dia);
            }
            jsonObjet.add("diasSelecionados",arrayDias);

            RequestBody body = RequestBody.create(MediaType.parse("json"),jsonObjet.toString());

            Call call = new RetrofitInicializador().getRetrofit().create(VagaService.class).cadastrarVaga(body);
            Callback callback = new RetrofitCallback(this,getString(R.string.cadastrando_vaga),getString(R.string.erro_cadastrar_vaga)){
                @Override
                public void onResponse(Call call, Response response) {
                    if(response.code() == javax.ws.rs.core.Response.Status.OK.getStatusCode()) {
                        Toast.makeText(CadastrarVagaUi.this,getApplicationContext().getString(R.string.vaga_cadastrado_sucesso),Toast.LENGTH_SHORT).show();
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
        else {
            return;
        }

    }

}
