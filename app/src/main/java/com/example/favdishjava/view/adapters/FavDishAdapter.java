package com.example.favdishjava.view.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.favdishjava.R;
import com.example.favdishjava.databinding.ItemCustomListLayoutBinding;
import com.example.favdishjava.databinding.ItemDishLayoutBinding;
import com.example.favdishjava.model.entities.FavDish;
import com.example.favdishjava.utils.Constants;
import com.example.favdishjava.view.activities.AddUpdateDishActivity;
import com.example.favdishjava.view.fragments.AllDishesFragment;
import com.example.favdishjava.view.fragments.FavoriteDishesFragment;

import java.util.ArrayList;
import java.util.List;

public class FavDishAdapter extends RecyclerView.Adapter<FavDishAdapter.ViewHolder> {
    private Fragment fragment;
    private List<FavDish> dishes = new ArrayList<FavDish>();

    public FavDishAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemDishLayoutBinding binding;

        public ViewHolder(ItemDishLayoutBinding root) {
            super(root.getRoot());
            binding = root;
        }
    }

    @NonNull
    @Override
    public FavDishAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemDishLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavDish dish = dishes.get(position);
        Glide.with(fragment).load(dish.image).into(holder.binding.ivDishImage);
        holder.binding.tvDishTitle.setText(dish.title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment instanceof AllDishesFragment){
                    ((AllDishesFragment) fragment).dishDetails(dish);
                }
                else if(fragment instanceof FavoriteDishesFragment){
                }
            }
        });

        if(fragment instanceof AllDishesFragment){
           holder.binding.ibMore.setVisibility(View.VISIBLE);
        }
        else if(fragment instanceof FavoriteDishesFragment){
            holder.binding.ibMore.setVisibility(View.GONE);
        }

        holder.binding.ibMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(fragment.getContext(), holder.binding.ibMore);
                popupMenu.getMenuInflater().inflate(R.menu.menu_adapter, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_edit_dish) {
                            Intent intent = new Intent(fragment.requireActivity(), AddUpdateDishActivity.class);
                            intent.putExtra(Constants.EXTRA_DISH_DETAILS, dish);
                            fragment.requireActivity().startActivity(intent);
                        } else if (item.getItemId() == R.id.action_delete_dish) {
                            if (fragment instanceof AllDishesFragment) {
                                ((AllDishesFragment) fragment).deleteDish(dish);
                            }
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public void dishesList(List<FavDish> list) {
        dishes = list;
        notifyDataSetChanged();
    }
}
