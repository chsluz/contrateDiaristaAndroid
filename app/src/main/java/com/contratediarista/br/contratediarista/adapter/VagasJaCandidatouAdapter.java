package com.contratediarista.br.contratediarista.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.entity.Rotina;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by manga on 19/11/2017.
 */

public class VagasJaCandidatouAdapter extends BaseAdapter {
    private Activity activity;
    private List<Rotina> rotinas;

    public VagasJaCandidatouAdapter(Activity activity, List<Rotina> rotinas) {
        this.activity = activity;
        this.rotinas = rotinas;
    }

    @Override
    public int getCount() {
        return rotinas.size();
    }

    @Override
    public Object getItem(int i) {
        return rotinas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return rotinas.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Rotina rotina = rotinas.get(i);
        LayoutInflater inflater = activity.getLayoutInflater();
        View linha = inflater.inflate(R.layout.vagas_ja_candidatou_ui_linha,null);
        TextView tvNome = (TextView) linha.findViewById(R.id.tv_nome);
        tvNome.setText("Nome: " + rotina.getVaga().getContratante().getNome());

        TextView tvData = (TextView) linha.findViewById(R.id.tv_data);
        tvData.setText(new SimpleDateFormat("dd/MM/yyyy").format(rotina.getData()));

        TextView tvPeriodo = (TextView) linha.findViewById(R.id.tv_periodo);
        tvPeriodo.setText("Período: " + rotina.getVaga().getTipoPeriodo().getDescricao());

        TextView tvValor = (TextView) linha.findViewById(R.id.tv_valor);
        tvValor.setText("Valor: " + String.valueOf(rotina.getVaga().getValorPeriodo()));
        return linha;
    }
}
