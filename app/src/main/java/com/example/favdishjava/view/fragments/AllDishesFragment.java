package com.example.favdishjava.view.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.favdishjava.R;
import com.example.favdishjava.application.FavDishApplication;
import com.example.favdishjava.databinding.ActivityAddUpdateDishBinding;
import com.example.favdishjava.databinding.DialogCustomListBinding;
import com.example.favdishjava.databinding.FragmentAllDishesBinding;
import com.example.favdishjava.model.entities.FavDish;
import com.example.favdishjava.utils.Constants;
import com.example.favdishjava.view.activities.AddUpdateDishActivity;
import com.example.favdishjava.view.activities.MainActivity;
import com.example.favdishjava.view.adapters.CustomListItemAdapter;
import com.example.favdishjava.view.adapters.FavDishAdapter;
import com.example.favdishjava.viewmodel.FavDishViewModel;
import com.example.favdishjava.viewmodel.FavDishViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class AllDishesFragment extends Fragment {

    private FavDishViewModel mFavDishViewModel;
    private Dialog mCustomListDialog;
    private FragmentAllDishesBinding binding;
    private FavDishAdapter adapter;
    private final CompositeDisposable mDisposable = new CompositeDisposable();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavDishViewModel = ViewModelProviders.of(this, new FavDishViewModelFactory(new FavDishApplication(getActivity().getApplicationContext()).getRepository())).get(FavDishViewModel.class);

        setHasOptionsMenu(true);
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
        binding = FragmentAllDishesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new FavDishAdapter(this);
        binding.rvDishesList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.rvDishesList.setAdapter(adapter);
        mFavDishViewModel.allDishesList().observe(getViewLifecycleOwner(), new Observer<List<FavDish>>() {
            @Override
            public void onChanged(List<FavDish> favDishes) {
                if (!favDishes.isEmpty()) {
                    binding.rvDishesList.setVisibility(View.VISIBLE);
                    binding.tvNoDishesAddedYet.setVisibility(View.GONE);
                    adapter.dishesList(favDishes);
                } else {
                    binding.rvDishesList.setVisibility(View.GONE);
                    binding.tvNoDishesAddedYet.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_filter_dishes:
                filterDishesListDialog();
                return true;
            case R.id.action_add_dish:
                startActivity(new Intent(getContext(), AddUpdateDishActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void filterDishesListDialog() {
        mCustomListDialog = new Dialog(requireActivity());
        DialogCustomListBinding dialogCustomListBinding = DialogCustomListBinding.inflate(getLayoutInflater());
        mCustomListDialog.setContentView(dialogCustomListBinding.getRoot());
        dialogCustomListBinding.tvTitle.setText(getResources().getString(R.string.title_select_item_to_filter));
        dialogCustomListBinding.rvList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        ArrayList<String> dishTypes = Constants.dishTypes();
        dishTypes.add(0, Constants.ALL_ITEMS);
        dialogCustomListBinding.rvList.setAdapter(new CustomListItemAdapter(requireActivity(), AllDishesFragment.this, dishTypes, Constants.FILTER_SELECTION));
        mCustomListDialog.show();
    }

    public void filterSelection(String filterItemSelection) {
        mCustomListDialog.dismiss();
        if (filterItemSelection == Constants.ALL_ITEMS) {
            mFavDishViewModel.allDishesList().observe(getViewLifecycleOwner(), new Observer<List<FavDish>>() {
                @Override
                public void onChanged(List<FavDish> favDishes) {
                    if (!favDishes.isEmpty()) {
                        binding.rvDishesList.setVisibility(View.VISIBLE);
                        binding.tvNoDishesAddedYet.setVisibility(View.GONE);
                        adapter.dishesList(favDishes);
                    } else {
                        binding.rvDishesList.setVisibility(View.GONE);
                        binding.tvNoDishesAddedYet.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            mFavDishViewModel.getFilteredList(filterItemSelection).observe(getViewLifecycleOwner(), new Observer<List<FavDish>>() {
                @Override
                public void onChanged(List<FavDish> favDishes) {
                    if (!favDishes.isEmpty()) {
                        binding.rvDishesList.setVisibility(View.VISIBLE);
                        binding.tvNoDishesAddedYet.setVisibility(View.GONE);
                        adapter.dishesList(favDishes);
                    } else {
                        binding.rvDishesList.setVisibility(View.GONE);
                        binding.tvNoDishesAddedYet.setVisibility(View.VISIBLE);
                    }
                }
            });

        }

    }

    public void deleteDish(FavDish dish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(getResources().getString(R.string.title_delete_dish));
        builder.setMessage(getResources().getString(R.string.msg_delete_dish_dialog, dish.title));
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(getResources().getString(R.string.lbl_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDisposable.add(mFavDishViewModel.delete(dish)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> dialog.dismiss(),
                                throwable -> Log.e("Error", "Unable to delete data")));

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.lbl_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        builder.show();
    }

    public void dishDetails(FavDish favDish) {
        if (requireActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            if (activity != null)
                activity.hideBottomNavigationView();
        }
        NavHostFragment.findNavController(this).navigate(AllDishesFragmentDirections.actionAllDishesToDishDetails(favDish));
    }
}