package com.contratediarista.br.contratediarista.ui;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.MensagemAdapter;
import com.contratediarista.br.contratediarista.entity.Conversa;
import com.contratediarista.br.contratediarista.entity.Mensagem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConversaActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference;
    private String nome;
    private String nomeUsuarioLogado;
    private String uidDestinatario;
    private String uidRemetente;
    private EditText etMensagem;
    private ImageButton imgEnviar;
    private ListView lvConversas;
    private ArrayList<Mensagem> mensagens;
    MensagemAdapter adapter;
    private ValueEventListener valueEventListener;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);
        sharedPreferences = getSharedPreferences("informacoes_usuario",MODE_PRIVATE);
        nomeUsuarioLogado = sharedPreferences.getString("nome","");

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            nome = extra.get("nome").toString();
            uidDestinatario = extra.get("chave").toString();
        }
        uidRemetente = firebaseAuth.getCurrentUser().getUid();
        getSupportActionBar().setTitle(nome);
        getSupportActionBar().setIcon(R.drawable.ic_action_name);
        etMensagem = (EditText) findViewById(R.id.et_conversas);
        imgEnviar = (ImageButton) findViewById(R.id.btn_enviar_mensagem);
        lvConversas = (ListView) findViewById(R.id.lv_conversas);

        mensagens = new ArrayList<>();
        adapter = new MensagemAdapter(ConversaActivity.this,mensagens);
        lvConversas.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("mensagens")
                .child(uidRemetente)
                .child(uidDestinatario);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mensagens.clear();
                for(DataSnapshot dados : dataSnapshot.getChildren()) {
                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(valueEventListener);

        imgEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etMensagem.getText().toString();
                if (msg.isEmpty()) {
                    return;
                } else {
                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(uidRemetente);
                    mensagem.setMensagem(msg);

                    boolean retornoRemetente = salvarMensagemRemetente(mensagem);
                    if(!retornoRemetente) {
                        Toast.makeText(ConversaActivity.this,"Erro ao enviar mensagem, tente novamente",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        boolean retornoDestinatario = salvarMensagemDestinatario(mensagem);
                    }
                    Conversa conversa = new Conversa();
                    conversa.setIdUsuario(uidDestinatario);
                    conversa.setNome(nome);
                    conversa.setMensagem(mensagem.getMensagem());
                    boolean retorno = salvarConversaRemetente(conversa);
                    if(!retorno) {
                        Toast.makeText(ConversaActivity.this,"Erro ao salvar conversa, tente novamente",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        conversa = new Conversa();
                        conversa.setIdUsuario(uidRemetente);
                        conversa.setNome(nomeUsuarioLogado);
                        conversa.setMensagem(mensagem.getMensagem());
                        salvarConversaDestinatario(conversa);
                    }

                    etMensagem.setText("");
                }
            }
        });
    }


    private boolean salvarConversaRemetente(Conversa conversa) {
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("conversas");
            databaseReference.child(uidRemetente)
                    .child(uidDestinatario)
                    .setValue(conversa);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean salvarConversaDestinatario(Conversa conversa) {
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("conversas");
            databaseReference.child(uidDestinatario)
                    .child(uidRemetente)
                    .setValue(conversa);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean salvarMensagemRemetente(Mensagem mensagem) {
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("mensagens");
            databaseReference.child(uidRemetente)
                    .child(uidDestinatario)
                    .push()
                    .setValue(mensagem);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean salvarMensagemDestinatario(Mensagem mensagem) {
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("mensagens");
            databaseReference.child(uidDestinatario)
                    .child(uidRemetente)
                    .push()
                    .setValue(mensagem);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListener);
    }
}
