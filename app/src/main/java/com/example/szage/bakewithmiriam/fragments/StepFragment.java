package com.example.szage.bakewithmiriam.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.szage.bakewithmiriam.R;
import com.example.szage.bakewithmiriam.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;


/**
 * Step Fragment displays selected Step.
 */
public class StepFragment extends Fragment {

    private final String TAG = StepFragment.class.getSimpleName();

    private int mStepListIndex;
    private ArrayList<Step> mStepList = new ArrayList<>();

    private Uri mVideoUri;
    private TextView mStepDescription;
    private TextView mLongStepDescription;

    private boolean mIsThereSavedState;

    private ExoPlayer mExoPlayer;
    private long mPlayerPosition;
    private SimpleExoPlayerView mSimpleExoPlayerView;

    private Button mPreviousButton;
    private Button mNextButton;

    private View decorView;

    // Define a new interface OnClickNavigation that triggers a callback in the host activity
    OnClickNavigation mNavigationListener;

    // OnClickNavigation interface, calls a method in the host activity named handleNavigation
    public interface OnClickNavigation {
        void handleNavigation(int listIndex);
    }

    public StepFragment() {
        // Required empty public constructor
    }

    public static StepFragment newInstance(OnClickNavigation navigationListener) {
        StepFragment stepFragment = new StepFragment();
        stepFragment.mNavigationListener = navigationListener;
        return stepFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mNavigationListener = (OnClickNavigation) context;
        } catch (ClassCastException ex) {
            Log.i(TAG, "There is no navigation callback in two pane mode");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        // Find the view for ExoPlayer
        mSimpleExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);

        // Find the view for previous button, attach it
        mPreviousButton = (Button) rootView.findViewById(R.id.previous_button);

        // Find the view for next button attach it
        mNextButton = (Button) rootView.findViewById(R.id.next_button);

        // If there's a save state of code, store true in a flag
        if (savedInstanceState != null) {
            mIsThereSavedState = true;
        } else mIsThereSavedState = false;

        // Check if the argument has any data sent by the Activity/ StepAdapter
        if (getArguments() != null) {

            // create Step object
            Step step;
            // variable to identify two/single pane mode
            boolean twoPane;

            // Get the flag to identify two/single pane mode
            twoPane = getArguments().getBoolean("twoPane");
            // Get the step list
            mStepList = getArguments().getParcelableArrayList("steps");
            // Get the step's list index
            mStepListIndex = getArguments().getInt("stepListIndex");

            // If a step is selected, get the step from the list with it's index
            if (mStepListIndex != 0) {
                step = mStepList.get(mStepListIndex);
                // If step is not selected yet, set it for the very first step
            } else step = mStepList.get(0);

            // Check whether it's two pane mode
            if (twoPane == true) {
                // In two pane mode
                // Make the previous and next buttons disappear in two pane mode
                mPreviousButton.setVisibility(View.GONE);
                mNextButton.setVisibility(View.GONE);

            } else {
                // In single pane mode
                // Call method makeButtonsClickable
                makeButtonsClickable();
            }

            // Get descriptions, and the video URL for Step
            if (step != null) {
                String shortDescription = step.getShortDescription();
                String longDescription = step.getLongDescription();
                String videoThumbnailURL = step.getThumbnailURL();
                String videoURL = step.getVideoUrl();

                // if there is a video (videoURL or videoThumbnailURL),
                // parse it and assign it to mVideoUri variable
                if (videoURL != null && !videoURL.isEmpty()) {
                    mVideoUri = Uri.parse(videoURL);
                    // initialize the ExoPlayer
                    // But only if there's no saved state of code
                    if (savedInstanceState == null) {
                        initializeExoPlayer();
                    }
                } else if (videoThumbnailURL != null && !videoThumbnailURL.isEmpty()) {
                    mVideoUri = Uri.parse(videoThumbnailURL);
                    // initialize the ExoPlayer
                    // But only if there's no saved state of code
                    if (savedInstanceState == null) {
                        initializeExoPlayer();
                    }
                } else {
                    // If there's no video to the recipe, Log it
                    Log.i(TAG, "There is no video source available");
                    // Replace player's view
                    mSimpleExoPlayerView.setVisibility(View.GONE);
                }

                // Get the text views and set the texts on particular views
                mStepDescription = (TextView) rootView.findViewById(R.id.step_description);
                mStepDescription.setText(shortDescription);
                mLongStepDescription = (TextView) rootView.findViewById(R.id.long_description);
                mLongStepDescription.setText(longDescription);
            }
        } else Log.i(TAG, "Step details are not exist");

