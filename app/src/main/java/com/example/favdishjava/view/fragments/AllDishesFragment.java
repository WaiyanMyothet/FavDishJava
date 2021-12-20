package com.example.favdishjava.view.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

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

public class AllDishesFragment extends Fragment {

    private FavDishViewModel mFavDishViewModel;
    private Dialog mCustomListDialog;
    private FragmentAllDishesBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavDishViewModel = ViewModelProviders.of(this, new FavDishViewModelFactory(new FavDishApplication(getActivity().getApplicationContext()).getRepository())).get(FavDishViewModel.class);

        setHasOptionsMenu(true);
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
        binding.rvDishesList.setLayoutManager(new GridLayoutManager(getActivity(),2));
        FavDishAdapter adapter=new FavDishAdapter(this);
        binding.rvDishesList.setAdapter(adapter);
        mFavDishViewModel.allDishesList().observe(getViewLifecycleOwner(), new Observer<List<FavDish>>() {
            @Override
            public void onChanged(List<FavDish> favDishes) {
                Log.e("onchanged", String.valueOf(favDishes.size()));
                if(!favDishes.isEmpty()){
                    Log.e("onchanged", "if");
                    binding.rvDishesList.setVisibility(View.VISIBLE);
                    binding.tvNoDishesAddedYet.setVisibility(View.GONE);
                    adapter.dishesList(favDishes);
                }
                else{
                    Log.e("onchanged", "else");
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

    public void filterSelection(String item) {
        mCustomListDialog.dismiss();

    }
}