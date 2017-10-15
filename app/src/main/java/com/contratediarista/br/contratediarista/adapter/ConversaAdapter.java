package com.contratediarista.br.contratediarista.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.entity.Conversa;
import com.contratediarista.br.contratediarista.entity.Mensagem;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * Created by manga on 14/10/2017.
 */

public class ConversaAdapter extends BaseAdapter {
    private Activity activity;
    private List<Conversa> conversas;

    public ConversaAdapter(Activity activity,List<Conversa> conversas) {
        this.activity = activity;
        this.conversas = conversas;
    }

    @Override
    public int getCount() {
        return conversas.size();
    }

    @Override
    public Object getItem(int position) {
        return conversas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Conversa conversa = conversas.get(position);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        LayoutInflater inflater = activity.getLayoutInflater();
        View linha = inflater.inflate(R.layout.lista_conversas, parent, false);

        TextView tvNome = (TextView) linha.findViewById(R.id.tv_nome);
        tvNome.setText(conversa.getNome());

        TextView tvMensagem = (TextView) linha.findViewById(R.id.tv_mensagem);
        tvMensagem.setText(conversa.getMensagem());

        return linha;
    }
}
