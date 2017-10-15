package com.contratediarista.br.contratediarista.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.enuns.TipoPeriodo;

import java.util.List;

/**
 * Created by manga on 15/10/2017.
 */

public class SpinnerTipoPeriodoAdapter extends BaseAdapter {
    private Activity activity;
    private List<TipoPeriodo> tipoPeriodos;

    public SpinnerTipoPeriodoAdapter(Activity activity, List<TipoPeriodo> tipoPeriodos) {
        this.tipoPeriodos = tipoPeriodos;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return tipoPeriodos.size();
    }

    @Override
    public Object getItem(int position) {
        return tipoPeriodos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tipoPeriodos.get(position).ordinal();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TipoPeriodo tipo = tipoPeriodos.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
        View linha = inflater.inflate(R.layout.spinner_tipo_periodo,null) ;
        TextView tvTipoPeriodo = (TextView) linha.findViewById(R.id.tv_tipo_periodo);
        tvTipoPeriodo.setText(tipo.getDescricao());
        return linha;
    }
}
