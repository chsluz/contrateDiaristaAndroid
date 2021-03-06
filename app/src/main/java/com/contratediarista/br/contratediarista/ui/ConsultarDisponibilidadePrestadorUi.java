package com.contratediarista.br.contratediarista.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.contratediarista.br.contratediarista.R;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConsultarDisponibilidadePrestadorUi extends Fragment {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat formatoJson = new SimpleDateFormat("yyyy-MM-dd");
    private EditText etDataInicial;
    private EditText etDataFinal;
    private EditText etValorInicial;
    private EditText etValorFinal;
    private Button btnBuscar;
    private Date dataInicial;
    private Date dataFinal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.consultar_disponibilidade_prestador_ui, container, false);
        etDataInicial = (EditText) view.findViewById(R.id.et_data_inicial);
        etDataFinal = (EditText) view.findViewById(R.id.et_data_final);
        etValorInicial = (EditText) view.findViewById(R.id.et_valor_periodo_inicial);
        etValorFinal = (EditText) view.findViewById(R.id.et_valor_periodo_final);
        btnBuscar = (Button) view.findViewById(R.id.btn_buscar);
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
                DatePickerDialog dialog =
                        new DatePickerDialog(getActivity(),dateInicial,year,month,day);
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
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),dateFinal,year,month,day);
                dialog.show();
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validacao = true;
                if(etValorInicial.getText().equals("")) {
                    validacao = false;
                }
                if(etValorFinal.getText().equals("")) {
                    validacao = false;
                }

                if(validacao) {
                    String json = montarChamada();
                    Bundle bundle = new Bundle();
                    bundle.putString("jsonObject",json);
                    Fragment fragment = new ListarDisponibilidadePrestadorUi();
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                }
            }
        });

        return view;
    }

    public String montarChamada() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dataInicial", formatoJson.format(dataInicial));
        jsonObject.addProperty("dataFinal",formatoJson.format(dataFinal));
        jsonObject.addProperty("valorInicial", etValorInicial.getText().toString());
        jsonObject.addProperty("valorFinal", etValorFinal.getText().toString());
        return jsonObject.toString();
    }
}
