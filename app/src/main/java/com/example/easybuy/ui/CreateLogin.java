package com.example.easybuy.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easybuy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateLogin extends AppCompatActivity {
    private EditText login;
    private EditText password;
    private Button btnCreate;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_login);
        btnCreate = findViewById(R.id.btnCreate);
        login = findViewById(R.id.editTextCreateLogin);
        password = findViewById(R.id.editTextCreatePassword);
        buttonClick();
    }

    private void buttonClick() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login.getText().toString();
                String newPassword = password.getText().toString();
                auth.createUserWithEmailAndPassword(email, newPassword)
                        .addOnCompleteListener(CreateLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = auth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(CreateLogin.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });
    }
        private void updateUI(FirebaseUser user) {
            if (user != null) {
                Intent intent = new Intent(CreateLogin.this, ShoppingList.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Falha ao logar", Toast.LENGTH_SHORT).show();
            }
    }
}