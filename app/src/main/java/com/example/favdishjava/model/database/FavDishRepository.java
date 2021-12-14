package com.example.favdishjava.model.database;

import com.example.favdishjava.model.entities.FavDish;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class FavDishRepository {
    private FavDishDao _favDishDao;

    public FavDishRepository(FavDishDao favDishDao) {
        _favDishDao = favDishDao;
    }

    public Completable insertFavDishData(FavDish favDish) {
        return _favDishDao.insertFavDishDetails(favDish);
    }

    public Flowable<List<FavDish>> allDishesList (){
        return _favDishDao.getAllDishesList();
    }

    public Completable updateFavDishData(FavDish favDish) {
        return _favDishDao.updateFavDishDetails(favDish);
    }

    public Flowable<List<FavDish>> favoriteDishes(){
        return _favDishDao.getFavoriteDishesList();
    }

    public Completable deleteFavDishData(FavDish favDish) {
        return _favDishDao.deleteFavDishDetails(favDish);
    }

    public Flowable<List<FavDish>> filteredListDishes(String value) {
        return _favDishDao.getFilteredDishesList(value);
    }

    public Flowable<List<FavDish>> dishesList() {
        return _favDishDao.getDishesList();
    }

}
