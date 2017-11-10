package com.contratediarista.br.contratediarista.ui;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.MenuAdapter;
import com.contratediarista.br.contratediarista.entity.Usuario;
import com.contratediarista.br.contratediarista.enuns.TipoUsuario;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.firebase.FirebaseInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.UsuarioService;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FirebaseAuth firebaseAuth;
    private ListView lvMenu;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] menus;
    private Usuario usuario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        firebaseAuth = FirebaseInicializador.getFirebaseAuth();

        Call call = new RetrofitInicializador().getRetrofit().create(UsuarioService.class).buscarPorUid(firebaseAuth.getCurrentUser().getUid());
        RetrofitCallback callback = new RetrofitCallback(this, getString(R.string.buscando_informacoes_usuario), getString(R.string.erro_buscar_informacoes_usuario)) {
            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);
                if (response.code() == javax.ws.rs.core.Response.Status.OK.getStatusCode()) {
                    usuario = (Usuario) response.body();
                    carregarMenuPrincipal();
                }
                if(response.code() == 204) {
                    MenuActivity.super.onBackPressed();
                    Toast.makeText(MenuActivity.this,"Usuário não cadastrado no sistema",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                MenuActivity.super.onBackPressed();
            }
        };
        call.enqueue(callback);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        lvMenu = (ListView) findViewById(R.id.lv_menu);

        if (savedInstanceState == null) {
            //positionSelected(0);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                null, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };


        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(usuario != null) {
            if (usuario.getTipoUsuario().equals(TipoUsuario.CONTRATANTE)) {
                inflater.inflate(R.menu.menu_contratante, menu);
            } else {
                inflater.inflate(R.menu.menu_prestador, menu);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(lvMenu);
        if(usuario != null) {
            if (usuario.getTipoUsuario().equals(TipoUsuario.CONTRATANTE)) {
                menu.findItem(R.id.menu_cadastrar_vaga).setVisible(!drawerOpen);
            } else {
                menu.findItem(R.id.menu_cadastrar_disponibilidade).setVisible(!drawerOpen);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void carregarMenuPrincipal() {
        String[] menusContratante = {"Cadastrar Vaga","Aprovação de vaga","Sair"};
        String[] menusPrestador = {"Cadastrar Disponibilidade","Sair"};
        if(usuario.getTipoUsuario().equals(TipoUsuario.CONTRATANTE)) {
            menus = menusContratante;
        }
        else {
            menus = menusPrestador;
        }
        MenuAdapter adapter = new MenuAdapter(this, Arrays.asList(menus));
        lvMenu.setAdapter(adapter);
        lvMenu.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        positionSelected(position);
    }

    public void positionSelected(int position) {
        FragmentTransaction ft;
        Fragment fragment;
        if(usuario.getTipoUsuario().equals(TipoUsuario.CONTRATANTE)) {
            if (position == 0) {
                fragment = new CadastrarVagaUi();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            } else if (position == 1) {
                fragment = new AprovacaoVagasUi();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            } else if (position == 2) {
                firebaseAuth.signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(MenuActivity.this, LoginUi.class);
                startActivity(intent);
            }
        }
        else {
            if (position == 2) {
                firebaseAuth.signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(MenuActivity.this, LoginUi.class);
                startActivity(intent);
            }
        }
        lvMenu.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(lvMenu);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public void onBackPressed() {
    }

}

