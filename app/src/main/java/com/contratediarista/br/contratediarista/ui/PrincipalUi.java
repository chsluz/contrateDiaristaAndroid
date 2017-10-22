package com.contratediarista.br.contratediarista.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.MenuPrincipalAdapter;
import com.contratediarista.br.contratediarista.entity.Usuario;
import com.contratediarista.br.contratediarista.enuns.TipoUsuario;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.firebase.FirebaseInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.UsuarioService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrincipalUi extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private FirebaseAuth firebaseAuth;
    private Usuario usuario;
    private ListView lvMenuPrincipal;
    private List<String> menus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_ui);
        firebaseAuth = FirebaseInicializador.getFirebaseAuth();
        lvMenuPrincipal = (ListView) findViewById(R.id.lv_menu_principal);
        lvMenuPrincipal.setOnItemClickListener(this);

        Call call = new RetrofitInicializador().getRetrofit().create(UsuarioService.class).buscarPorUid(firebaseAuth.getCurrentUser().getUid());
        RetrofitCallback callback = new RetrofitCallback(this, getString(R.string.buscando_informacoes_usuario), getString(R.string.erro_buscar_informacoes_usuario)) {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == javax.ws.rs.core.Response.Status.OK.getStatusCode()) {
                    usuario = (Usuario) response.body();
                    carregarMenuPrincipal();
                }
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
            }
        };
        call.enqueue(callback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sair) {
            firebaseAuth.signOut();
            Intent intent = new Intent(PrincipalUi.this, LoginUi.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void carregarMenuPrincipal() {
        menus = new ArrayList<>();
        SharedPreferences.Editor preferences = getSharedPreferences("informacoes_usuario", MODE_PRIVATE).edit();
        preferences.putString("uidUsuario",usuario.getUid());
        preferences.putString("nome", usuario.getNome());
        preferences.putString("tipoUsuario", usuario.getTipoUsuario().getDescricao());
        preferences.commit();
        if (usuario.getTipoUsuario().equals(TipoUsuario.CONTRATANTE)) {
            menus.add("Cadastrar nova vaga");
            menus.add("Cadastrar tipo atividade");

            menus.add("Consultar aprovação de vagas");
            menus.add("Consultar Disponibilidade prestador");
            menus.add("Consultar calendário");
            menus.add("Mensagens");

        } else if (usuario.getTipoUsuario().equals(TipoUsuario.PRESTADOR)) {
            menus.add("Cadastrar disponibilidade");
            menus.add("Cadastrar tipo atividade");

            menus.add("Consultar vagas");
            menus.add("Consultar vagas vinculadas");
            menus.add("Consultar disponibilidade");

            menus.add("Mensagens");

        }

        MenuPrincipalAdapter adapter = new MenuPrincipalAdapter(this,menus);
        lvMenuPrincipal.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        if (usuario.getTipoUsuario().equals(TipoUsuario.PRESTADOR)) {
            if(position == 0) {
                Intent intent = new Intent(PrincipalUi.this,CadastrarDisponibilidadeUi.class);
                intent.putExtra("uidUsuario", usuario.getUid());
                startActivity(intent);
            }
            else if(position == 1) {
                Intent intent = new Intent(PrincipalUi.this,CadastrarTipoAtividadeUi.class);
                startActivity(intent);
            }
            else if(position == 2) {
                Intent intent = new Intent(PrincipalUi.this,ConsultarVagasUi.class);
                startActivity(intent);
            }
            else if(position == 3) {
                Intent intent = new Intent(PrincipalUi.this,VisualizarVagasVinculadasUi.class);
                startActivity(intent);
            }
            else if(position == 4) {
                Intent intent = new Intent(PrincipalUi.this,ConsultarDisponibilidade.class) ;
                startActivity(intent);
            }
            else if (position == 5) {
                Intent intent = new Intent(PrincipalUi.this, MensagensUi.class);
                startActivity(intent);
            }
        } else {
            if (position == 0) {
                Intent intent = new Intent(PrincipalUi.this, CadastrarVagaUi.class);
                intent.putExtra("uidUsuario", usuario.getUid());
                startActivity(intent);
            }
            else if(position == 1) {
                Intent intent = new Intent(PrincipalUi.this,CadastrarTipoAtividadeUi.class);
                startActivity(intent);
            }else if(position == 2) {
                Intent intent = new Intent(PrincipalUi.this,AprovacaoVagasUi.class);
                startActivity(intent);
            } else if(position == 3) {
                Intent intent = new Intent(PrincipalUi.this,ConsultarDisponibilidadePrestadorUi.class);
                startActivity(intent);
            }
            else if (position == 5) {
                Intent intent = new Intent(PrincipalUi.this, MensagensUi.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
    }
}
