package com.example.favdishjava.application;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.example.favdishjava.model.database.FavDishRepository;
import com.example.favdishjava.model.database.FavDishRoomDatabase;
import com.example.favdishjava.utils.Constants;

public class FavDishApplication extends Application {
    private static FavDishRoomDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = FavDishRoomDatabase.getDatabase(this);
    }

    public FavDishRepository getRepository() {
        return new FavDishRepository(database.favDishDao());
    }
}
