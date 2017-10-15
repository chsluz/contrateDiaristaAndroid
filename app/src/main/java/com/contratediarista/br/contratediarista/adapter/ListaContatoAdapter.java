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
 * Created by manga on 14/10/2017.
 */

public class ListaContatoAdapter extends BaseAdapter {
    private Activity activity;
    private List<Usuario> usuarios;

    public ListaContatoAdapter(Activity activity, List<Usuario> usuarios) {
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
        LayoutInflater inflater = LayoutInflater.from(activity);
        View linha = inflater.inflate(R.layout.lista_contato,null);

        TextView tvNome = (TextView) linha.findViewById(R.id.tv_nome);
        tvNome.setText(usuario.getNome());
        return linha;
    }
}
