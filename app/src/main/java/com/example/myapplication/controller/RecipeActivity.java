package com.example.myapplication.controller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.myapplication.api.RecipeRequestListener;
import com.example.myapplication.databinding.ActivityRecipeBinding;
import com.example.myapplication.model.Recipe;
import com.example.myapplication.model.RecipesService;
import com.example.myapplication.model.UserService;

public class RecipeActivity extends AppCompatActivity {
    private ActivityRecipeBinding binding;

    private int recipeId;

    public int getUserId(){
        return UserService.getInstance().getCurrentUser().getId();
    }

    public String getCategory(){
        String category = null;
        int categoryId = binding.radioGroupCategory.getCheckedRadioButtonId();
        if(categoryId != -1){
            category = (String)findViewById(categoryId).getTag();
        }
        return category;
    }

    public String getName(){
        return binding.editTextName.getText().toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public int getDurationHour() {
        return binding.spinnerTimePicker.getHour();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public int getDurationMinute() {
        return binding.spinnerTimePicker.getMinute();
    }

    public String getDescription() {
        return binding.multilineDescription.getText().toString();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.spinnerTimePicker.setIs24HourView(true);
        binding.spinnerTimePicker.setHour(0);
        binding.spinnerTimePicker.setMinute(0);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            Recipe recipe = extras.getParcelable(RecipesActivity.KEY_EDIT_RECIPE_EXTRA);

            recipeId = recipe.getId();

            ((RadioButton)binding.radioGroupCategory.findViewWithTag(recipe.getCategory())).setChecked(true);
            binding.editTextName.setText(recipe.getName());
            binding.spinnerTimePicker.setHour(recipe.getDurationHours());
            binding.spinnerTimePicker.setMinute(recipe.getDurationMinutes());
            binding.multilineDescription.setText(recipe.getDescription());
            binding.buttonSave.setOnClickListener(editClickListener);
            binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RecipesService.getInstance().delete(recipeId, recipeRequestListener, RecipeActivity.this);
                }
            });

        }else{
            binding.buttonDelete.setVisibility(View.GONE);
            binding.buttonSave.setText("Add");

            binding.buttonSave.setOnClickListener(addClickListener);
        }




    }

    private View.OnClickListener addClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View view) {

            RecipesService.getInstance().add(getCategory(), getName(), getDurationHour(), getDurationMinute(), getDescription(), getUserId(), recipeRequestListener, RecipeActivity.this);


        }
    };

    private View.OnClickListener editClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View view) {

            RecipesService.getInstance().edit(recipeId, getCategory(), getName(), getDurationHour(), getDurationMinute(), getDescription(), recipeRequestListener, RecipeActivity.this);


        }
    };

    private RecipeRequestListener recipeRequestListener = new RecipeRequestListener() {
        @Override
        public void onResponse(boolean success) {
            if(success){
                finishOk();
                finish();
            }else{
                Toast.makeText(RecipeActivity.this, "Invalid infos...", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void finishOk(){
        setResult(RESULT_OK);
        finish();
    }
}