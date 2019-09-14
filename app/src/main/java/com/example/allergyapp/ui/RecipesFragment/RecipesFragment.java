package com.example.allergyapp.ui.RecipesFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.allergyapp.R;

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
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recipesRecyclerView);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            //initializing the productlist
        recipeList = new ArrayList<>();


        //adding some items to our list
        recipeList.add(
                new Recipe(
                        "Apple MacBook Air Core i5 5th Gen - (8 GB/128 GB SSD/Mac OS Sierra)",
                        "13.3 inch, Silver, 1.35 kg",
                        "hello there"));

        recipeList.add(
                new Recipe(
                        "Apple MacBook Air Core i5 5th Gen - (8 GB/128 GB SSD/Mac OS Sierra)",
                        "13.3 inch, Silver, 1.35 kg",
                        "hello there"));



        // specify an adapter
        mAdapter = new RecipesAdapter(getActivity().getApplicationContext(), recipeList);
        recyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);

    }

}
