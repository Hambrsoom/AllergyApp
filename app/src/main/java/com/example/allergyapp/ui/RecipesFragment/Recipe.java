package com.example.allergyapp.ui.RecipesFragment;

import java.util.ArrayList;

public class Recipe {
    String imageUrl;
    String name;
    ArrayList<String> list;

    public Recipe(){
        imageUrl= null;
        name= null;
        list= null;
    }
    public Recipe(String imageUrl,String name, ArrayList<String> list){
        this.imageUrl = imageUrl;
        this.name     = name;
        this.list     = list;
    }
}
