package com.example.favdishjava.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fav_dishes_table")
public class FavDish implements Parcelable {

    public String image;
    @ColumnInfo(name = "image_source")
    public String imageSource;
    public String title;
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
    public Integer id = 0;

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
