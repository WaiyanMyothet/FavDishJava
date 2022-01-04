package com.example.favdishjava.view.fragments;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.favdishjava.R;
import com.example.favdishjava.application.FavDishApplication;
import com.example.favdishjava.databinding.DialogCustomListBinding;
import com.example.favdishjava.databinding.FragmentFavoriteDishesBinding;
import com.example.favdishjava.databinding.FragmentRandomDishBinding;
import com.example.favdishjava.model.entities.FavDish;
import com.example.favdishjava.model.entities.RandomDish;
import com.example.favdishjava.utils.Constants;
import com.example.favdishjava.view.adapters.CustomListItemAdapter;
import com.example.favdishjava.viewmodel.FavDishViewModel;
import com.example.favdishjava.viewmodel.FavDishViewModelFactory;
import com.example.favdishjava.viewmodel.RandomDishViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RandomDishFragment extends Fragment {
    private FragmentRandomDishBinding binding;
    private Dialog mProgressDialog;
    private RandomDishViewModel mRandomDishViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private FavDishViewModel mFavDishViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavDishViewModel = ViewModelProviders.of(this, new FavDishViewModelFactory(new FavDishApplication().getRepository())).get(FavDishViewModel.class);
        mRandomDishViewModel = ViewModelProviders.of(this).get(RandomDishViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRandomDishBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRandomDishViewModel.getRandomDishFromAPI();
        randomDishViewModelObserver();
        binding.srlRandomDish.setOnRefreshListener(() -> mRandomDishViewModel.getRandomDishFromAPI());
    }

    private void randomDishViewModelObserver() {
        mRandomDishViewModel.randomDishResponse.observe(getViewLifecycleOwner(), new Observer<RandomDish.Recipes>() {
            @Override
            public void onChanged(RandomDish.Recipes recipes) {
                if(binding.srlRandomDish.isRefreshing()){
                    binding.srlRandomDish.setRefreshing(false);
                }
                setRandomDishResponseInUI(recipes.recipes.get(0));
            }
        });

        mRandomDishViewModel.randomDishLoadingError.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(binding.srlRandomDish.isRefreshing()){
                    binding.srlRandomDish.setRefreshing(false);
                }
            }
        });

        mRandomDishViewModel.loadRandomDish.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean && !binding.srlRandomDish.isRefreshing()){
                    showCustomProgressDialog();
                }
                else{
                    hideProgressDialog();
                }
            }
        });

    }

    private void setRandomDishResponseInUI(RandomDish.Recipe recipe) {
        Glide.with(requireActivity()).load(recipe.image).into(binding.ivDishImage);
        binding.tvTitle.setText(recipe.title);
        String dishType = "other";
        if (!recipe.dishTypes.isEmpty()) {
            dishType = recipe.dishTypes.get(0);
            binding.tvType.setText(dishType);
        }
        binding.tvCategory.setText("Other");
        String ingredients = "";
        for (RandomDish.ExtendedIngredient extendedIngredient : recipe.extendedIngredients) {
            if (ingredients.isEmpty()) {
                ingredients = extendedIngredient.original;
            } else {
                ingredients = ingredients + ", \n" + extendedIngredient.original;
            }
        }
        binding.tvIngredients.setText(ingredients);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvCookingDirection.setText(Html.fromHtml(recipe.instructions, Html.FROM_HTML_MODE_COMPACT));
        } else
            binding.tvCookingDirection.setText(Html.fromHtml(recipe.instructions));

        binding.tvCookingTime.setText(getResources().getString(R.string.lbl_estimate_cooking_time, Integer.toString(recipe.readyInMinutes)));
        binding.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_favorite_unselected));


        final String dishTypeFinal = dishType;
        final String ingredientsFinal = ingredients;
        binding.ivFavoriteDish.setOnClickListener(new View.OnClickListener() {
            Boolean addedToFavorite = false;

            @Override
            public void onClick(View v) {
                if (addedToFavorite) {
                    Toast.makeText(requireActivity(), getResources().getString(R.string.msg_already_added_to_favorites), Toast.LENGTH_SHORT).show();
                } else {
                    FavDish favDish = new FavDish();

                    favDish.setCategory("Other");
                    favDish.setCookingTime(Integer.toString(recipe.readyInMinutes));
                    favDish.setDirectionToCook(recipe.instructions);
                    favDish.setFavoriteDish(true);
                    favDish.setImage(recipe.image);
                    favDish.setImageSource(Constants.DISH_IMAGE_SOURCE_ONLINE);
                    favDish.setIngredients(ingredientsFinal);
                    favDish.setTitle(recipe.title);
                    favDish.setType(dishTypeFinal);
                    mDisposable.add(mFavDishViewModel.insert(favDish)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(
                                    new DisposableCompletableObserver() {
                                        @Override
                                        public void onComplete() {
                                            addedToFavorite = true;
                                            Toast.makeText(requireActivity(), getResources().getString(R.string.msg_added_to_favorites), Toast.LENGTH_SHORT).show();
                                            binding.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_favorite_selected));
                                        }

                                        @Override
                                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                            Log.e("Error", "Unable to insert data");
                                        }
                                    }
                            ));
                }
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void showCustomProgressDialog() {
        mProgressDialog = new Dialog(requireActivity());
        mProgressDialog.setContentView(R.layout.dialog_custom_progress);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}