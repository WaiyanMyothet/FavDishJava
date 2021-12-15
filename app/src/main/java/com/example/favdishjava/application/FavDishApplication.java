package com.example.favdishjava.application;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.example.favdishjava.model.database.FavDishRepository;
import com.example.favdishjava.model.database.FavDishRoomDatabase;
import com.example.favdishjava.utils.Constants;

public class FavDishApplication extends Application {

    private FavDishRoomDatabase database;
    private FavDishRepository repository;
    private Context context;

    public FavDishApplication(Context context) {
        this.context=context;
        database = FavDishRoomDatabase.getDatabase(context);
        repository = new FavDishRepository(database.favDishDao());
    }
    public FavDishRepository getRepository(){
        return  repository;
    }
}
