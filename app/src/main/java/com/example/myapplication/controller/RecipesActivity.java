package com.example.myapplication.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.api.RecipesFetchListener;
import com.example.myapplication.databinding.ActivityRecipesBinding;
import com.example.myapplication.model.Recipe;
import com.example.myapplication.model.RecipesService;
import com.example.myapplication.model.User;
import com.example.myapplication.model.UserService;

import java.util.ArrayList;
import java.util.Random;

public class RecipesActivity extends AppCompatActivity implements OnRecipeClickListener{
    private final static int ADD_RECIPE_REQUEST = 1;
    private final static int EDIT_RECIPE_REQUEST = 2;
    public final static String KEY_EDIT_RECIPE_EXTRA = "ca.joffman.edit-recipe-extra";
    private ActivityRecipesBinding binding;

    private RecipiesAdapter recipiesAdapter;

    private int getCurrentUserId() {
        return UserService.getInstance().getCurrentUser().getId();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipesBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        binding.recyclerRecipes.setHasFixedSize(true);
        binding.recyclerRecipes.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.recyclerRecipes.setLayoutManager(new LinearLayoutManager(this));

        recipiesAdapter = new RecipiesAdapter(this);
        binding.recyclerRecipes.setAdapter(recipiesAdapter);

        refresh();

        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addRecipeIntent = new Intent(RecipesActivity.this, RecipeActivity.class);
                startActivityForResult(addRecipeIntent, ADD_RECIPE_REQUEST);
            }
        });



        binding.buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserService.getInstance().logOut();

                Intent logoutIntent = new Intent(RecipesActivity.this, LoginActivity.class);
                startActivity(logoutIntent);

                finish();
            }
        });
    }


    private void refresh(){
        RecipesService.getInstance().getAll(getCurrentUserId(), new RecipesFetchListener() {
            @Override
            public void onResponse(ArrayList<Recipe> recipes) {
                if(recipes != null){
                    recipiesAdapter.setRecipes(recipes);
                    recipiesAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(RecipesActivity.this, "Could not load recipes...", Toast.LENGTH_SHORT).show();
                }

            }
        }, this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && (requestCode == ADD_RECIPE_REQUEST || requestCode == EDIT_RECIPE_REQUEST)){
            refresh();
        }
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {

        Intent editRecipeIntent = new Intent(RecipesActivity.this, RecipeActivity.class);
        editRecipeIntent.putExtra(KEY_EDIT_RECIPE_EXTRA, recipe);
        startActivityForResult(editRecipeIntent, EDIT_RECIPE_REQUEST);
    }


}