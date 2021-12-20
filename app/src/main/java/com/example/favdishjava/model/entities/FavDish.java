package com.example.favdishjava.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fav_dishes_table")
public class FavDish implements Parcelable {

    public String image;

    public String getImage() {
        return image;
    }

    public String getImageSource() {
        return imageSource;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public String getDirectionToCook() {
        return directionToCook;
    }

    public Boolean getFavoriteDish() {
        return favoriteDish;
    }

    public Integer getId() {
        return id;
    }

    @ColumnInfo(name = "image_source")
    public String imageSource;
    public String title;

    public void setImage(String image) {
        this.image = image;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public void setDirectionToCook(String directionToCook) {
        this.directionToCook = directionToCook;
    }

    public void setFavoriteDish(Boolean favoriteDish) {
        this.favoriteDish = favoriteDish;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String type;
    public String category;
    public String ingredients;
    @ColumnInfo(name = "cooking_time")
    public String cookingTime;
    @ColumnInfo(name = "instructions")
    public String directionToCook;
    @ColumnInfo(name = "favorite_dish")
    public Boolean favoriteDish = false;
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    public  FavDish(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<FavDish> CREATOR
            = new Parcelable.Creator<FavDish>() {
        public FavDish createFromParcel(Parcel in) {
            return new FavDish(in);
        }

        public FavDish[] newArray(int size) {
            return new FavDish[size];
        }
    };

    private FavDish(Parcel in) {
        id = in.readInt();
        image = in.readString();
        imageSource = in.readString();
        title = in.readString();
        category = in.readString();
        type = in.readString();
        ingredients = in.readString();
        cookingTime = in.readString();
        directionToCook = in.readString();
        favoriteDish = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(image);
        dest.writeString(imageSource);
        dest.writeString(title);
        dest.writeString(category);
        dest.writeString(type);
        dest.writeString(ingredients);
        dest.writeString(cookingTime);
        dest.writeString(directionToCook);
        dest.writeInt(favoriteDish ? 1 : 0);
    }
}
