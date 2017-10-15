package com.contratediarista.br.contratediarista.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.contratediarista.br.contratediarista.fragmento.ContatosFragment;
import com.contratediarista.br.contratediarista.fragmento.ConversasFragment;

/**
 * Created by manga on 14/10/2017.
 */

public class TabAdapter extends FragmentStatePagerAdapter {
    private String[] tituloAbas = {"CONVERSAS","CONTATOS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case  0:
                fragment = new ConversasFragment();
                break;
            case 1:
                fragment = new ContatosFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tituloAbas[position];
    }
}
