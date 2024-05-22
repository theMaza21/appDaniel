package com.daniel.appdaniel.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daniel.appdaniel.R;
import com.daniel.appdaniel.models.User;
import com.daniel.appdaniel.providers.AuthProvider;
import com.daniel.appdaniel.providers.UsersProvider;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    TextView  mTextViewRegister;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    Button mButtonLogin;
    AuthProvider mAuthProvider;
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    SignInButton mButtonGoogle;
   UsersProvider mUsersProvider;
   AlertDialog mDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewRegister = findViewById(R.id.textViewRegister);
        mTextInputEmail = findViewById(R.id.textInputEmail);
        mTextInputPassword = findViewById(R.id.textInputPasword);
        mButtonLogin = findViewById(R.id.btnLongin);
        mAuthProvider = new AuthProvider();
            mButtonGoogle = findViewById(R.id.btnLonginGoogle);

            mDialog = new   SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento ")
                .setCancelable(false).build();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
       mUsersProvider = new UsersProvider();
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             login();
            }
        });

          mButtonGoogle.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  signIn();
              }
          });


        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inter = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(inter);
            }
        });
        obtenerFCMToken();
    }

    private void obtenerFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Error al obtener el token de registro de FCM", task.getException());
                            return;
                        }

                        // Obtén el nuevo token de registro de FCM
                        String token = task.getResult();

                        // Registra y muestra un mensaje
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuthProvider.getUserSesion() != null)
        {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        mDialog.show();
        mAuthProvider.googleLogin(acct).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id =mAuthProvider.getUid();
                            checkUserExist(id);


                        }
                        else
                        {
                            mDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "No se pude iniciar secion con google  ",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void checkUserExist(final String id)
    {
        mUsersProvider.getUser(id).addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if (documentSnapshot.exists())
                {
                    mDialog.dismiss();
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
                else
                {
                    String email = mAuthProvider.getEmail();
                    User user = new User();
                    user.setEmail(email);
                    user.setId(id);
                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        mDialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, CompleteProfileActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "No se pudo almacenar la informacion del Usuario ",
                                    Toast.LENGTH_LONG).show();
                        }
                    }


                });
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void login()
    {
        String email = mTextInputEmail.getText().toString();
        String password = mTextInputPassword.getText().toString();
        mDialog.show();
        mAuthProvider.login(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.dismiss();
                if (task.isSuccessful())
                {
                 
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "El email o la contrasena que ingresastes no son correctas ",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        Log.d("CAMPO","email:"+email);
        Log.d("CAMPO","email:"+password);

    }
}