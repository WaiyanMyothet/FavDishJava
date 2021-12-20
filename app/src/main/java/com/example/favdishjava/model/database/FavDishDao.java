package com.example.favdishjava.model.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.favdishjava.model.entities.FavDish;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface FavDishDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertFavDishDetails(FavDish favDish);

    @Query("SELECT * FROM FAV_DISHES_TABLE ORDER BY ID")
    Flowable<List<FavDish>> getAllDishesList();

    @Update
    Completable updateFavDishDetails(FavDish favDish);

    @Query("SELECT * FROM FAV_DISHES_TABLE WHERE favorite_dish = 1")
    Flowable<List<FavDish>> getFavoriteDishesList();

    @Delete
    Completable deleteFavDishDetails(FavDish favDish);

    @Query("SELECT * FROM FAV_DISHES_TABLE WHERE type = :filterType")
    Flowable<List<FavDish>> getFilteredDishesList(String filterType);

    @Query("SELECT * FROM FAV_DISHES_TABLE ORDER BY ID")
    Flowable<List<FavDish>> getDishesList();


}

