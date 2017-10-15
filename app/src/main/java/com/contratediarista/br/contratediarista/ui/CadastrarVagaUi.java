package com.contratediarista.br.contratediarista.ui;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.contratediarista.br.contratediarista.R;

import java.text.SimpleDateFormat;
import java.util.Date;

@TargetApi(24)
public class CadastrarVagaUi extends AppCompatActivity {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String uidUsuario = "";
    private EditText etDataInicial;
    private EditText etDataFinal;
    private Date dataInicial;
    private Date dataFinal;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_vaga_ui);

        etDataInicial = (EditText) findViewById(R.id.et_data_inicial);
        etDataFinal = (EditText) findViewById(R.id.et_data_final);

        dataInicial = new Date();
        dataFinal = new Date();

        etDataInicial.setText(sdf.format(dataInicial));
        etDataFinal.setText(sdf.format(dataFinal));

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            uidUsuario = extra.get("uidUsuario").toString();
        }

    }
}
