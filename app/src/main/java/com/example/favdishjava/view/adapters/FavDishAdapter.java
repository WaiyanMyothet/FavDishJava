package com.example.favdishjava.view.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.favdishjava.databinding.ItemCustomListLayoutBinding;
import com.example.favdishjava.databinding.ItemDishLayoutBinding;
import com.example.favdishjava.model.entities.FavDish;
import com.example.favdishjava.view.activities.AddUpdateDishActivity;
import com.example.favdishjava.view.fragments.AllDishesFragment;

import java.util.ArrayList;
import java.util.List;

public class FavDishAdapter extends RecyclerView.Adapter<FavDishAdapter.ViewHolder> {
    private Fragment fragment;
    private List<FavDish> dishes=new ArrayList<FavDish>();
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
        FavDish dish=dishes.get(position);
        Glide.with(fragment).load(dish.image).into(holder.binding.ivDishImage);
        holder.binding.tvDishTitle.setText(dish.title);
       // holder.itemView.setOnClickListener();

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
