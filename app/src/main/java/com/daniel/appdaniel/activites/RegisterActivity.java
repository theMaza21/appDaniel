package com.daniel.appdaniel.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;

import com.daniel.appdaniel.R;
import com.daniel.appdaniel.models.User;
import com.daniel.appdaniel.providers.AuthProvider;
import com.daniel.appdaniel.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {
  CircleImageView mCirculeImageViewBack;
  TextInputEditText mTextInputUsername;
  TextInputEditText mTextInputEmail;
  TextInputEditText mTextInputPasswod;
    TextInputEditText mTextInputPhone;
  TextInputEditText mTextInputConfirmPassword;
  Button mButtonRegister;
  UsersProvider mUserProvider;
  AuthProvider mAuthProvider;
  AlertDialog mDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mCirculeImageViewBack = findViewById(R.id.circuleImageBack);
        mTextInputUsername = findViewById(R.id.NombreDeUsuario);
        mTextInputEmail =findViewById(R.id.CorreoElectronico);
        mTextInputPasswod = findViewById(R.id.RejisterPasword);
        mTextInputConfirmPassword = findViewById(R.id.ConfirmarPasword);
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

        mCirculeImageViewBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }
    private void  register()
    {
        String username = mTextInputUsername.getText().toString();
        String email = mTextInputEmail.getText().toString();
        String password = mTextInputPasswod.getText().toString();
        String confirmPassword = mTextInputConfirmPassword.getText().toString();
        String phone = mTextInputPhone.getText().toString();

        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && !phone.isEmpty())
        {
            if (isEmailValid(email))
            {
                if(password.equals(confirmPassword))
                {
                    if (password.length() >= 6)
                    {
                        createUser(username,email,password,phone);
                    }
                    else
                    {
                        Toast.makeText(this,"La Contrasena de ve de tener al menos 6 de carateres ",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(this,"Las contrasenas no considen ",
                            Toast.LENGTH_LONG).show();
                }
                Toast.makeText(this, "Isertastes Todos los campos y el Email es valido ",
                        Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Isertastes Todos los campos pero el Email no es valido  ",
                        Toast.LENGTH_LONG).show();
            }
            Toast.makeText(this, "Has insertado todos los campos",
                    Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "Para continuar inserta todos los campos ",
                    Toast.LENGTH_LONG).show();

        }

    }
    private void createUser(final String username, final String email,final String password, final String phone)
    {
        mDialog.show();
        mAuthProvider.register(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
     {
         @Override
         public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful())
            {

                String id = mAuthProvider.getUid();
                User user = new User();
                user.setId(id);
                user.setEmail(email);
                user.setUsername(username);
                user.setPhone(phone);
                user.setTimestamp(new Date().getTime());
                mUserProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mDialog.dismiss();
                        if (task.isSuccessful())
                        {
                          Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this,"No se  pudo almacenarse en la base de datos  ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else
            {
                mDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "No Se pudo Rejistrar el Usuario ",
                        Toast.LENGTH_SHORT).show();
            }
         }
     });
    }
    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}