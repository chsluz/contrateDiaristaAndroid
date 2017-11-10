package com.contratediarista.br.contratediarista.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.entity.Usuario;

import java.util.List;

/**
 * Created by manga on 22/10/2017.
 */

public class UsuarioAdapter extends BaseAdapter {
    private Activity activity;
    private List<Usuario> usuarios;

    public UsuarioAdapter(Activity activity,List<Usuario> usuarios) {
        this.activity = activity;
        this.usuarios = usuarios;
    }

    @Override
    public int getCount() {
        return usuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return usuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Usuario usuario = usuarios.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
        View linha = inflater.inflate(R.layout.usuario_ui_linha,null);
        TextView tvNome = (TextView) linha.findViewById(R.id.tv_nome);
        tvNome.setText("Nome: "+usuario.getNome());

        TextView tvAvaliacao = (TextView) linha.findViewById(R.id.tv_avaliacao);
        tvAvaliacao.setText("Avaliação: " +usuario.getMediaAprovacaoUsuario());

        TextView tvQtdeAvaliacao = (TextView) linha.findViewById(R.id.tv_qtde_avaliacao);
        tvQtdeAvaliacao.setText("Qtde Avaliação: "+usuario.getQuantidadeAvaliacoesUsuario());
        return linha;
    }
}
