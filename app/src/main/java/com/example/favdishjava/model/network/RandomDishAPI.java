package com.example.favdishjava.model.network;

import com.example.favdishjava.model.entities.RandomDish;
import com.example.favdishjava.utils.Constants;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RandomDishAPI {

    @GET(Constants.API_ENDPOINT)
    Single<RandomDish.Recipes> getRandomDish(
            // Query parameter appended to the URL. This is the best practice instead of appending it as we have done in the browser.
            @Query(Constants.API_KEY) String apiKey,
            @Query(Constants.LIMIT_LICENSE) Boolean limitLicense,
            @Query(Constants.TAGS) String tags,
            @Query(Constants.NUMBER) int number
    );
}
