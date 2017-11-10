package com.contratediarista.br.contratediarista.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.entity.Rotina;
import com.contratediarista.br.contratediarista.entity.TipoAtividade;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by manga on 22/10/2017.
 */

public class AprovacaoVagasAdapter extends BaseAdapter {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private Activity activity;
    private List<Rotina> rotinas;

    public AprovacaoVagasAdapter(Activity activity,List<Rotina> rotinas) {
        this.activity = activity;
        this.rotinas = rotinas;
    }

    @Override
    public int getCount() {
        return rotinas.size();
    }

    @Override
    public Object getItem(int position) {
        return rotinas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rotinas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Rotina rotina = rotinas.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
        View linha = inflater.inflate(R.layout.aprovacao_vaga_ui_linha,null);

        TextView tvDataVagaCadastrada = (TextView) linha.findViewById(R.id.tv_data_vaga_cadastrada);
        tvDataVagaCadastrada.setText("Data: "+sdf.format(rotina.getData()));

        TextView tvPeriodoCadastrado = (TextView) linha.findViewById(R.id.tv_periodo_vaga);
        tvPeriodoCadastrado.setText("Período: "+rotina.getVaga().getTipoPeriodo().getDescricao());

        TextView tvDiaSemana = (TextView) linha.findViewById(R.id.tv_dia_da_semana);
        tvDiaSemana.setText("Dia: "+rotina.getDiaSemana().getDescricao());

        TextView tvValor = (TextView) linha.findViewById(R.id.tv_valor_periodo);
        tvValor.setText("Valor: "+rotina.getVaga().getValorPeriodo());

        TextView tvCandidatos = (TextView) linha.findViewById(R.id.tv_canditatos);
        tvCandidatos.setText("Candidatos: "+ (!rotina.getPrestadores().isEmpty() ? "Sim" : "Não") );

        TextView tvSelecionado = (TextView) linha.findViewById(R.id.tv_selecionado);
        tvSelecionado.setText("Selecionado: "+ (rotina.getPrestadorSelecionado() != null ? "Sim" : "Não"));

        return linha;
    }
}
