package com.example.myapplication.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.api.UserRequestListener;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.model.UserService;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = binding.editTextUsername.getText().toString();
                String password = binding.editTextPassword.getText().toString();

                UserService.getInstance().logIn(username, password, new UserRequestListener() {
                    @Override
                    public void onResponse(boolean success) {
                        if(success){
                            loggedIn();
                        }else{
                            Toast.makeText(LoginActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, LoginActivity.this);


            }
        });



        if (UserService.getInstance().getCurrentUser() != null){
            loggedIn();
        }


        binding.buttonSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }

    private void loggedIn(){
        Intent loggedInIntent = new Intent(LoginActivity.this, RecipesActivity.class);
        startActivity(loggedInIntent);

        finishAffinity();
    }
}