package com.contratediarista.br.contratediarista.fragmento;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.ListaContatoAdapter;
import com.contratediarista.br.contratediarista.entity.Usuario;
import com.contratediarista.br.contratediarista.enuns.TipoUsuario;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.UsuarioService;
import com.contratediarista.br.contratediarista.ui.ConversaActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ListView lvContatos;
    private List<Usuario> usuarios;
    private String tipoUsuario;
    SharedPreferences sharedPreferences;


    public ContatosFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Buscando contatos");
        progressDialog.show();
        String uid = firebaseAuth.getCurrentUser().getUid();
        sharedPreferences = getActivity().getSharedPreferences("informacoes_usuario", Context.MODE_PRIVATE);
        tipoUsuario = sharedPreferences.getString("tipoUsuario","");
        Call call = null;
        if(TipoUsuario.PRESTADOR.getDescricao().equals(tipoUsuario)) {
            call = new RetrofitInicializador().getRetrofit().create(UsuarioService.class).buscarContatosPrestador(uid);
        }
        else if(TipoUsuario.CONTRATANTE.getDescricao().equals(tipoUsuario)) {
            call = new RetrofitInicializador().getRetrofit().create(UsuarioService.class).buscarContatosContratante(uid);
        }
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.code() == javax.ws.rs.core.Response.Status.OK.getStatusCode()) {
                    usuarios = (List<Usuario>) response.body();
                    carregarListaContatos();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("Erro", "onFailure: ", t.getCause());
                progressDialog.dismiss();
            }
        });
        View view  = inflater.inflate(R.layout.fragment_contatos, container, false);

        lvContatos = (ListView) view.findViewById(R.id.lv_contatos);

        lvContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Usuario user = usuarios.get(position);
                Intent intent = new Intent(getActivity(), ConversaActivity.class);
                intent.putExtra("nome",user.getNome());
                intent.putExtra("chave",user.getUid());
                startActivity(intent);

            }
        });

        return view;
    }

    public void carregarListaContatos() {
        ListaContatoAdapter adapter = new ListaContatoAdapter(getActivity(),usuarios);
        lvContatos.setAdapter(adapter);
    }

}
