package com.example.szage.bakewithmiriam.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.szage.bakewithmiriam.QueryUtils;
import com.example.szage.bakewithmiriam.R;
import com.example.szage.bakewithmiriam.fragments.RecipeFragment;
import com.example.szage.bakewithmiriam.models.Recipe;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

/**
 * Recipe Activity requests (@QueryUtils) asynchronously available recipes
 * and passes Recipe ArrayList with Recipe objects.
 */

public class RecipeActivity extends AppCompatActivity {

    private static final String TAG = RecipeActivity.class.getSimpleName();
    private ArrayList<Recipe> mRecipeList;
    private RecipeFragment mRecipeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // Create an Empty array list for recipes
        mRecipeList = new ArrayList<>();

        // Make sure that Recipe task only gets called when the activity is created
        if (savedInstanceState != null) {
            Log.i(TAG, "savedInstanceState is not null");
        } runRecipeTask();
    }

    // Method for running Recipe Task
    public void runRecipeTask() { new RecipeTask().execute(); }

    // Activity passes the list of recipes to it's Fragment when it gets created
    public void sendDataToFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("RecipeList", mRecipeList);
        // New instance of Recipe Fragment
        mRecipeFragment =  new RecipeFragment();
        // Set the bundle with desired data as arguments of the fragment
        mRecipeFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.recipe_fragment, mRecipeFragment)
                .commit();
    }


    /**
     *  Recipe Task is responsible for querying the data asynchronously.
     */
    public class RecipeTask extends AsyncTask<Void, Void, ArrayList> {

        /**
         * Query JSON data in the background Thread.
         *
         * @return mRecipeList that is a list of Recipe objects
         */
        @Override
        protected ArrayList<Recipe> doInBackground(Void... voids) {

            Log.i(TAG, "Recipe task is running");

            // Get the application context
            Context context = getApplicationContext();

            // Get the JSON response by calling method getJsonData
            Response jsonResponse = QueryUtils.getJsonData(context);

            try {

                // get the recipe list by calling method getRecipesData
                mRecipeList = QueryUtils.getRecipesData(jsonResponse);

                // Send the recipe list to the fragment.
                sendDataToFragment();

                // return the list of recipe objects
                return mRecipeList;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Save the state of code when device gets rotated.
     *
     * @param outState holds the data we want to save.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mRecipeList", mRecipeList);
    }

    /**
     * Restore the state of code after device rotation.
     *
     * @param savedInstanceState has saved (state of) data.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipeList = savedInstanceState.getParcelableArrayList("mRecipeList");
    }
}
