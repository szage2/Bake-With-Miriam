package com.example.szage.bakewithmiriam.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.szage.bakewithmiriam.R;
import com.example.szage.bakewithmiriam.fragments.StepFragment;
import com.example.szage.bakewithmiriam.models.Step;

import java.util.ArrayList;

/**
 * Detail Activity passes the Step Object to it's fragment.
 */

public class StepActivity extends AppCompatActivity implements StepFragment.OnClickNavigation{

    private Step mStep;
    private ArrayList<Step> mStepList = new ArrayList<>();
    private int mStepListIndex;
    private StepFragment mStepFragment;
    private final boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        // Get the intent that launched this activity with it's extras
        Intent intentThatStartedStepActivity = getIntent();
        Bundle extras = intentThatStartedStepActivity.getExtras();

        // If there's extra data provide it to activity's fragment
        if (extras != null) {
            mStep = (Step) extras.get("step");
            String recipeName = (String) extras.get("recipeName");
            mStepList = (ArrayList<Step>) extras.get("steps");
            // Get the index of the selected step
            mStepListIndex = extras.getInt("stepListIndex");
            // set the recipe name as title of the support action bar
            this.getSupportActionBar().setTitle(recipeName);

            // call sendDataToStepFragment method.
            sendDataToStepFragment();
        }
    }

    // Activity passes data to it's Fragment when it gets created
    public void sendDataToStepFragment() {
        Bundle stepBundle = new Bundle();
        // Notify fragment it's not two pane mode
        stepBundle.putBoolean("twoPane", mTwoPane);
        // Send the list of Step Objects
        stepBundle.putParcelableArrayList("steps", mStepList);
        // Send the position of the Object
        stepBundle.putInt("stepListIndex", mStepListIndex);
        // New instance of Step Fragment
        mStepFragment = new StepFragment();
        // Set the bundle with desired data as arguments of the fragment
        mStepFragment.setArguments(stepBundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_fragment, mStepFragment).commit();
    }

    /**
     * Save the state of code when device gets rotated.
     *
     * @param outState holds the data we want to save.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("step", mStep);
    }

    /**
     * Restore the state of code after device rotation.
     *
     * @param savedInstanceState has saved (state of) data.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mStep = savedInstanceState.getParcelable("step");
    }

    @Override
    public void handleNavigation(int listIndex) {
        mStepListIndex = listIndex;
        Log.i("TAG", "ListIndex in Step Activity is " + mStepListIndex);
        sendDataToStepFragment();
    }
}
