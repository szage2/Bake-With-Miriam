package com.example.szage.bakewithmiriam.fragments;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.szage.bakewithmiriam.R;
import com.example.szage.bakewithmiriam.models.Step;

import java.util.ArrayList;


/**
 * Step Fragment displays selected Step.
 */

public class StepFragment extends Fragment {

    private final String TAG = StepFragment.class.getSimpleName();
    private Step mStep;
    private boolean mTwoPane;
    private int mStepListIndex;

    public StepFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        // Check if the argument has any data sent by the Activity
        if (getArguments() != null) {

            mTwoPane = getArguments().getBoolean("twoPane");

            // Check whether it's two pane mode
            if (mTwoPane == true) {

                // If it is, get the steps arrayList and extract the selected step
                ArrayList<Step> steps = getArguments().getParcelableArrayList("steps");

                // Get the list index of the selected step
                mStepListIndex = getArguments().getInt("stepListIndex");

                if (mStepListIndex != 0) {
                    mStep = steps.get(mStepListIndex);
                    // If step is not selected yet, set it for the very first step
                } else mStep = steps.get(0);

            } else {
                // Otherwise, get the Step object from the arguments
                mStep = getArguments().getParcelable("step");
            }

            // Get descriptions, and the video URL for Step
            if (mStep != null) {
                String shortDescription = (String) mStep.getShortDescription();
                String longDescription = (String) mStep.getLongDescription();
                String videoThumbnailURL = (String) mStep.getThumbnailURL();
                String videoURL = (String) mStep.getVideoUrl();

                // Get the view and set the texts on particular views
                TextView stepDescription = (TextView) rootView.findViewById(R.id.step_description);
                stepDescription.setText(shortDescription);
                TextView longStepDescription = (TextView) rootView.findViewById(R.id.long_description);
                longStepDescription.setText(longDescription);
            }
        } else Log.i(TAG, "Step details are not exist");

        // Inflate the layout for this fragment
        return rootView;
    }
}
