package com.example.favdishjava.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;

import com.example.favdishjava.model.database.FavDishRepository;
import com.example.favdishjava.model.entities.FavDish;

import java.util.List;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavDishViewModel extends ViewModel {
    private FavDishRepository _repository;

    public FavDishViewModel(FavDishRepository repository) {
        _repository = repository;
    }

    public void insert(FavDish favDish) {
        _repository.insertFavDishData(favDish);
    }

    public LiveData<List<FavDish>> allDishesList() {
        return LiveDataReactiveStreams.fromPublisher(_repository.allDishesList()
                .subscribeOn(Schedulers.io()));
    }

    public void update(FavDish favDish) {
        _repository.updateFavDishData(favDish);
    }

    public LiveData<List<FavDish>> favoriteDishes() {
        return LiveDataReactiveStreams.fromPublisher(_repository.favoriteDishes()
                .subscribeOn(Schedulers.io()));
    }

    public void delete(FavDish favDish) {
        _repository.deleteFavDishData(favDish);
    }

    public LiveData<List<FavDish>> getFilteredList(String value) {
        return LiveDataReactiveStreams.fromPublisher(_repository.filteredListDishes(value)
                .subscribeOn(Schedulers.io()));
    }

}

