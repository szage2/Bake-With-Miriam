package com.example.szage.bakewithmiriam.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.szage.bakewithmiriam.R;
import com.example.szage.bakewithmiriam.models.Step;


/**
 * Step Fragment displays selected Step.
 */

public class StepFragment extends Fragment {

    private final String TAG = StepFragment.class.getSimpleName();

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
            // If it has, get it into the Step object
            Step step = getArguments().getParcelable("step");

            // Get descriptions, and the video URL for Step
            String shortDescription = (String) step.getShortDescription();
            String longDescription = (String) step.getLongDescription();
            String videoThumbnailURL = (String) step.getThumbnailURL();
            String videoURL = (String) step.getVideoUrl();

            // Get the view and set the texts on particular views
            TextView stepDescription = (TextView) rootView.findViewById(R.id.step_description);
            stepDescription.setText(shortDescription);
            TextView longStepDescription = (TextView) rootView.findViewById(R.id.long_description);
            longStepDescription.setText(longDescription);

        } else Log.i(TAG, "Step details are not exist");

        // Inflate the layout for this fragment
        return rootView;
    }
}
