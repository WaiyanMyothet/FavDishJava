package com.example.favdishjava.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.favdishjava.R;
import com.example.favdishjava.application.FavDishApplication;
import com.example.favdishjava.databinding.FragmentAllDishesBinding;
import com.example.favdishjava.databinding.FragmentFavoriteDishesBinding;
import com.example.favdishjava.model.entities.FavDish;
import com.example.favdishjava.view.activities.MainActivity;
import com.example.favdishjava.view.adapters.FavDishAdapter;
import com.example.favdishjava.viewmodel.FavDishViewModel;
import com.example.favdishjava.viewmodel.FavDishViewModelFactory;

import java.util.List;

public class FavoriteDishesFragment extends Fragment {
    private FragmentFavoriteDishesBinding binding;

    private FavDishViewModel mFavDishViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavDishViewModel = ViewModelProviders.of(this, new FavDishViewModelFactory(new FavDishApplication(getActivity().getApplicationContext()).getRepository())).get(FavDishViewModel.class);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (requireActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null)
                activity.showBottomNavigationView();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFavoriteDishesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FavDishAdapter adapter = new FavDishAdapter(this);
        binding.rvFavoriteDishesList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.rvFavoriteDishesList.setAdapter(adapter);
        mFavDishViewModel.favoriteDishes().observe(getViewLifecycleOwner(), new Observer<List<FavDish>>() {
            @Override
            public void onChanged(List<FavDish> favDishes) {
                if (!favDishes.isEmpty()) {
                    binding.rvFavoriteDishesList.setVisibility(View.VISIBLE);
                    binding.tvNoFavoriteDishesAvailable.setVisibility(View.GONE);
                    adapter.dishesList(favDishes);
                } else {
                    binding.rvFavoriteDishesList.setVisibility(View.GONE);
                    binding.tvNoFavoriteDishesAvailable.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void dishDetails(FavDish favDish) {
        if (requireActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null)
                activity.hideBottomNavigationView();
        }
        NavHostFragment.findNavController(this).navigate(AllDishesFragmentDirections.actionAllDishesToDishDetails(favDish));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}