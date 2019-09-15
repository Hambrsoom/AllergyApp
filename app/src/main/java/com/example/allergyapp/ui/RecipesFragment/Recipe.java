package com.example.allergyapp.ui.RecipesFragment;

import java.util.ArrayList;

public class Recipe {
    String imageUrl;
    private int image;
    String description;

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    String ingredients;
    String name;
    ArrayList<String> list;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Recipe(){
        imageUrl= null;
        name= null;
        list= null;
    }
    public Recipe(String title, String shortdesc, String ingredients){
        this.name = title;
        this.description = shortdesc;
        this.ingredients = ingredients;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }
}