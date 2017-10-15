package com.contratediarista.br.contratediarista.adapter;

import android.app.Activity;
import android.content.Context;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.entity.Mensagem;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * Created by manga on 14/10/2017.
 */

public class MensagemAdapter extends BaseAdapter {
    private Activity activity;
    private List<Mensagem> mensagens;

    public MensagemAdapter(Activity activity,List<Mensagem> mensagens) {
        this.activity = activity;
        this.mensagens = mensagens;
    }

    @Override
    public int getCount() {
        return mensagens.size();
    }

    @Override
    public Object getItem(int position) {
        return mensagens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Mensagem mensagem = mensagens.get(position);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        LayoutInflater inflater = activity.getLayoutInflater();
        View linha = null;
        if(auth.getCurrentUser().getUid().equals(mensagem.getIdUsuario())) {
            linha = inflater.inflate(R.layout.item_mensagem_direita, parent, false);
        }
        else {
            linha = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
        }
        TextView tvMensagem = (TextView) linha.findViewById(R.id.tv_mensagem);
        tvMensagem.setText(mensagem.getMensagem());

        return linha;
    }
}
