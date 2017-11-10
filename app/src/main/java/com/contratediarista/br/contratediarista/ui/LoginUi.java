package com.contratediarista.br.contratediarista.ui;

import android.Manifest;
import android.app.ProgressDialog;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.Login;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginUi extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private EditText etLogin;
    private EditText etSenha;
    private Button btnLogar;
    private String chaveUsuarioLogado;
    CallbackManager mCallbackManager;
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

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Success", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("CANCEL", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("ERRO", "facebook:onError", error);
                // ...
            }
        });


        if (firebaseAuth.getCurrentUser() != null) {
            Intent iMenu = new Intent(LoginUi.this,MenuDrawerUi.class);
            //Intent iPrincipal = new Intent(LoginUi.this, PrincipalUi.class);
            startActivity(iMenu);
        }
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logarUsuario(v);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Token", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sucsess", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (firebaseAuth.getCurrentUser() != null) {
                                Intent iPrincipal = new Intent(LoginUi.this, PrincipalUi.class);
                                startActivity(iPrincipal);
                            }
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Erro", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginUi.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
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

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage(getString(R.string.logando));
        dialog.show();


        firebaseAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(LoginUi.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent iMenu = new Intent(LoginUi.this,MenuDrawerUi.class);
                            //Intent iPrincipal = new Intent(LoginUi.this, PrincipalUi.class);
                            startActivity(iMenu);
                            dialog.dismiss();
                        } else {
                            exibirMsgErro(task.getException());
                            dialog.dismiss();
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
