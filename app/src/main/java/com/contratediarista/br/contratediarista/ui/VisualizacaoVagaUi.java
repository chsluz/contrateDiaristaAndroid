package com.contratediarista.br.contratediarista.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.entity.Rotina;
import com.contratediarista.br.contratediarista.entity.Usuario;
import com.contratediarista.br.contratediarista.helper.Permissao;
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

public class VisualizacaoVagaUi extends Fragment implements OnMapReadyCallback {
    Gson gson = new Gson();
    Rotina rotina;
    private GoogleMap mMap;
    LatLng latLng;
    Button btnCandidatar;
    TextView tvJaCandidatou;
    private String uidUsuario;
    SharedPreferences sharedPreferences;
    private boolean jaCandidato;
    private boolean contratante;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_visualizacao_vaga_ui, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            rotina = gson.fromJson(bundle.get("rotina").toString(),Rotina.class);
            contratante = bundle.getBoolean("contratante");
        }

        sharedPreferences = getActivity().getSharedPreferences("informacoes_usuario",Context.MODE_PRIVATE);
        uidUsuario = sharedPreferences.getString("uidUsuario","");

        btnCandidatar = (Button) view.findViewById(R.id.btn_candidatar);
        tvJaCandidatou = (TextView) view.findViewById(R.id.tv_ja_candidato);

        if(rotina != null) {
            double lat = rotina.getVaga().getEndereco().getLatitude();
            double lng = rotina.getVaga().getEndereco().getLongitude();
            latLng = new LatLng(lat,lng);
        }
        if(contratante) {
            btnCandidatar.setVisibility(View.GONE);
            tvJaCandidatou.setVisibility(View.GONE);
        }
        jaCandidato = verificarJaCandidato();
        if(jaCandidato) {
            btnCandidatar.setVisibility(View.GONE);
        }
        else {
            tvJaCandidatou.setVisibility(View.GONE);
        }
        btnCandidatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rotina.getPrestadores().size() >= 5) {
                    Toast.makeText(getActivity(),
                            "Número máximo de candidatos já preenchido",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Call call = new RetrofitInicializador().getRetrofit()
                            .create(RotinaService.class).candidatarVaga(uidUsuario,rotina.getId());
                    RetrofitCallback callback =
                            new RetrofitCallback(getActivity(),
                                    getString(R.string.candidatando_vaga),
                                    getString(R.string.erro_candidatar_vaga)){
                        @Override
                        public void onResponse(Call call, Response response) {
                            if(response.code() == 200) {
                                btnCandidatar.setVisibility(View.GONE);
                                tvJaCandidatou.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(),
                                        "Candidatou-se com sucesso.",Toast.LENGTH_SHORT).show();
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
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
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

    public boolean verificarJaCandidato() {
        Usuario usuario = new Usuario();
        usuario.setUid(uidUsuario);
        if(rotina.getPrestadores().contains(usuario)) {
            return true;
        }
        else if(rotina.getPrestadorSelecionado() != null && rotina.getPrestadorSelecionado().equals(usuario)) {
            return true;
        }
        else {
            return false;
        }
    }
}
