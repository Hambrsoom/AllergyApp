package com.example.allergyapp.ui.RecipesFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.allergyapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecipesFragment extends Fragment {

    private RecipesViewModel mViewModel;
    private RecyclerView recyclerView;
    String[] items = {"item0","item1", "item3"};
    RecyclerView.LayoutManager layoutManager;
    RecipesAdapter mAdapter;

    public static RecipesFragment newInstance() {
        return new RecipesFragment();
    }
    TextView txt;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipes_fragment, container, false);
        recyclerView = rootView.findViewById(R.id.recipesRecyclerView);
        recyclerView.setAdapter(new RecipesAdapter(getActivity().getApplicationContext(), items));

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new RecipesAdapter(getActivity().getApplicationContext(), items);
        recyclerView.setAdapter(mAdapter);
        return inflater.inflate(R.layout.recipes_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);


        // TODO: Use the ViewModel
    }

}
