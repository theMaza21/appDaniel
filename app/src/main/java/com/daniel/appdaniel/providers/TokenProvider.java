package com.daniel.appdaniel.providers;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.daniel.appdaniel.models.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;


import java.time.Instant;


public class TokenProvider {
    CollectionReference mCollection;
        public TokenProvider() {
            mCollection = FirebaseFirestore.getInstance().collection("Token");
        }

        public void create(final String idUser) {
            if (idUser == null) {
                return;
            }
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                String token = task.getResult();
                                Token tokenObject = new Token(token);
                                mCollection.document(idUser).set(tokenObject).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "Token almacenado correctamente en Firestore");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "Error al almacenar el token en Firestore", e);
                                            }
                                        });
                            } else {
                                Log.w(TAG, "No se pudo obtener el token.", task.getException());
                            }
                        }
                    });
        }

        public Task<DocumentSnapshot> getToken(String idUser) {
            return mCollection.document(idUser).get();
        }
    }
