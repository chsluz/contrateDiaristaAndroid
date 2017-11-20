package com.contratediarista.br.contratediarista.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.entity.Usuario;
import com.contratediarista.br.contratediarista.enuns.TipoUsuario;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.firebase.FirebaseInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.UsuarioService;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Response;

public class MenuDrawerUi extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Usuario usuario;
    private FirebaseAuth firebaseAuth;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseInicializador.getFirebaseAuth();
        setContentView(R.layout.menu_drawer_ui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(!usuarioJaLogado()) {
            carregarInformacoesDoUsuario();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        inflateMenuNavigation();
    }


    private void carregarInformacoesDoUsuario() {
        Call call = new RetrofitInicializador()
                .getRetrofit()
                .create(UsuarioService.class)
                .buscarPorUid(firebaseAuth.getCurrentUser().getUid());
        RetrofitCallback callback = new RetrofitCallback(this,
                getString(R.string.buscando_informacoes_usuario),
                getString(R.string.erro_buscar_informacoes_usuario)) {
            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);
                if (response.code() == javax.ws.rs.core.Response.Status.OK.getStatusCode()) {
                    usuario = (Usuario) response.body();
                    inflateMenuNavigation();
                }
                if (response.code() == 204) {
                    MenuDrawerUi.super.onBackPressed();
                    Toast.makeText(MenuDrawerUi.this,
                            "Usuário não cadastrado no sistema",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                MenuDrawerUi.super.onBackPressed();
            }
        };
        call.enqueue(callback);

    }

    private boolean usuarioJaLogado() {
        SharedPreferences sharedPreferences = getSharedPreferences("informacoes_usuario", MODE_PRIVATE);
        if(!sharedPreferences.getString("uidUsuario","").isEmpty()){
            usuario = new Usuario();
            usuario.setUid(sharedPreferences.getString("uidUsuario",""));
            usuario.setNome(sharedPreferences.getString("nome",""));
            usuario.setTipoUsuario(TipoUsuario.getTipoUsuario(sharedPreferences.getString("tipoUsuario","")));
            return true;
        }
        else {
            return false;
        }
    }


    public void inflateMenuNavigation() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (usuario != null) {
            SharedPreferences.Editor preferences = getSharedPreferences("informacoes_usuario", MODE_PRIVATE).edit();
            preferences.putString("uidUsuario", usuario.getUid());
            preferences.putString("nome", usuario.getNome());
            preferences.putString("tipoUsuario", usuario.getTipoUsuario().getDescricao());
            preferences.commit();
        }

        View header = navigationView.getHeaderView(0);
        TextView tvEmail = (TextView) header.findViewById(R.id.tv_email);
        String email = firebaseAuth.getCurrentUser().getEmail();
        if (!email.isEmpty() && tvEmail != null) {
            tvEmail.setText(email);
        }
        if (usuario != null && usuario.getTipoUsuario().equals(TipoUsuario.CONTRATANTE)) {
            navigationView.inflateMenu(R.menu.menu_contratante);
        } else if (usuario != null && usuario.getTipoUsuario().equals(TipoUsuario.PRESTADOR)) {
            navigationView.inflateMenu(R.menu.menu_prestador);
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.menu_cadastrar_vaga) {
            getSupportActionBar().setTitle("Cadastrar Vaga");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new CadastrarVagaUi()).commit();
        }
        else if(id == R.id.menu_cadastrar_tipo_atividade_contratante) {
            getSupportActionBar().setTitle("Cadastrar Tipo de atividade");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new CadastrarTipoAtividadeUi()).commit();
        }
        else if(id == R.id.menu_vagas_cadastradas_contratante) {
            getSupportActionBar().setTitle("Vagas Cadastradas");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,new ConsultarVagaCadastradaContratanteUi()).commit();
        }
        else if (id == R.id.menu_aprovacao_vaga) {
            getSupportActionBar().setTitle("Aprovação de vagas");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AprovacaoVagasUi()).commit();
        }
        else if(id == R.id.menu_agenda_contratante) {
            getSupportActionBar().setTitle("Agenda");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new VisualizarVagasAprovadasUi()).commit();
        }
        else if(id == R.id.menu_consultar_disponibilidade_prestador) {
            getSupportActionBar().setTitle("Consultar Disponibilidade do Prestador");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ConsultarDisponibilidadePrestadorUi()).commit();
        }
        else if(id == R.id.menu_mensagens_contratante) {
            getSupportActionBar().setTitle("Mensagens");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new MensagensUi()).commit();
        }
        else if(id == R.id.menu_cadastrar_disponibilidade) {
            getSupportActionBar().setTitle("Cadastrar disponibilidade");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new CadastrarDisponibilidadeUi()).commit();
        }
        else if(id == R.id.menu_cadastrar_tipo_atividade_prestador) {
            getSupportActionBar().setTitle("Cadastrar tipo atividade");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new CadastrarTipoAtividadeUi()).commit();
        }else if(id == R.id.menu_consultar_vagas_prestador) {
            getSupportActionBar().setTitle("Consultar vaga");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new ConsultarVagasUi()).commit();
        }else if(id == R.id.menu_consultar_vagas_pretendidas) {
            getSupportActionBar().setTitle("Consultar vagas pretendidas");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new VisualizarVagasJaCandidatouUi()).commit();
        }else if(id == R.id.menu_agenda_prestador) {
            getSupportActionBar().setTitle("Agenda");
            Fragment fragment = new VisualizarVagasVinculadasUi();
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
        }else if(id == R.id.menu_consultar_disponibilidade) {
            getSupportActionBar().setTitle("Consultar Disponibilidade");
            Fragment fragment = new ConsultarDisponibilidade();
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
        }
        else if(id == R.id.menu_mensagens_prestador) {
            getSupportActionBar().setTitle("Mensagens");
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new MensagensUi()).commit();
        }
        else if (id == R.id.sair) {
            firebaseAuth.signOut();
            LoginManager.getInstance().logOut();
            limparDadosDoUsuarioNoAparelho();
            Intent intent = new Intent(MenuDrawerUi.this, LoginUi.class);
            startActivity(intent);
        }
        DrawerLayout drawer;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void limparDadosDoUsuarioNoAparelho() {
        SharedPreferences.Editor editor = getSharedPreferences("informacoes_usuario", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
}
