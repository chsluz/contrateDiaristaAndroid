package com.contratediarista.br.contratediarista.fragmento;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.adapter.ConversaAdapter;
import com.contratediarista.br.contratediarista.entity.Conversa;
import com.contratediarista.br.contratediarista.ui.ConversaActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {
    private ListView lvConversas;
    private List<Conversa> conversas;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;


    public ConversasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        conversas = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("conversas")
        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        lvConversas = (ListView) view.findViewById(R.id.lv_conversas);
        final ConversaAdapter adapter = new ConversaAdapter(getActivity(),conversas);
        lvConversas.setAdapter(adapter);

        lvConversas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Conversa conversa = conversas.get(position);
                Intent intent = new Intent(getActivity(), ConversaActivity.class);
                intent.putExtra("nome",conversa.getNome());
                intent.putExtra("chave",conversa.getIdUsuario());
                startActivity(intent);
            }
        });

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                conversas.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    Conversa conversa = data.getValue(Conversa.class);
                    conversas.add(conversa);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListener);
    }
}
