package com.contratediarista.br.contratediarista.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.contratediarista.br.contratediarista.R;
import com.contratediarista.br.contratediarista.helper.Permissao;
import com.contratediarista.br.contratediarista.retrofit.firebase.FirebaseInicializador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginUi extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private EditText etLogin;
    private EditText etSenha;
    private Button btnLogar;
    private String chaveUsuarioLogado;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ui);
        Permissao.validaPermissoes(1,this,permissoes);
        firebaseAuth = FirebaseInicializador.getFirebaseAuth();
        etLogin = (EditText) findViewById(R.id.et_email);
        etSenha = (EditText) findViewById(R.id.et_senha);
        btnLogar = (Button) findViewById(R.id.btn_logar);

        if (firebaseAuth.getCurrentUser() != null) {
            Intent iPrincipal = new Intent(LoginUi.this, PrincipalUi.class);
            startActivity(iPrincipal);
        }
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logarUsuario(v);
            }
        });
    }

    public void logarUsuario(View view) {
        if ((etLogin.getText() == null || etLogin.getText().toString().isEmpty())
                || (etSenha.getText() == null || etSenha.getText().toString().isEmpty())) {
            Toast.makeText(this, R.string.preencha_login_senha, Toast.LENGTH_SHORT).show();
            return;
        }
        String email = etLogin.getText().toString();
        String senha = etSenha.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(LoginUi.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent iPrincipal = new Intent(LoginUi.this, PrincipalUi.class);
                            startActivity(iPrincipal);
                        } else {
                            exibirMsgErro(task.getException());
                        }
                    }
                })
        ;
    }

    public void exibirMsgErro(Exception e) {
        String chave = "";
        if(e instanceof FirebaseAuthInvalidUserException) {
            if(((FirebaseAuthInvalidUserException) e).getErrorCode().equals("ERROR_USER_NOT_FOUND")) {
              chave =  getString(R.string.ERROR_USER_NOT_FOUND);
            }
        }else if(e instanceof  FirebaseAuthInvalidCredentialsException) {
            if(((FirebaseAuthInvalidCredentialsException) e).getErrorCode().equals("ERROR_WRONG_PASSWORD")) {
                chave = getString(R.string.ERROR_WRONG_PASSWORD);
            }
            if(((FirebaseAuthInvalidCredentialsException) e).getErrorCode().equals("ERROR_INVALID_EMAIL")) {
                chave = getString(R.string.ERROR_INVALID_EMAIL);
            }
        }
        else{
            chave = e.getMessage();
        }
        Toast.makeText(this, chave, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int result : grantResults) {
            if(result == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }
    }

    public void  alertaValidacaoPermissao() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Permissões negadas");
        alert.setMessage("Para utiliza o app, é necessário aceitar as permissões");
        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
    }
}
