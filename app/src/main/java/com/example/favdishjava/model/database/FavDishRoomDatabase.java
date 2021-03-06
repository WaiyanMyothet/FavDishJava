package com.example.favdishjava.model.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.favdishjava.model.entities.FavDish;


@Database(entities = FavDish.class, version = 1)
public abstract class FavDishRoomDatabase extends RoomDatabase {
    public static FavDishRoomDatabase Instance;

    public static synchronized FavDishRoomDatabase getDatabase(Context context) {
        if (Instance == null) {
            Instance = Room.databaseBuilder(
                    context,
                    FavDishRoomDatabase.class,
                    "fav_dish_database"
            )
                    .fallbackToDestructiveMigration().build();
        }
        return Instance;
    }

    public abstract FavDishDao favDishDao();


}
