package com.example.favdishjava.utils;

import java.util.ArrayList;

public class Constants {
    public static final String DISH_TYPE = "DishType";
    public static final String DISH_CATEGORY = "DishCategory";
    public static final String DISH_COOKING_TIME = "DishCookingTime";
    public static final String DISH_IMAGE_SOURCE_LOCAL = "Local";
    public static final String DISH_IMAGE_SOURCE_ONLINE = "Online";
    public static final String EXTRA_DISH_DETAILS = "DishDetails";
    public static final String ALL_ITEMS = "All";
    public static final String FILTER_SELECTION = "FilterSelection";


    public static final String BASE_URL = "https://api.spoonacular.com/";
    public static final String API_ENDPOINT = "recipes/random";

    public static final String API_KEY_VALUE = "433b42674ee94a64ad420e101000eb79";
    public static final String API_KEY = "apiKey";

    public static final String LIMIT_LICENSE = "limitLicense";
    public static final String TAGS = "tags";
    public static final String NUMBER = "number";
    public static final Boolean LIMIT_LICENSE_VALUE = true;
    public static final String TAGS_VALUE = "vegetarian, dessert";
    public static final Integer NUMBER_VALUE = 1;

    public static final String NOTIFICATION_ID = "FavDish_notification_id";
    public static final String NOTIFICATION_NAME = "FavDish";
    public static final String NOTIFICATION_CHANNEL = "FavDish_channel_01";

    /**
     * This function will return the Dish Type List items.
     */
    public static ArrayList<String> dishTypes() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("breakfast");
        list.add("lunch");
        list.add("snacks");
        list.add("dinner");
        list.add("salad");
        list.add("side dish");
        list.add("dessert");
        list.add("other");
        return list;
    }

    /**
     * This function will return the Dish Category list items.
     */
    public static ArrayList<String> dishCategories() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Pizza");
        list.add("BBQ");
        list.add("Bakery");
        list.add("Burger");
        list.add("Cafe");
        list.add("Chicken");
        list.add("Dessert");
        list.add("Drinks");
        list.add("Hot Dogs");
        list.add("Juices");
        list.add("Sandwich");
        list.add("Tea & Coffee");
        list.add("Wraps");
        list.add("Other");
        return list;
    }

    /**
     * This function will return the Dish Cooking Time list items. The time added is in Minutes.
     */
    public static ArrayList<String> dishCookTime() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("10");
        list.add("15");
        list.add("20");
        list.add("30");
        list.add("45");
        list.add("50");
        list.add("60");
        list.add("90");
        list.add("120");
        list.add("150");
        list.add("180");
        return list;
    }
}
