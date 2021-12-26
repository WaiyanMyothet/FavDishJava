package com.example.favdishjava.model.entities;

import java.util.List;

public class RandomDish {
    public class Recipes {
        public List<Recipe> recipes;
    }

    public class Recipe {
        public int aggregateLikes;
        public List<AnalyzedInstruction> analyzedInstructions;
        public boolean cheap;
        public String creditsText;
        public List<Object> cuisines;
        public boolean dairyFree;
        public List<String > diets;
        public List<String> dishTypes;
        public List<ExtendedIngredient> extendedIngredients;
        public String gaps;
        public boolean glutenFree;
        public double healthScore;
        public int id;
        public String image;
        public String imageType;
        public String instructions;
        public String license;
        public boolean lowFodmap;
        public List<String> occasions;
        public Object originalId;
        public double pricePerServing;
        public int readyInMinutes;
        public int servings;
        public String sourceName;
        public String sourceUrl;
        public double spoonacularScore;
        public String spoonacularSourceUrl;
        public String summary;
        public boolean sustainable;
        public String title;
        public boolean vegan;
        public boolean vegetarian;
        public boolean veryHealthy;
        public boolean veryPopular;
        public int weightWatcherSmartPoints;
    }

    public class AnalyzedInstruction {
        public String name;
        public List<Step> steps;
    }

    public class ExtendedIngredient {

        public String aisle;
        public double amount;

        public String consistency;
        public int id;

        public String image;

        public RandomDish.Measures measures;

        public List<String> meta;

        public List<String> metaInformation;

        public String name;

        public String nameClean;

        public String original;

        public String originalName;

        public String originalString;

        public String unit;

    }

    public class Step {
        public String step;
        public int number;
        public Length length;
        public List<Ingredient> ingredients;
        public List<Equipment> equipment;
    }

    public class Equipment {
        public int id;
        public String image;
        public String localizedName;
        public String name;
    }

    public class Ingredient {
        public int id;
        public String image;
        public String localizedName;
        public String name;
    }

    public class Length {
        public int number;
        public String unit;
    }

    public class Measures {
        public Metric metric;
        public Us us;
    }

    public class Metric {
        public Double amount;
        public String unitLong;
        public String unitShort;
    }

    public class Us {
        public Double amount;
        public String unitLong;
        public String unitShort;
    }

}
