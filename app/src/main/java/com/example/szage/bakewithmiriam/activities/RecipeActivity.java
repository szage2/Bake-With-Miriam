package com.example.szage.bakewithmiriam.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView noInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // Make sure that Recipe task only gets called when the activity is created
        if (savedInstanceState == null) {
            // Create an Empty array list for recipes
            mRecipeList = new ArrayList<>();
            runRecipeTask();
        } else {
            // Restore the saved state of code
            mRecipeList = savedInstanceState.getParcelableArrayList("mRecipeList");
        }

        // Find the view in the layout for noInternet text view
        noInternet = (TextView) findViewById(R.id.no_internet_message);
    }

    // Method for running Recipe Task
    public void runRecipeTask() { new RecipeTask().execute();}

    // Activity passes the list of recipes to it's Fragment when it gets created
    public void sendDataToFragment() {
        Log.i(TAG, "sendDataToFragment is called");
        // New instance of Recipe Fragment
        RecipeFragment recipeFragment;
        recipeFragment =  new RecipeFragment();
        // Create new Bundle
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("RecipeList", mRecipeList);
        // Set the bundle with desired data as arguments of the fragment
        recipeFragment.setArguments(bundle);
        // Begin fragment transaction
        getSupportFragmentManager().
                beginTransaction().replace(R.id.recipe_fragment, recipeFragment).commit();
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

                // Run this method on the UI thread
                // to be able to set visibility and toast message if needed
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setNotificationVisibility();
                    }
                });

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
     * Show error and toast message depending on the size of the Recipe List
     */
    private void setNotificationVisibility() {
        if (mRecipeList.size() != 0) {
            // If the Recipe List is not empty, hide the error message
            noInternet.setVisibility(View.GONE);
        } else {
            // If the Recipe List is empty, show a message of the error
            noInternet.setVisibility(View.VISIBLE);
            // Ask user to check connectivity in a toast message
            Toast.makeText(getApplicationContext(), R.string.check_connectivity,
                    Toast.LENGTH_SHORT).show();
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
}
