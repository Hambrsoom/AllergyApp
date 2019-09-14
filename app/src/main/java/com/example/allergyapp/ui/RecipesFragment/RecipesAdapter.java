package com.example.allergyapp.ui.RecipesFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.allergyapp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public Context context;
    public String[] items;
    public RecipesAdapter(Context context, String[] items){
        this.context = context;
        this.items = items;
    }

    public static class Recipe extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public Recipe(TextView v) {
            super(v);
            textView = v;
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe, parent, false);

        Recipe recipe = new Recipe(v);
        return recipe;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        holder.textView.setText(items[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }
    public class Item extends RecyclerView.ViewHolder {

        public Item(@NonNull View itemView) {
            super(itemView);
        }
    }
}
