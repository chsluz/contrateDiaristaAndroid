package com.contratediarista.br.contratediarista.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.contratediarista.br.contratediarista.R;

import java.util.List;

/**
 * Created by manga on 21/10/2017.
 */

public class MenuPrincipalAdapter extends BaseAdapter {
    private Activity activity;
    private List<String> menus;

    public MenuPrincipalAdapter(Activity activity, List<String> menus) {
        this.activity = activity;
        this.menus = menus;
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int position) {
        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String menu = menus.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
        View linha = inflater.inflate(R.layout.principal_ui_linha,null);
        TextView tvMenu = (TextView) linha.findViewById(R.id.tv_principal);
        tvMenu.setText(menu);
        return linha;
    }
}
