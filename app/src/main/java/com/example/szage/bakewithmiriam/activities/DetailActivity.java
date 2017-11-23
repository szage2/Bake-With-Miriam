package com.example.szage.bakewithmiriam.activities;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.szage.bakewithmiriam.fragments.DetailFragment;
import com.example.szage.bakewithmiriam.R;
import com.example.szage.bakewithmiriam.fragments.StepFragment;
import com.example.szage.bakewithmiriam.models.Ingredient;
import com.example.szage.bakewithmiriam.models.Recipe;
import com.example.szage.bakewithmiriam.models.Step;
import com.example.szage.bakewithmiriam.widget.RecipeWidgetProvider;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.example.szage.bakewithmiriam.widget.RecipeWidgetProvider.ACTION_UPDATE;

/**
 * Detail Activity passes the Recipe Object to it's fragment.
 */

public class DetailActivity extends AppCompatActivity implements DetailFragment.OnStepClickListener {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private DetailFragment mDetailFragment = DetailFragment.newInstance(this);
    private StepFragment mStepFragment;
    private Recipe mRecipe;
    ArrayList<Step> mSteps = new ArrayList<>();
    private boolean mTwoPane;
    private int mStepListIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Check if it's two pane mode
        if (findViewById(R.id.step_linear_layout) != null) {
            mTwoPane = true;
        } else mTwoPane = false;

        // Get the intent that launched this activity with it's extras
        Intent intentThatStartedDetailActivity = getIntent();
        Bundle extras = intentThatStartedDetailActivity.getExtras();

        // If there's extra data provide it to activity's fragment
        if (extras != null) {
            mRecipe = (Recipe) extras.get("recipe");
            String recipeName = mRecipe.getRecipeName();

            // set the recipe name as title of the support action bar
            this.getSupportActionBar().setTitle(recipeName);

            sendDataToDetailFragment();

            // in case of two pane mode, send the step list to Step Fragment to avoid white screen
            // But only if there's not saved state of instance
            if (mTwoPane == true && savedInstanceState == null) {
                sendDataToStepFragment();
            }
            // Send data to widget's Provider class
            sendDataToWidget();
            // Call method updateWidget
            updateWidget(mRecipe);
        }
    }

    // Activity passes the Recipe object to Detail Fragment when it gets created
    public void sendDataToDetailFragment() {
        Bundle detailBundle = new Bundle();
        detailBundle.putParcelable("recipe", mRecipe);
        detailBundle.putBoolean("twoPane", mTwoPane);
        // New instance of Detail Fragment
        mDetailFragment =  new DetailFragment();
        // Set the bundle with desired data as arguments of the fragment
        mDetailFragment.setArguments(detailBundle);
        // Fragment transaction
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_fragment, mDetailFragment).commit();
    }

    // Activity passes the steps array list to Step Fragment when it gets created
    public void sendDataToStepFragment() {
        // Get the steps Array list
        mSteps = mRecipe.getStepList();
        Bundle stepBundle = new Bundle();
        stepBundle.putParcelableArrayList("steps", mSteps);
        // Inform the fragment about two pane mode
        stepBundle.putBoolean("twoPane", mTwoPane);
        // and send the position of the selected step
        stepBundle.putInt("stepListIndex", mStepListIndex);
        // New instance of Step Fragment
        mStepFragment = new StepFragment();
        // Set the bundle with desired data as arguments of the fragment
        mStepFragment.setArguments(stepBundle);
        // Fragment transaction
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_fragment, mStepFragment).commit();
    }

    /**
     * Broadcast the selected recipe to the widget
     */
    public void sendDataToWidget() {
        Log.i(TAG, "sendDataToWidget is called");
        // Create an intent that will be used for Recipe Widget Provider
        Intent widgetIntent = new Intent(DetailActivity.this, RecipeWidgetProvider.class);
        // Set the update action on the intent
        widgetIntent.setAction(ACTION_UPDATE);
        // Attach extra data, the selected Recipe object
        widgetIntent.putExtra("recipe", mRecipe);
        // Broadcast it to the widget
        sendBroadcast(widgetIntent);
    }

    // Once widget is updated
    public void updateWidget(Recipe recipe) {
        // Get the application context
        Context context = getApplicationContext();
        // get the recipe's ingredient list and convert it go json
        Gson gson = new Gson();
        String json = gson.toJson(recipe.getIngredientList());
        // Edit shared preferences and store json format of ingredients
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("savedJsonFormat", json).apply();
    }

    /**
     * Save the state of code when device gets rotated.
     *
     * @param outState holds the data we want to save.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipe", mRecipe);
        outState.putInt("listIndex", mStepListIndex);
    }

    /**
     * Restore the state of code after device rotation.
     *
     * @param savedInstanceState has saved (state of) data.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipe = savedInstanceState.getParcelable("recipe");
        mStepListIndex = savedInstanceState.getInt("listIndex");
    }

    /**
     * Notifies the Step fragment of the click event and provides data in two pane mode
     *
     * @param position is the position of the selected Step item
     */
    @Override
    public void handleOnClick(int position) {
        if (mTwoPane == true) {
            mStepListIndex = position;
            sendDataToStepFragment();
        }
    }
}
