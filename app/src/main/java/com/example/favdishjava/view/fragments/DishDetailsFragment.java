package com.example.favdishjava.view.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.palette.graphics.Palette;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.favdishjava.R;
import com.example.favdishjava.application.FavDishApplication;
import com.example.favdishjava.databinding.FragmentAllDishesBinding;
import com.example.favdishjava.databinding.FragmentDishDetailsBinding;
import com.example.favdishjava.model.entities.FavDish;
import com.example.favdishjava.utils.Constants;
import com.example.favdishjava.view.activities.AddUpdateDishActivity;
import com.example.favdishjava.viewmodel.FavDishViewModel;
import com.example.favdishjava.viewmodel.FavDishViewModelFactory;

import java.io.IOException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DishDetailsFragment extends Fragment {

    private FragmentDishDetailsBinding binding;
    private FavDishViewModel mFavDishViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    FavDish favDishDetails = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavDishViewModel = ViewModelProviders.of(this, new FavDishViewModelFactory(new FavDishApplication().getRepository())).get(FavDishViewModel.class);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDishDetailsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favDishDetails = DishDetailsFragmentArgs.fromBundle(getArguments()).getDishDetails();
        if (favDishDetails != null) {
            try {
                Glide.with(requireActivity())
                        .load(favDishDetails.image)
                        .centerCrop()
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e("Tag", "Error Loading Image", e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Bitmap bm = ((BitmapDrawable) resource).getBitmap();
                                Palette.from(bm).generate(new Palette.PaletteAsyncListener() {
                                    public void onGenerated(Palette p) {
                                        Integer intColor = 0;
                                        if (p.getVibrantSwatch() != null) {
                                            intColor = p.getVibrantSwatch().getRgb();
                                        }
                                        binding.rlDishDetailMain.setBackgroundColor(intColor);
                                    }
                                });
                                return false;
                            }
                        })
                        .into(binding.ivDishImage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            binding.tvTitle.setText(favDishDetails.title);
            binding.tvType.setText(favDishDetails.type);
            binding.tvCategory.setText(favDishDetails.category);
            binding.tvIngredients.setText(favDishDetails.ingredients);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.tvCookingDirection.setText(Html.fromHtml(favDishDetails.directionToCook, Html.FROM_HTML_MODE_COMPACT));
            } else
                binding.tvCookingDirection.setText(Html.fromHtml(favDishDetails.directionToCook));
            binding.tvCookingTime.setText(getResources().getString(R.string.lbl_estimate_cooking_time, favDishDetails.cookingTime));
            if (favDishDetails.favoriteDish) {
                binding.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_favorite_selected));
            } else
                binding.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_favorite_unselected));

            binding.ivFavoriteDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favDishDetails.favoriteDish = !favDishDetails.favoriteDish;
                    mDisposable.add(mFavDishViewModel.update(favDishDetails)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> UpdateSuccess(favDishDetails.favoriteDish),
                                    throwable -> Log.e("Error", "Unable to update data")));
                }
            });
        }

    }

    private void UpdateSuccess(Boolean favorite) {
        if (favorite) {
            binding.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_favorite_selected));
            Toast.makeText(requireActivity(), getResources().getString(R.string.msg_added_to_favorites), Toast.LENGTH_SHORT).show();
        } else {
            binding.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_favorite_unselected));
            Toast.makeText(requireActivity(), getResources().getString(R.string.msg_removed_from_favorite), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_share_dish:
                String type = "text/plain";
                String subject = "Checkout this dish recipe";
                String extraText = "";
                String shareWith = "Share with";

                if (favDishDetails != null) {
                    String image = "";
                    if (favDishDetails.imageSource == Constants.DISH_IMAGE_SOURCE_ONLINE) {
                        image = favDishDetails.image;
                    }
                    String cookingInstructions = "";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cookingInstructions = Html.fromHtml(favDishDetails.directionToCook, Html.FROM_HTML_MODE_COMPACT).toString();
                    } else
                        cookingInstructions = Html.fromHtml(favDishDetails.directionToCook).toString();
                    extraText=image+"\n"+"Title: "+favDishDetails.title+"\n\n Type: "+favDishDetails.type+"\n\n Instruction To Cook: "+cookingInstructions;
                }

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType(type);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, extraText);
                startActivity(Intent.createChooser(intent, shareWith));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}