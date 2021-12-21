package com.example.favdishjava.view.activities;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.favdishjava.R;
import com.example.favdishjava.application.FavDishApplication;
import com.example.favdishjava.databinding.ActivityAddUpdateDishBinding;
import com.example.favdishjava.databinding.ActivityMainBinding;
import com.example.favdishjava.databinding.DialogCustomImageSelectionBinding;
import com.example.favdishjava.databinding.DialogCustomListBinding;
import com.example.favdishjava.model.database.FavDishRepository;
import com.example.favdishjava.model.entities.FavDish;
import com.example.favdishjava.utils.Constants;
import com.example.favdishjava.view.adapters.CustomListItemAdapter;
import com.example.favdishjava.viewmodel.FavDishViewModel;
import com.example.favdishjava.viewmodel.FavDishViewModelFactory;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddUpdateDishActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int CAMERA = 1;
    public static final int GALLERY = 2;
    public static final String IMAGE_DIRECTORY = "FavDishImages";

    private ActivityAddUpdateDishBinding binding;
    private String mImagePath = "";
    private FavDish mFavDishDetails = null;
    private Dialog mCustomListDialog;

    private FavDishViewModel mFavDishViewModel;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    private  Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddUpdateDishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mFavDishViewModel = ViewModelProviders.of(this, new FavDishViewModelFactory(new FavDishApplication(getApplicationContext()).getRepository())).get(FavDishViewModel.class);
        binding.ivAddDishImage.setOnClickListener(this::onClick);
        binding.etType.setOnClickListener(this::onClick);
        binding.etCategory.setOnClickListener(this::onClick);
        binding.etCookingTime.setOnClickListener(this::onClick);
        binding.btnAddDish.setOnClickListener(this::onClick);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.containsKey(Constants.EXTRA_DISH_DETAILS)) {
                mFavDishDetails = extras.getParcelable(Constants.EXTRA_DISH_DETAILS);
            }
        }
        setUpActionBar();
        if (mFavDishDetails != null) {
            mImagePath = mFavDishDetails.image;
            Glide.with(AddUpdateDishActivity.this)
                    .load(mImagePath)
                    .centerCrop()
                    .into(binding.ivDishImage);
            binding.etTitle.setText(mFavDishDetails.title);
            binding.etType.setText(mFavDishDetails.type);
            binding.etCategory.setText(mFavDishDetails.category);
            binding.etIngredients.setText(mFavDishDetails.ingredients);
            binding.etCookingTime.setText(mFavDishDetails.cookingTime);
            binding.etDirectionToCook.setText(mFavDishDetails.directionToCook);
            binding.btnAddDish.setText(getResources().getString(R.string.lbl_update_dish));
            binding.ivAddDishImage.setImageDrawable(ContextCompat.getDrawable(AddUpdateDishActivity.this, R.drawable.ic_vector_edit));
        }
    }

    private void setUpActionBar() {
        if (getSupportActionBar() != null) {
            if (mFavDishDetails != null && mFavDishDetails.id != 0) {
                getSupportActionBar().setTitle(getResources().getString(R.string.title_edit_dish));
            } else {
                getSupportActionBar().setTitle(getResources().getString(R.string.title_add_dish));
            }
        }
    }

    private void customItemsListDialog(String title, List<String> itemsList, String selection) {
        mCustomListDialog = new Dialog(this);
        DialogCustomListBinding dialogCustomListBinding = DialogCustomListBinding.inflate(getLayoutInflater());
        mCustomListDialog.setContentView(dialogCustomListBinding.getRoot());
        dialogCustomListBinding.tvTitle.setText(title);
        dialogCustomListBinding.rvList.setLayoutManager(new LinearLayoutManager(this));
        dialogCustomListBinding.rvList.setAdapter(new CustomListItemAdapter(this, null, itemsList, selection));
        mCustomListDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialog.dismiss();
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap thumbnail = (Bitmap) extras.get("data");
                    Glide.with(AddUpdateDishActivity.this)
                            .load(thumbnail)
                            .centerCrop()
                            .into(binding.ivDishImage);
                    mImagePath = saveImageToInternalStorage(thumbnail);
                    binding.ivAddDishImage.setImageDrawable(ContextCompat.getDrawable(AddUpdateDishActivity.this, R.drawable.ic_vector_edit));
                }
            } else if (requestCode == GALLERY) {
                if (data != null) {
                    Uri selectedPhotoUri = data.getData();
                    Glide.with(AddUpdateDishActivity.this)
                            .load(selectedPhotoUri)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    Bitmap bm = ((BitmapDrawable) resource).getBitmap();
                                    mImagePath = saveImageToInternalStorage(bm);
                                    return false;
                                }
                            })
                            .into(binding.ivDishImage);
                    binding.ivAddDishImage.setImageDrawable(ContextCompat.getDrawable(AddUpdateDishActivity.this, R.drawable.ic_vector_edit));
                }
            }

        } else if (resultCode == RESULT_CANCELED) {
            Log.e("Cancelled", "Cancelled");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_dish_image:
                customImageSelectionDialog();
                return;
            case R.id.et_type:
                customItemsListDialog(getResources().getString(R.string.title_select_dish_type), Constants.dishTypes(), Constants.DISH_TYPE);
                return;
            case R.id.et_category:
                customItemsListDialog(getResources().getString(R.string.title_select_dish_category), Constants.dishCategories(), Constants.DISH_CATEGORY);
                return;
            case R.id.et_cooking_time:
                customItemsListDialog(getResources().getString(R.string.title_select_dish_cooking_time), Constants.dishCookTime(), Constants.DISH_COOKING_TIME);
                return;
            case R.id.btn_add_dish:
                String title = binding.etTitle.getText().toString().trim();
                String type = binding.etType.getText().toString().trim();
                String category = binding.etCategory.getText().toString().trim();
                String ingredients = binding.etIngredients.getText().toString().trim();
                String cookingInMinutes = binding.etCookingTime.getText().toString().trim();
                String cookingDirection = binding.etDirectionToCook.getText().toString().trim();
                if (mImagePath.isEmpty()) {
                    Toast.makeText(AddUpdateDishActivity.this, getResources().getString(R.string.err_msg_select_dish_image), Toast.LENGTH_SHORT).show();
                } else if (title.isEmpty()) {
                    Toast.makeText(AddUpdateDishActivity.this, getResources().getString(R.string.err_msg_enter_dish_title), Toast.LENGTH_SHORT).show();
                } else if (type.isEmpty()) {
                    Toast.makeText(AddUpdateDishActivity.this, getResources().getString(R.string.err_msg_select_dish_type), Toast.LENGTH_SHORT).show();
                } else if (category.isEmpty()) {
                    Toast.makeText(AddUpdateDishActivity.this, getResources().getString(R.string.err_msg_select_dish_category), Toast.LENGTH_SHORT).show();
                } else if (ingredients.isEmpty()) {
                    Toast.makeText(AddUpdateDishActivity.this, getResources().getString(R.string.err_msg_enter_dish_ingredients), Toast.LENGTH_SHORT).show();
                } else if (cookingInMinutes.isEmpty()) {
                    Toast.makeText(AddUpdateDishActivity.this, getResources().getString(R.string.err_msg_select_dish_cooking_time), Toast.LENGTH_SHORT).show();
                } else if (cookingDirection.isEmpty()) {
                    Toast.makeText(AddUpdateDishActivity.this, getResources().getString(R.string.err_msg_enter_dish_cooking_instructions), Toast.LENGTH_SHORT).show();
                } else {
                    Integer dishID = 0;
                    String imageSource = Constants.DISH_IMAGE_SOURCE_LOCAL;
                    Boolean favoriteDish = false;
                    FavDish favDish = new FavDish();
                    if (mFavDishDetails != null) {
                        dishID = mFavDishDetails.id;
                        favDish.setId(dishID);
                        imageSource = mFavDishDetails.imageSource;
                        favoriteDish = mFavDishDetails.favoriteDish;
                    }

                    favDish.setCategory(category);
                    favDish.setCookingTime(cookingInMinutes);
                    favDish.setDirectionToCook(cookingDirection);
                    favDish.setFavoriteDish(favoriteDish);
                    favDish.setImage(mImagePath);
                    favDish.setImageSource(imageSource);
                    favDish.setIngredients(ingredients);
                    favDish.setTitle(title);
                    favDish.setType(type);
                    if (dishID == 0) {
                        mDisposable.add(mFavDishViewModel.insert(favDish)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> InsertSuccess(),
                                        throwable -> Log.e("Error", "Unable to insert data")));
                    } else {
                        mDisposable.add(mFavDishViewModel.update(favDish)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> UpdateSuccess(),
                                        throwable -> Log.e("Error", "Unable to update data")));
                    }
                }
                return;
            default:
                break;
        }
    }

    private void customImageSelectionDialog() {
        dialog = new Dialog(this);
        DialogCustomImageSelectionBinding imgBinding = DialogCustomImageSelectionBinding.inflate(getLayoutInflater());
        dialog.setContentView(imgBinding.getRoot());
        imgBinding.tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(AddUpdateDishActivity.this)
                        .withPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        showRationalDialogForPermissions();
                    }
                }).onSameThread()
                        .check();
            }
        });
        imgBinding.tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(AddUpdateDishActivity.this)
                        .withPermission(
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        ).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), GALLERY);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(AddUpdateDishActivity.this,
                                "You have denied the storage permission to select image.",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        showRationalDialogForPermissions();
                    }
                }).onSameThread()
                        .check();
            }
        });
        dialog.show();
    }

    private void showRationalDialogForPermissions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings");
        builder.setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri url = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(url);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        builder.show();
    }

    public void selectedListItem(String item, String selection) {
        switch (selection) {
            case Constants.DISH_TYPE:
                mCustomListDialog.dismiss();
                binding.etType.setText(item);
                return;
            case Constants.DISH_CATEGORY:
                mCustomListDialog.dismiss();
                binding.etCategory.setText(item);
                return;
            case Constants.DISH_COOKING_TIME:
                mCustomListDialog.dismiss();
                binding.etCookingTime.setText(item);
                return;
        }
    }

    private String saveImageToInternalStorage(Bitmap bitmap) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        File directory = cw.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE);
        UUID uuid=UUID.randomUUID();
        File mypath = new File(directory, uuid+".jpg");

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stream.flush();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    private void InsertSuccess() {
        Toast.makeText(AddUpdateDishActivity.this,
                "You successfully added your favorite dish details.",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    private void UpdateSuccess() {
        Toast.makeText(AddUpdateDishActivity.this,
                "You successfully added your favorite dish details.",
                Toast.LENGTH_SHORT).show();
        finish();
    }

}