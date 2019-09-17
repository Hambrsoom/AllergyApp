package com.example.allergyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.allergyapp.ui.RecipesFragment.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button btnLogin;
    Button btnRegister;
    FirebaseAuth mAuth;
    TextView txt;
    List<Recipe> recipeList = new ArrayList<>();
    RequestQueue rq;
    private final static String URL  = "https://api.edamam.com/search?q=salmon&app_id=b535c32e&app_key=18bbb1d1d4d94b1f53dad01ca771b366";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth       = FirebaseAuth.getInstance();
        email       = findViewById(R.id.emailEditText);
        password    = findViewById(R.id.passwordEditText);
        btnLogin    = findViewById(R.id.loginBtn);
        btnRegister = findViewById(R.id.registerBtn);
        rq = Volley.newRequestQueue(this);
    }
    public void onClickLogin(View view){
        if(email.length()>0 && password.length()>0){
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
        } else {
            Toast toast_2 = Toast.makeText(getApplicationContext(), "Please Fill up the username and password fields before.",Toast.LENGTH_SHORT);
            toast_2.show();
        }

    }
    public void logIn() {
        Intent myIntent = new Intent(this, MainFeatureActivity.class);
        this.startActivity(myIntent);
    }

    public void onClickRegister(View view){
        if(email.length()>0 && password.length()>0){
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
        }else {
            Toast toast_2 = Toast.makeText(getApplicationContext(), "Please Fill up the username and password fields before.",Toast.LENGTH_SHORT);
            toast_2.show();
        }

    }

}
