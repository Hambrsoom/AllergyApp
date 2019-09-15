package com.example.allergyapp.ui.RecipesFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.allergyapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecipesFragment extends Fragment {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String UID   = user.getUid();
    private RecipesViewModel mViewModel;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView resultTv;
    TextView header2;
    Button searchRecipe;
    EditText editText;
    RecipesAdapter mAdapter;
    List<Recipe> recipeList;
    RequestQueue rq;
    String allergies;
    String search;


    public static RecipesFragment newInstance() {
        return new RecipesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recipes_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recipesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        resultTv = rootView.findViewById(R.id.headerTv);
        header2 = rootView.findViewById(R.id.headerTv2);
        resultTv.setVisibility(View.GONE);
        header2.setVisibility(View.GONE);
        editText = rootView.findViewById(R.id.editText);
        searchRecipe = rootView.findViewById(R.id.searchRecipe);

        allergies = "";
        searchRecipe.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                searchRecipe();
            }
        });
        // use a linear layout manager

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            //initializing the productlist
        recipeList = new ArrayList<>();

        // specify an adapter
        mAdapter = new RecipesAdapter(getActivity().getApplicationContext(), recipeList);
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private void searchRecipe() {
        search = editText.getText().toString();
        (FirebaseDatabase.getInstance().getReference()).child("users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                allergies = snapshot.getValue().toString();
                String URL  = "https://api.edamam.com/search?q="+search+"&app_id=b535c32e&app_key=18bbb1d1d4d94b1f53dad01ca771b366";
                if(!allergies.contains("Empty")){
                    String[]tempArr = allergies.split(",");
                    for(String str : tempArr){
                        URL += "&excluded="+str;
                    }
                }
                rq = Volley.newRequestQueue(getActivity().getApplicationContext());
                JsonObjectRequest objReq = new JsonObjectRequest(
                        Request.Method.GET,
                        URL,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String hits = response.getString("hits");
                                    recipeList.clear();
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
                                    resultTv.setText(recipeList.size()+" Results");
                                    header2.setVisibility(View.VISIBLE);
                                    resultTv.setVisibility(View.VISIBLE);
                                    mAdapter.notifyDataSetChanged();
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
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);

    }

}
