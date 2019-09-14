package com.example.allergyapp.ui.RecipesFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.allergyapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {
    public Context context;
    public String[] items;
    private List<Recipe> recipeList;

    public RecipesAdapter(Context context, List<Recipe> recipeList){
        this.context = context;
        this.recipeList = recipeList;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTitle;
        TextView recipeDescr;
        TextView textViewIngredient;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            recipeDescr = itemView.findViewById(R.id.textViewShortDesc);
            textViewIngredient = itemView.findViewById(R.id.textViewIngredient);
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe, null);
        RecipeViewHolder holder = new RecipeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {

        Recipe recipe = recipeList.get(position);
        holder.textViewTitle.setText(recipe.getName());
        holder.recipeDescr.setText(recipe.getDescription());
        holder.textViewIngredient.setText(recipe.ingredients);

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
    public class Item extends RecyclerView.ViewHolder {

        public Item(@NonNull View itemView) {
            super(itemView);
        }
    }
}

