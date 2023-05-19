package com.example.testapp;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView emailView;
    private TextView repeatedPasswordView;
    private TextView passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);
        repeatedPasswordView = findViewById(R.id.passwordRepeat);

        Button registerBtn = findViewById(R.id.registerButton);
        TextView redirectToLogin = findViewById(R.id.redirectToLogin);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailView.getText().toString();
                String password = passwordView.getText().toString();
                String repeatPassword = repeatedPasswordView.getText().toString();
                if(checkInputedData(email, password, repeatPassword)) {
                    firebaseregistration(mAuth, email, password);
                }
            }
        });

        redirectToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }

    private void firebaseregistration(FirebaseAuth mAuth, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Firebase registration failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public boolean checkInputedData(String email, String password, String repeatPassword) {
        if(TextUtils.isEmpty(password)) {
            passwordView.setError("Password cannot be empty");
            passwordView.requestFocus();
            return false;
        } else if(TextUtils.isEmpty(email)) {
            emailView.setError("Email cannot be empty");
            passwordView.requestFocus();
            return false;
        } else if(!TextUtils.equals(password, repeatPassword)) {
            repeatedPasswordView.setError("Password doesn't matching");
            repeatedPasswordView.requestFocus();
            return false;
        } else {
            return true;
        }
    }

}
