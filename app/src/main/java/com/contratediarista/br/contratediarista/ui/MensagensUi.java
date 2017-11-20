package com.contratediarista.br.contratediarista.ui;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.TabAdapter;
import com.contratediarista.br.contratediarista.helper.SlidingTabLayout;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MensagensUi extends Fragment {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mensagens_ui, container, false);
        slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.stl_tabs);
        viewPager = (ViewPager) view.findViewById(R.id.vp_paginas);
        TabAdapter adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        slidingTabLayout.setViewPager(viewPager);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(getActivity(),R.color.colorAccent));
        return view;
    }
}
