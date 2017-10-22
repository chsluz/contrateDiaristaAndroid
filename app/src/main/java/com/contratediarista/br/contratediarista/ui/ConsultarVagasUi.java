package com.contratediarista.br.contratediarista.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.SpinnerTipoPeriodoAdapter;
import com.contratediarista.br.contratediarista.enuns.DiasSemana;
import com.contratediarista.br.contratediarista.enuns.TipoPeriodo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ConsultarVagasUi extends AppCompatActivity {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat formatoJson = new SimpleDateFormat("yyyy-MM-dd");
    private EditText etDataInicial;
    private EditText etDataFinal;
    private EditText etValorInicial;
    private EditText etValorFinal;
    private Spinner spTipoPeriodo;
    private CheckBox checkSeg;
    private CheckBox checkTer;
    private CheckBox checkQua;
    private CheckBox checkQui;
    private CheckBox checkSex;
    private CheckBox checkSab;
    private List<TipoPeriodo> tiposPeriodo;
    private List<DiasSemana> diasSelecionados;
    private TipoPeriodo tipoPeriodo;
    private Date dataInicial;
    private Date dataFinal;
    private Button btnBuscarVaga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultar_vagas_ui);
        dataInicial = new Date();
        dataFinal = new Date();
        tiposPeriodo = Arrays.asList(TipoPeriodo.values());
        etDataInicial = (EditText) findViewById(R.id.et_data_inicial);
        etDataInicial.setText(sdf.format(dataInicial));
        etDataFinal = (EditText) findViewById(R.id.et_data_final);
        etDataFinal.setText(sdf.format(dataFinal));
        etValorInicial = (EditText)findViewById(R.id.et_valor_inicial);
        etValorFinal = (EditText) findViewById(R.id.et_valor_final);
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
        btnBuscarVaga = (Button) findViewById(R.id.btn_buscar_vaga);
        btnBuscarVaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarCheckboxSelecionados();
                boolean validacao = true;
                if(etValorInicial.getText().equals("")) {
                    Toast.makeText(ConsultarVagasUi.this,"Preencha o campo valor inicial",Toast.LENGTH_SHORT).show();
                    validacao = false;
                }
                if(etValorFinal.getText().equals("")) {
                    Toast.makeText(ConsultarVagasUi.this,"Preencha o campo valor final",Toast.LENGTH_SHORT).show();
                    validacao = false;
                }
                if(tipoPeriodo == null) {
                    Toast.makeText(ConsultarVagasUi.this,"Selecione o período",Toast.LENGTH_SHORT).show();
                    validacao = false;
                }
                if(diasSelecionados.isEmpty()) {
                    Toast.makeText(ConsultarVagasUi.this,"Selecione pelo menos um dia da semana",Toast.LENGTH_SHORT).show();
                    validacao = false;
                }
                if(validacao) {
                    JsonObject jsonObjet = new JsonObject();
                    jsonObjet.addProperty("valorInicial",etValorInicial.getText().toString());
                    jsonObjet.addProperty("valorFinal",etValorFinal.getText().toString());
                    jsonObjet.addProperty("periodo",tipoPeriodo.ordinal());
                    jsonObjet.addProperty("dataInicial",formatoJson.format(dataInicial));
                    jsonObjet.addProperty("dataFinal",formatoJson.format(dataFinal));

                    JsonArray arrayDias = new JsonArray();
                    for(DiasSemana dia : diasSelecionados) {
                        JsonObject jsonDia = new JsonObject();
                        jsonDia.addProperty("diaSemana", dia.ordinal());
                        arrayDias.add(jsonDia);
                    }
                    jsonObjet.add("diasSelecionados",arrayDias);
                    Intent intent = new Intent(ConsultarVagasUi.this,VagasUi.class);
                    intent.putExtra("jsonObject",jsonObjet.toString());
                    startActivity(intent);
                }

            }
        });



        final DatePickerDialog.OnDateSetListener dateInicial = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR,year);
                    calendar.set(Calendar.MONTH,month);
                    calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
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
                DatePickerDialog dialog = new DatePickerDialog(ConsultarVagasUi.this,dateInicial,year,month,day);
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
                DatePickerDialog dialog = new DatePickerDialog(ConsultarVagasUi.this,dateFinal,year,month,day);
                dialog.show();
            }
        });
    }

    public void verificarCheckboxSelecionados() {
        diasSelecionados = new ArrayList<>();
        if(checkSeg.isChecked()) {
            diasSelecionados.add(DiasSemana.SEGUNDA);
        }
        if(checkTer.isChecked()) {
            diasSelecionados.add(DiasSemana.TERCA);
        }
        if(checkQua.isChecked()) {
            diasSelecionados.add(DiasSemana.QUARTA);
        }
        if(checkQui.isChecked()) {
            diasSelecionados.add(DiasSemana.QUINTA);
        }
        if(checkSex.isChecked()) {
            diasSelecionados.add(DiasSemana.SEXTA);
        }
        if(checkSab.isChecked()) {
            diasSelecionados.add(DiasSemana.SABADO);
        }
    }


}
