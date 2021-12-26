package com.example.favdishjava.model.network;

import com.example.favdishjava.model.entities.RandomDish;
import com.example.favdishjava.utils.Constants;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RandomDishApiService {
    private Retrofit retrofit;
    private RandomDishAPI randomDishAPI;

    public RandomDishApiService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        randomDishAPI = retrofit.create(RandomDishAPI.class);
    }

    public Single<RandomDish.Recipes> getRandomDish() {
        return randomDishAPI.getRandomDish(Constants.API_KEY_VALUE, Constants.LIMIT_LICENSE_VALUE, Constants.TAGS_VALUE, Constants.NUMBER_VALUE);
    }

}
