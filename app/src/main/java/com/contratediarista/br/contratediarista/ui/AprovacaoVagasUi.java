package com.contratediarista.br.contratediarista.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.contratediarista.br.contratediarista.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AprovacaoVagasUi extends AppCompatActivity {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat formatoJson = new SimpleDateFormat("yyyy-MM-dd");
    private EditText etDataInicial;
    private EditText etDataFinal;
    private Button btnBuscar;
    private Date dataInicial;
    private Date dataFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aprovacao_vagas_ui);
        etDataInicial = (EditText) findViewById(R.id.et_data_inicial);
        etDataFinal = (EditText) findViewById(R.id.et_data_final);
        btnBuscar = (Button) findViewById(R.id.btn_buscar);

        dataInicial = new Date();
        dataFinal = new Date();
        etDataInicial.setText(sdf.format(dataInicial));
        etDataFinal.setText(sdf.format(dataFinal));

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
                DatePickerDialog dialog = new DatePickerDialog(AprovacaoVagasUi.this,dateInicial,year,month,day);
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
                DatePickerDialog dialog = new DatePickerDialog(AprovacaoVagasUi.this,dateFinal,year,month,day);
                dialog.show();
            }
        });




        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(AprovacaoVagasUi.this,VisualizarAprovacaoVagaUi.class);
                intent.putExtra("dataInicial",formatoJson.format(dataInicial));
                intent.putExtra("dataFinal",formatoJson.format(dataFinal));
                startActivity(intent);
            }
        });


    }
}
