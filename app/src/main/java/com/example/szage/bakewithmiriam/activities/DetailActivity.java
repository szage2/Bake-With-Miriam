package com.example.szage.bakewithmiriam.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.szage.bakewithmiriam.fragments.DetailFragment;
import com.example.szage.bakewithmiriam.R;
import com.example.szage.bakewithmiriam.models.Recipe;

/**
 * Detail Activity passes the Recipe Object to it's fragment.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private DetailFragment mDetailFragment;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState != null) {
            Log.i(TAG, "saved instance state: " + savedInstanceState.getParcelableArrayList("recipe"));
        }

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
        }
    }

    // Activity passes the Recipe object to it's Fragment when it gets created
    public void sendDataToDetailFragment() {
        Bundle detailBundle = new Bundle();
        detailBundle.putParcelable("recipe", mRecipe);
        // New instance of Recipe Fragment
        mDetailFragment =  new DetailFragment();
        // Set the bundle with desired data as arguments of the fragment
        mDetailFragment.setArguments(detailBundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_fragment, mDetailFragment).commit();
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
    }
}
