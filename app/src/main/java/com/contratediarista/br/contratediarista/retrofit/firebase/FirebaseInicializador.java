package com.contratediarista.br.contratediarista.retrofit.firebase;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by manga on 13/10/2017.
 */

public class FirebaseInicializador {
    private static FirebaseAuth firebaseAuth;
    public static FirebaseAuth getFirebaseAuth() {
        if(firebaseAuth == null) {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

}
