package com.example.allergyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.allergyapp.ui.RecipesFragment.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        email       = findViewById(R.id.email);
        password    = findViewById(R.id.password);
        btnLogin    = findViewById(R.id.login);
        btnRegister = findViewById(R.id.register);
        rq = Volley.newRequestQueue(this);
        jsonParse();

    }

    private void jsonParse() {


        JsonObjectRequest objReq = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String hits = response.getString("hits");
                            for (int i=0; i <= 9; i++) {
                                JSONObject hitsObject = new JSONArray(hits).getJSONObject(i);
                                Recipe recipeObject = new Recipe();
                                String recipe = hitsObject.getString("recipe");
                                String uriString = new JSONObject(recipe).getString("uri");
                                recipeObject.setDescription(uriString);
                                String labelString = new JSONObject(recipe).getString("label");
                                recipeObject.setName(labelString);
                                String ingredients = new JSONObject(recipe).getString("ingredientLines");
                                recipeObject.setIngredients(ingredients);
                                String image = new JSONObject(recipe).getString("image");
                                recipeObject.setImageUrl(image);
                                recipeList.add(recipeObject);

                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        rq.add(objReq);
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
