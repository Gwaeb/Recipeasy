package com.example.myapplication.api;

import com.example.myapplication.model.Recipe;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface RecipesFetchListener {
    void onResponse(ArrayList<Recipe> recipes);
}
