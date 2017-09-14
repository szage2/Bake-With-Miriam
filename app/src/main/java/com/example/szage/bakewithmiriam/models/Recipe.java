package com.example.szage.bakewithmiriam.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Recipe class containing the recipe and getter methods for it's name, servings, image URL
 * and 2 lists, recipe ingredients and steps of preparation.
 */

public class Recipe implements Parcelable {

    // Name of the recipe
    private String mRecipeName;
    // Servings of the recipe
    private String mServings;
    // Image URL of the recipe
    private String mImageURL;
    // List of recipe ingredients
    private ArrayList<Ingredient> mIngredientList = new ArrayList<>();
    // List of preparation steps
    private ArrayList<Step> mStepList = new ArrayList<>();

    public Recipe(String recipeName, String servings, String imageURL, ArrayList<Ingredient> ingredientList, ArrayList<Step> stepList) {
        mRecipeName = recipeName;
        mServings = servings;
        mImageURL = imageURL;
        mIngredientList = ingredientList;
        mStepList = stepList;
    }

    protected Recipe(Parcel in) {
        mRecipeName = in.readString();
        mServings = in.readString();
        mImageURL = in.readString();
        if (in.readByte() == 0x01) {
            mIngredientList = new ArrayList<Ingredient>();
            in.readList(mIngredientList, Ingredient.class.getClassLoader());
        } else {
            mIngredientList = null;
        }
        if (in.readByte() == 0x01) {
            mStepList = new ArrayList<Step>();
            in.readList(mStepList, Step.class.getClassLoader());
        } else {
            mStepList = null;
        }
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mRecipeName);
        dest.writeString(mServings);
        dest.writeString(mImageURL);
        if (mIngredientList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mIngredientList);
        }
        if (mStepList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mStepList);
        }
    }

    /**
     * @return the recipe's name
     */
    public String getRecipeName() {
        return mRecipeName;
    }

    /**
     * @return the recipe's servings
     */
    public String getServings() {
        return mServings;
    }

    /**
     * @return the recipe's image URL
     */
    public String getImageURL() {
        return mImageURL;
    }

    /**
     * @return the recipe's Ingredient list
     */
    public ArrayList<Ingredient> getIngredientList() {
        return mIngredientList;
    }

    /**
     * @return the recipe's preparation steps
     */
    public ArrayList<Step> getStepList() {
        return mStepList;
    }
}
