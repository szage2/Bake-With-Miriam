package com.example.szage.bakewithmiriam;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.szage.bakewithmiriam.models.Ingredient;
import com.example.szage.bakewithmiriam.models.Recipe;
import com.example.szage.bakewithmiriam.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Query Utils is responsible for creating network connectivity and requesting
 * JSON data from source. It also performs data parsing.
 */

public class QueryUtils {

    private static final String TAG = QueryUtils.class.getSimpleName();

    private static final String BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    // Create an empty array lists, objects will be added to
    private static ArrayList<Recipe> mRecipes = new ArrayList<>();
    private static ArrayList<Ingredient> mIngredients;
    private static ArrayList<Step> mSteps;

    // Fields for extracting JSON data
    private final static String RECIPE_NAME = "name";
    private final static String RECIPE_IMAGE = "image";
    private final static String RECIPE_SERVINGS = "servings";
    private final static String RECIPE_INGREDIENT_LIST = "ingredients";
    private final static String RECIPE_INGREDIENT = "ingredient";
    private final static String INGREDIENT_MEASURE = "measure";
    private final static String INGREDIENT_QUANTITY = "quantity";
    private final static String RECIPE_STEP_List = "steps";
    private final static String STEP_SHORT_DESCRIPTION = "shortDescription";
    private final static String STEP_DESCRIPTION = "description";
    private final static String STEP_VIDEO_URL = "videoURL";
    private final static String STEP_THUMBNAIL_URL = "thumbnailURL";

    private static Response response = null;

    /**
     * Creates the connectivity and request json data.
     *
     * @param context is the context
     * @return response is the json response that contains all the recipe data
     */
    public static Response getJsonData(Context context) {

        // Creating connectivity
        try {
            OkHttpClient client = new OkHttpClient();

            // Request of JSON data
            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .build();

            // Response for the request
            response = client.newCall(request).execute();

            checkConnectivity(context);

            // Handle if error occured
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return JSON response;
        return response;
    }

    /**
     * Provides information about the state of network connection (connected or not)
     *
     * @param context is the current context
     * @return true if connected to internet
     */
    private static boolean checkConnectivity(Context context) {
        try {
            // Get the connectivity service
            ConnectivityManager  cm = (ConnectivityManager)
                    context.getSystemService(context.CONNECTIVITY_SERVICE);

            // Query active network connection
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            // Tells us if we have active network
            boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
            Log.i(TAG, "Device is connected to network: " + isConnected);

            // if no network is available networkInfo will be null
            // otherwise check if we are connected
            return isConnected;

            // Otherwise Log the error message and return false
        } catch (Exception ex) {
            Log.e(TAG, "Device is not connected to network. Caused by: " + ex);
            return false;
        }
    }

    /**
     * Extract JSON data, creating List of Recipe Objects.
     *
     * @param response is the json response that contains recipe details
     * @return  mRecipes a list of recipes
     * @throws IOException error that occured during connectivity
     * @throws JSONException error that occured during parsing
     */
    public static ArrayList<Recipe> getRecipesData(Response response) throws IOException, JSONException {

        // Check if the response was successful and it's not null
        if (response != null && response.isSuccessful()) {

            // Clear mRecipes list to make sure items won't be duplicated
            mRecipes.clear();

            String jsonData = response.body().string();

            JSONArray jsonResponseArray = new JSONArray(jsonData);

            // Iterating through jsonResponseArray to get all recipes
            for (int i = 0; i < jsonResponseArray.length(); i++) {

                JSONObject recipeObject = jsonResponseArray.getJSONObject(i);

                String recipeName = recipeObject.getString(RECIPE_NAME);
                String imageUrl = recipeObject.getString(RECIPE_IMAGE);
                String servings = recipeObject.getString(RECIPE_SERVINGS);

                Log.i(TAG, "recipe name is " + recipeName);

                // Call methods for create Lists of Ingredient and Step objects
                getIngredientData(recipeObject);
                getStepData(recipeObject);

                // Creating a new instance of Recipe object
                Recipe recipe = new Recipe(recipeName, servings, imageUrl, mIngredients, mSteps);

                // Adding recipe object to the list of recipes
                mRecipes.add(recipe);

                Log.i(TAG, "mRecipes size is " + mRecipes.size());
            }
        }
        // Return the list of recipes
        return mRecipes;
    }


    /**
     * Extract JSON data, creating List of Ingredient Objects.
     *
     * @param recipeObject is the Json object containing all recipe details
     * @throws JSONException error that occured during parsing
     */
    private static void getIngredientData(JSONObject recipeObject) throws JSONException {
        // Extract Ingredient objects into an ArrayList
        JSONArray ingredientsArray = recipeObject.getJSONArray(RECIPE_INGREDIENT_LIST);

        // Creating empty ArrayLists for Ingredients and Steps
        mIngredients = new ArrayList<>();
        mSteps = new ArrayList<>();

        // Iterating through ingredientsArray to get all ingredients
        for (int j = 0; j < ingredientsArray.length(); j++) {
            JSONObject currentIngredient = ingredientsArray.getJSONObject(j);

            String ingredientName = currentIngredient.getString(RECIPE_INGREDIENT);
            String measure = currentIngredient.getString(INGREDIENT_MEASURE);
            String quantity = currentIngredient.getString(INGREDIENT_QUANTITY);

            // Creating a new instance of Ingredient object
            Ingredient ingredient = new Ingredient(ingredientName, measure, quantity);

            // Adding ingredient object to the list of ingredients
            mIngredients.add(ingredient);
        }
    }

    /**
     * Extract JSON data, creating List of Step Objects.
     *
     * @param recipeObject is the Json object containing all recipe details
     * @throws JSONException error that occured during parsing
     */
    private static void getStepData(JSONObject recipeObject) throws JSONException {
        // Extract Step objects into an ArrayList
        JSONArray stepsArray = recipeObject.getJSONArray(RECIPE_STEP_List);

        // Iterating through stepsArray to get all preparation steps
        for (int k = 0; k < stepsArray.length(); k++) {
            JSONObject currentSteps = stepsArray.getJSONObject(k);

            String shortDescription = currentSteps.getString(STEP_SHORT_DESCRIPTION);
            String description = currentSteps.getString(STEP_DESCRIPTION);
            String videoURL = currentSteps.getString(STEP_VIDEO_URL);
            String thumbnailURL = currentSteps.getString(STEP_THUMBNAIL_URL);

            // Creating a new instance of Step object
            Step step = new Step(shortDescription, description, videoURL, thumbnailURL);

            // Adding step object to the list of steps
            mSteps.add(step);
        }
    }
}
