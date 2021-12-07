package com.example.favdishjava.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.favdishjava.R;
import com.example.favdishjava.databinding.ActivityAddUpdateDishBinding;
import com.example.favdishjava.databinding.ActivityMainBinding;

public class AddUpdateDishActivity extends AppCompatActivity {

    private ActivityAddUpdateDishBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddUpdateDishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}