package com.contratediarista.br.contratediarista.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.entity.TipoAtividade;

import java.util.List;

/**
 * Created by manga on 20/10/2017.
 */

public class ListaAtividadesAdapter extends BaseAdapter {
    private Activity activity;
    private List<TipoAtividade> tiposAtividade;
    public ListaAtividadesAdapter(Activity activity,List<TipoAtividade> tipoAtividades) {
        this.activity = activity;
        this.tiposAtividade = tipoAtividades;
    }

    @Override
    public int getCount() {
        return tiposAtividade.size();
    }

    @Override
    public Object getItem(int position) {
        return tiposAtividade.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tiposAtividade.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TipoAtividade tipoAtividade = tiposAtividade.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
        View linha = inflater.inflate(R.layout.lista_atividade_ui_linha,null);
        TextView tvDescricao = (TextView) linha.findViewById(R.id.tv_descricao);
        tvDescricao.setText(tipoAtividade.getDescricao());
        return linha;
    }
}
