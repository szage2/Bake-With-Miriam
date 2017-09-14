package com.example.szage.bakewithmiriam.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Ingredient class contains the ingredient,
 * it's name, measure and quantity for preparing the recipe and their getter methods.
 */

public class Ingredient implements Parcelable{

    // Ingredient name of the recipe
    private String mIngredientName;
    // Unit of the ingredient
    private String mMeasure;
    // Quantity of the ingredient
    private String mQuantity;


    public Ingredient(String ingredient, String measure, String quantity) {
        mIngredientName = ingredient;
        mMeasure = measure;
        mQuantity = quantity;
    }

    protected Ingredient(Parcel in) {
        mIngredientName = in.readString();
        mMeasure = in.readString();
        mQuantity = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mIngredientName);
        dest.writeString(mMeasure);
        dest.writeString(mQuantity);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    /**
     * @return the ingredient's name
     */
    public String getIngredientName() {
        return mIngredientName;
    }

    /**
     * @return the ingredient's unit needed
     */
    public String getMeasure() {
        return mMeasure;
    }

    /**
     * @return the ingredient's quantity needed
     */
    public String getQuantity() {
        return mQuantity;
    }

}
