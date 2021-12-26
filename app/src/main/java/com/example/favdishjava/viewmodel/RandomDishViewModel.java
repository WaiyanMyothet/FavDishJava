package com.example.favdishjava.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.favdishjava.model.database.FavDishRepository;
import com.example.favdishjava.model.entities.RandomDish;
import com.example.favdishjava.model.network.RandomDishApiService;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RandomDishViewModel extends ViewModel {

    public RandomDishApiService randomDishApiService = new RandomDishApiService();
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    public MutableLiveData<Boolean> loadRandomDish = new MutableLiveData<Boolean>();
    public MutableLiveData<RandomDish.Recipes> randomDishResponse = new MutableLiveData<RandomDish.Recipes>();
    public MutableLiveData<Boolean> randomDishLoadingError = new MutableLiveData<Boolean>();

    public void getRandomDishFromAPI() {
        loadRandomDish.setValue(true);
        mDisposable.add(randomDishApiService.getRandomDish()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableSingleObserver<RandomDish.Recipes>() {
                            @Override
                            public void onError(Throwable e) {
                                loadRandomDish.setValue(false);
                                randomDishLoadingError.setValue(true);
                                e.printStackTrace();
                            }

                            @Override
                            public void onSuccess(RandomDish.Recipes value) {
                                loadRandomDish.setValue(false);
                                randomDishLoadingError.setValue(false);
                                randomDishResponse.setValue(value);
                            }
                        }));

    }

}
