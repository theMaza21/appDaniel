package com.daniel.appdaniel.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.daniel.appdaniel.R;
import com.daniel.appdaniel.models.User;
import com.daniel.appdaniel.providers.AuthProvider;
import com.daniel.appdaniel.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class CompleteProfileActivity extends AppCompatActivity {

    TextInputEditText mTextInputUsername;
    TextInputEditText mTextInputPhone;

    Button mButtonRegister;
    AuthProvider mAuthProvider;
    UsersProvider mUserProvider;
    AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);


        mTextInputUsername = findViewById(R.id.NombreDeUsuario);
        mTextInputPhone = findViewById(R.id.textInputPhone);
        mButtonRegister = findViewById(R.id.btnRegister);

       mAuthProvider = new AuthProvider();
       mUserProvider = new UsersProvider();
        mDialog = new   SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento ")
                .setCancelable(false).build();

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }
    private void  register()
    {
        String username = mTextInputUsername.getText().toString();
        String phone = mTextInputPhone.getText().toString();


        if (!username.isEmpty())
        {
            UpdateUser(username, phone);
        }
        else
        {
            Toast.makeText(this, "Para continuar inserta todos los campos ",
                    Toast.LENGTH_LONG).show();

        }

    }
    private void UpdateUser(final String username, final String phone)
    {
        String id = mAuthProvider.getUid();
        User user = new User();
        user.setUsername(username);
        user.setId(id);
        user.setPhone(phone);
        user.setTimestamp(new Date().getTime());
        mDialog.show();
        mUserProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful())
                {
                    Intent intent = new Intent(CompleteProfileActivity.this,HomeActivity.class
                    );
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(CompleteProfileActivity.this,"No se  pudo almacenarse en la base de datos  ",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}