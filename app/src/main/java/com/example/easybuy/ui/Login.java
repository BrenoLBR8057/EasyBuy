package com.example.easybuy.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easybuy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private String TAG = "TAG";
    private FirebaseAuth authentication = FirebaseAuth.getInstance();
    private Button btnEnter;
    private EditText login;
    private EditText password;
    private TextView forgotPassword;
    private TextView createNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadFields();
        login();
        newUser();
    }

    private void loadFields() {
        login = findViewById(R.id.editTextLogin);
        password = findViewById(R.id.editTextPassword);
        forgotPassword = findViewById(R.id.textViewForgotPassword);
        createNewUser = findViewById(R.id.textViewCreateLogin);
        btnEnter = findViewById(R.id.btnEnter);
    }

    private void newUser(){
     createNewUser.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(Login.this, CreateLogin.class);
             startActivity(intent);
         }
     });
    }

    private void login(){
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getLogin = login.getText().toString();
                String getPassword = password.getText().toString();
                authentication.signInWithEmailAndPassword(getLogin, getPassword)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = authentication.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(Login.this, ShoppingList.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Falha ao logar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = authentication.getCurrentUser();
        updateUI(currentUser);
    }


}