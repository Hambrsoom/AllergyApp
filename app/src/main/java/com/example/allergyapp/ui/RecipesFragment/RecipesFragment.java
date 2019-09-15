package com.example.allergyapp.ui.RecipesFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
=======
import android.widget.Button;
>>>>>>> 29eb38f33e48df9b017df4ebfe3d710ff9f9854b
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.allergyapp.R;

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

    private RecipesViewModel mViewModel;
    private RecyclerView recyclerView;
    private EditText editText;
    RecyclerView.LayoutManager layoutManager;
    Button searchRecipe;
    EditText editText;
    RecipesAdapter mAdapter;
    List<Recipe> recipeList;
    RequestQueue rq;


    public static RecipesFragment newInstance() {
        return new RecipesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recipes_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.recipesRecyclerView);
        recyclerView.setHasFixedSize(true);
<<<<<<< HEAD
=======
        editText = rootView.findViewById(R.id.editText);
        searchRecipe = rootView.findViewById(R.id.searchRecipe);
        searchRecipe.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                searchRecipe();
            }
        });
        // use a linear layout manager
>>>>>>> 29eb38f33e48df9b017df4ebfe3d710ff9f9854b

        EditText editText = rootView.findViewById(R.id.editText);
        String searchTerm = editText.getText().toString();

        // Use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        // Initializing the product list
        recipeList = new ArrayList<>();

<<<<<<< HEAD
        // Extract recipes for the search term
        String url = "https://api.edamam.com/search?q=" + searchTerm + "&app_id=b535c32e&app_key=18bbb1d1d4d94b1f53dad01ca771b366&fbclid=IwAR0iI2L9T_GoF8kEWBI-TwlHQA4HAk9Fa6ONq_y4cKZiPyzUBCXYM8TVeaw";

        // Adding some items to our list
//        recipeList.add(
//                new Recipe(
//                        "Apple MacBook Air Core i5 5th Gen - (8 GB/128 GB SSD/Mac OS Sierra)",
//                        "13.3 inch, Silver, 1.35 kg",
//                        "hello there"));
//
//        recipeList.add(
//                new Recipe(
//                        "Apple MacBook Air Core i5 5th Gen - (8 GB/128 GB SSD/Mac OS Sierra)",
//                        "13.3 inch, Silver, 1.35 kg",
//                        "hello there"));

        // Specify an adapter
=======
        // specify an adapter
>>>>>>> 29eb38f33e48df9b017df4ebfe3d710ff9f9854b
        mAdapter = new RecipesAdapter(getActivity().getApplicationContext(), recipeList);
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private void searchRecipe() {
        String search = editText.getText().toString();
        String URL  = "https://api.edamam.com/search?q="+search+"&app_id=b535c32e&app_key=18bbb1d1d4d94b1f53dad01ca771b366";
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
    }


//    String URL  = "https://api.edamx`am.com/search?q=chicken&app_id=b535c32e&app_key=18bbb1d1d4d94b1f53dad01ca771b366&from=0&to=3&calories=591-722&health=alcohol-free&fbclid=IwAR06ycJqPK2fdPHZ-unQg9wculhgvyNbMUZgzbtNZ8kqoNoNqHUlygo1waw";
//    RequestQueue rq = Volley.newRequestQueue(this);
//    JsonObjectRequest objReq = new JsonObjectRequest(
//            Request.Method.GET,
//            URL,
//            null,
//            new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
////                        try {
////                            JSONArray jsonArray = response.getJSONArray("hits");
////                            for(int i =0; i<jsonArray.length();i++){
////                                JSONObject recipe = jsonArray.getJSONObject(i);
////                                String label      = recipe.getString("label");
////                                String image      = recipe.getString("image");
////                                //String ingredients = recipe.getString("ingredientLines");
////                                txt.append(label+", "+image);
////                            }
////                        } catch (JSONException e){
////                            e.printStackTrace();
////                        }
//                }
//            },
//            new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    error.printStackTrace();
//                }
//            }
//    );
//        rq.add(objReq);

}

