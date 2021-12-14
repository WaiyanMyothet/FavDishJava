package com.example.favdishjava.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.favdishjava.model.database.FavDishRepository;

public class FavDishViewModelFactory implements ViewModelProvider.Factory {
    private FavDishRepository _repository;

    public FavDishViewModelFactory(FavDishRepository repository) {
        _repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(FavDishViewModel.class)) {
            return (T) new FavDishViewModel(_repository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}