package com.daniel.appdaniel.providers;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthProvider
    {
        private FirebaseAuth maAuth;

    public AuthProvider()
    {
        maAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> register(String email, String password)
    {
        return maAuth.createUserWithEmailAndPassword(email,password);
    }
    public Task<AuthResult>login(String email, String password)
    {
    return maAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> googleLogin(GoogleSignInAccount googleSignInAccount)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),
                null);
        return maAuth.signInWithCredential(credential);
    }
    public String getEmail()
    {
        if (maAuth.getCurrentUser() != null)
        {
            return maAuth.getCurrentUser().getEmail();
        }
        else
        {
            return  null;
        }
    }
    public String getUid()
    {
        if (maAuth.getCurrentUser() != null)
        {
            return maAuth.getCurrentUser().getUid();
        }
        else
        {
            return null;
        }
    }
        public FirebaseUser
        getUserSesion()
        {
            if (maAuth.getCurrentUser() != null)
            {
                return maAuth.getCurrentUser();
            }
            else
            {
                return null;
            }
        }
    public void logout()
    {
        if (maAuth != null)
        maAuth.signOut();

    }

    }
