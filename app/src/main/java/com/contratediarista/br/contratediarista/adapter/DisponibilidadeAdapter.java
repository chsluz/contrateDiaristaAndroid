package com.contratediarista.br.contratediarista.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.entity.Disponibilidade;
import com.contratediarista.br.contratediarista.entity.TipoAtividade;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by manga on 21/10/2017.
 */

public class DisponibilidadeAdapter extends BaseAdapter {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private Activity activity;
    private List<Disponibilidade> disponibilidades;

    public DisponibilidadeAdapter(Activity activity,List<Disponibilidade> disponibilidades) {
        this.activity = activity;
        this.disponibilidades = disponibilidades;
    }

    @Override
    public int getCount() {
        return disponibilidades.size();
    }

    @Override
    public Object getItem(int position) {
        return disponibilidades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return disponibilidades.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Disponibilidade disponibilidade = disponibilidades.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
        View linha = inflater.inflate(R.layout.disponibilidade_ui_linha,null);

        float scale = activity.getResources().getDisplayMetrics().density;
        float scaleSp = activity.getResources().getDisplayMetrics().scaledDensity;

        LinearLayout linear = (LinearLayout) linha.findViewById(R.id.linear_disponibilidade);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins((int) (scale * 10),(int) (scale*3),0,0);

        TextView tvData = new TextView(activity);
        tvData.setText("Data: " + sdf.format(disponibilidade.getData()));
        tvData.setLayoutParams(params);
        tvData.setTextSize(10*scaleSp);
        linear.addView(tvData);

        TextView tvPeriodo = new TextView(activity);
        tvPeriodo.setText("Período: " + disponibilidade.getTipoPeriodo().getDescricao());
        tvPeriodo.setLayoutParams(params);
        tvPeriodo.setTextSize(10*scaleSp);
        linear.addView(tvPeriodo);

        TextView tvValor = new TextView(activity);
        tvValor.setText("Valor: " + disponibilidade.getValorPeriodo() +" R$");
        tvValor.setLayoutParams(params);
        tvValor.setTextSize(10*scaleSp);
        linear.addView(tvValor);

        for(TipoAtividade tipo : disponibilidade.getTiposAtividade()) {
            TextView tvAtividade = new TextView(activity);
            tvAtividade.setText("Atividade: " + tipo.getDescricao());
            tvAtividade.setLayoutParams(params);
            tvAtividade.setTextSize(9*scaleSp);
            linear.addView(tvAtividade);
        }

        return linha;
    }
}