        // Inflate the layout for this fragment
        return rootView;
    }

    private void initializeExoPlayer() {

        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mSimpleExoPlayerView.setPlayer((SimpleExoPlayer) mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            String userAgent = Util.getUserAgent(getContext(), "BakeWithMiriam");

            // Prepare the MediaSource.
            MediaSource mediaSource = new ExtractorMediaSource(mVideoUri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

            // In case the device has been rotated
            if (mIsThereSavedState == true) {
                // continue playing the video from previous position
                mExoPlayer.seekTo(mPlayerPosition);
            } else
                // Otherwise play it normally
            mExoPlayer.seekTo(mExoPlayer.getBufferedPosition());
        }
    }

    /**
     *  Release the player
     */
    private void releaseExoPlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // If a video is attached to the recipe, release the player
        if (mVideoUri != null) {
            releaseExoPlayer();
        }
    }

    /**
     * Make navigation buttons clickable and hide them if they are not needed
     */
    private void makeButtonsClickable() {

        // Set a click listener on previous button
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make sure to avoid Index out of range error
                if (mStepListIndex != 0) {
                    // decrease the list index by 1
                    mStepListIndex--;
                    // set the callback
                    mNavigationListener.handleNavigation(mStepListIndex);
                }
            }
        });

        // Set a click listener on next button
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make sure to avoid Index out of range error
                if (mStepListIndex <= mStepList.size() -1) {
                    // increase the list index by 1
                    mStepListIndex++;
                    mNavigationListener.handleNavigation(mStepListIndex);
                }
            }
        });

        // If the list index is 0, make the previous button disappear,
        // because there's no previous step
        if (mStepListIndex == 0) {
            mPreviousButton.setVisibility(View.GONE);
            // If the list index is equals or exceeds the size of the list,
            // make the previous button disappear as there's no next step
        } else if (mStepListIndex >= mStepList.size() -1) {
            mNextButton.setVisibility(View.GONE);
        }
    }

    /**
     * If orientation in landscape mode, make video full screen and the rest are invisible
     */
    public void orientationChangedToLandscape() {
        // Make texts invisible
        mStepDescription.setVisibility(View.GONE);
        mLongStepDescription.setVisibility(View.GONE);
        // Make navigation buttons invisible
        mPreviousButton.setVisibility(View.GONE);
        mNextButton.setVisibility(View.GONE);
        // Resize the player to fill the screen
        mSimpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
    }

    /**
     * If orientation in portrait mode, make views visible
     */
    public void orientationChangedToPortrait() {
        // Make texts visible
        mStepDescription.setVisibility(View.VISIBLE);
        mLongStepDescription.setVisibility(View.VISIBLE);
        // Make navigation buttons visible
        mPreviousButton.setVisibility(View.VISIBLE);
        mNextButton.setVisibility(View.VISIBLE);
        // Resize the player to fit to layout
        mSimpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // get the view in order to override it's features
        decorView = getView().getRootView();

        // Get the layout of the fragment
        RelativeLayout relativeLayout = (RelativeLayout) getView().findViewById(R.id.relative_layout);

        // Create layout parameters
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        // Variable to set visibility of status bar
        int uiOptions = 0;

        // If the device is in portrait mode
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            // Make the status bar visible
            uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;

            // An show the action bar as well
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();

            // Call orientationChangedToPortrait method
            orientationChangedToPortrait();

            // Set the margins on the layout parameters
            layoutParams.setMargins(12,12,12,12);

        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // If the device is in landscape mode
            // Make the status bar invisible
            uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

            // And hide the action bar as well
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

            // Call orientationChangedToLandscape method
            orientationChangedToLandscape();

            // Make margins disappear by setting them 0 in the layout
            layoutParams.setMargins(0,0,0,0);
        }
        // Set the status bar's visibility
        decorView.setSystemUiVisibility(uiOptions);

        // Set the layout parameters(margins) on the layout
        relativeLayout.setLayoutParams(layoutParams);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Make the status bar visible
        decorView = getView().getRootView();
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
        // And show the action bar as well
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    /**
     * Save the state of the current code
     * @param outState holds the saved state
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Get the current position of the exoPlayer
        mPlayerPosition = mExoPlayer.getCurrentPosition();
        outState.putLong("playerPosition", mPlayerPosition);
    }

    /**
     * Restore the state before device rotation
     * @param savedInstanceState is the version of code before device rotation
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore the position of the exoPlayer
            mPlayerPosition = savedInstanceState.getLong("playerPosition");
            Log.i(TAG, "player's position in restore is " + mPlayerPosition);
        }
        // Initialize the player
        initializeExoPlayer();
    }
}
