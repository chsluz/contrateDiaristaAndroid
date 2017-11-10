package com.contratediarista.br.contratediarista.ui;

import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.entity.Rotina;
import com.contratediarista.br.contratediarista.entity.Usuario;
import com.contratediarista.br.contratediarista.retrofit.RetrofitCallback;
import com.contratediarista.br.contratediarista.retrofit.RetrofitInicializador;
import com.contratediarista.br.contratediarista.retrofit.service.RotinaService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Response;

public class InformacoesUsuarioUi extends Fragment implements OnMapReadyCallback {
    private LatLng latLng;
    private GoogleMap mMap;
    private Usuario usuario;
    private Rotina rotina;
    private TextView tvNome;
    private TextView tvAvaliacao;
    private TextView tvQtdeAvaliacao;
    private Button btnRemover;
    private Button btnAprovar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.informacoes_usuario_ui, container, false);
        Bundle extra = this.getArguments();
        if(extra != null) {
            Gson gson = new Gson();
            usuario = gson.fromJson(extra.getString("usuario"),Usuario.class);
            rotina = gson.fromJson(extra.getString("rotina"),Rotina.class);
        }

        tvNome = (TextView) view.findViewById(R.id.tv_nome);
        tvNome.setText("Nome: " +usuario.getNome());
        tvAvaliacao = (TextView) view.findViewById(R.id.tv_avaliacao);
        tvAvaliacao.setText("Avaliação: "+usuario.getMediaAprovacaoUsuario());
        tvQtdeAvaliacao = (TextView) view.findViewById(R.id.tv_qtde_avaliacao);
        tvQtdeAvaliacao.setText("Qtde Avaliação: " +usuario.getQuantidadeAvaliacoesUsuario());
        btnRemover = (Button) view.findViewById(R.id.btn_remover);
        btnAprovar = (Button) view.findViewById(R.id.btn_aprovar);

        btnRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call call = new RetrofitInicializador().getRetrofit().create(RotinaService.class).removerListarPrestador(rotina.getId(),usuario.getUid());
                RetrofitCallback callback = new RetrofitCallback(getActivity(),"Removendo usuário da lista","Erro ao remover usuário da lista"){
                    @Override
                    public void onResponse(Call call, Response response) {
                        super.onResponse(call, response);
                        if(response.code() == 200) {
                            Toast.makeText(getActivity(),"Removido com sucesso.",Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new AprovacaoVagasUi()).commit();
                        }
                        else {
                            Toast.makeText(getActivity(),"Erro ao remover usuario da lista",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        super.onFailure(call, t);
                    }
                };
                call.enqueue(callback);
            }
        });

        btnAprovar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call call = new RetrofitInicializador().getRetrofit().create(RotinaService.class).alterarPrestadorSelecionado(rotina.getId(),usuario.getUid());
                RetrofitCallback callback = new RetrofitCallback(getActivity(),"Aprovando usuário","Erro ao aprovar usuário") {
                    @Override
                    public void onResponse(Call call, Response response) {
                        super.onResponse(call, response);
                        if(response.code() == 200) {
                            Toast.makeText(getActivity(),"Usuário aprovado com sucesso.",Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new AprovacaoVagasUi()).commit();
                        }
                        else {
                            Toast.makeText(getActivity(),"Erro ao aprovar usuario",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        super.onFailure(call, t);
                    }
                };
                call.enqueue(callback);
            }
        });

        latLng = new LatLng(usuario.getEndereco().getLatitude(),usuario.getEndereco().getLongitude());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) throws SecurityException {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        center();

    }


    public void center() throws  SecurityException{
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        //location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to location user
                .zoom(16).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.addMarker(new MarkerOptions().position(latLng).title("Endereço selecionado"));

    }
}
