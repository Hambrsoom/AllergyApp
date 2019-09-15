package com.example.allergyapp.ui.RecipesFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.allergyapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
            imageView = itemView.findViewById(R.id.recipeImage);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
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



            Picasso.get()
                    .load(recipe.getImageUrl())
                    .placeholder(R.drawable.crop_image_menu_flip)
                    .error(R.drawable.crop_image_menu_flip)
                    // To fit image into imageView
                    .fit()
                    // To prevent fade animation
                    .noFade()
                    .into(holder.imageView);

            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.crop_image_menu_flip));


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

