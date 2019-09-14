package com.example.allergyapp;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button btnLogin;
    Button btnRegister;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth       = FirebaseAuth.getInstance();
        email       = findViewById(R.id.email);
        password    = findViewById(R.id.password);
        btnLogin    = findViewById(R.id.login);
        btnRegister = findViewById(R.id.register);
    }

    public void onClickLogin(View view){
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    logIn();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Email or Password. Please try again.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
    public void logIn() {
        Intent myIntent = new Intent(this, MainFeatureActivity.class);
        this.startActivity(myIntent);
    }

    public void onClickRegister(View view){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).setValue("Empty");
                    logIn();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Email or Password. Please try again.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

}
