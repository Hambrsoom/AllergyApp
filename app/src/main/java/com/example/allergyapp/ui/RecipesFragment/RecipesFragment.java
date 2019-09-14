package com.example.allergyapp.ui.RecipesFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    RecipesAdapter mAdapter;
    List<Recipe> recipeList;

    public static RecipesFragment newInstance() {
        return new RecipesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recipes_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.recipesRecyclerView);
        recyclerView.setHasFixedSize(true);

        EditText editText = rootView.findViewById(R.id.editText);
        String searchTerm = editText.getText().toString();

        // Use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        // Initializing the product list
        recipeList = new ArrayList<>();

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
        mAdapter = new RecipesAdapter(getActivity().getApplicationContext(), recipeList);
        recyclerView.setAdapter(mAdapter);
        return rootView;
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

