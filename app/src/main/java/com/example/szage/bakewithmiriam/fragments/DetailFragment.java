package com.example.szage.bakewithmiriam.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.szage.bakewithmiriam.R;
import com.example.szage.bakewithmiriam.adapters.IngredientAdapter;
import com.example.szage.bakewithmiriam.adapters.StepAdapter;
import com.example.szage.bakewithmiriam.models.Ingredient;
import com.example.szage.bakewithmiriam.models.Recipe;
import com.example.szage.bakewithmiriam.models.Step;

import java.util.ArrayList;

/**
 * Detail Fragment displays a list of Ingredients and another list of preparation Steps.
 */

public class DetailFragment extends Fragment {

    private static final String TAG = DetailFragment.class.getSimpleName();
    private RecyclerView mIngredientRecyclerView;
    private RecyclerView mStepRecyclerView;
    private LinearLayoutManager mIngredientLayoutManager;
    private LinearLayoutManager mStepLayoutManager;
    private Recipe mRecipe;
    private boolean mTwoPane;

    // Define a new interface OnStepClickListener that triggers a callback in the host activity
    OnStepClickListener mListener;

    // OnStepClickListener interface, calls a method in the host activity named handleOnClick
    public interface OnStepClickListener {
        void handleOnClick(int position);
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(OnStepClickListener listener) {
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.mListener = listener;
        return detailFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create Empty array list for recipes and steps
        ArrayList<Ingredient> mIngredientList;
        ArrayList<Step> mStepList;

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // Check if the argument has any data sent by the Activity
        if (getArguments() != null) {
            // If it has, get it into the Recipe object
            mRecipe = getArguments().getParcelable("recipe");
            mTwoPane = getArguments().getBoolean("twoPane");
        } else Log.i(TAG, "recipe details are not exist");

        // Get the list of ingredients and list of steps
        mIngredientList = mRecipe.getIngredientList();
        mStepList = mRecipe.getStepList();

        // Get the particular recipe's name
        String recipeName = mRecipe.getRecipeName();

        // Attach the views to the Recycler View Objects
        mIngredientRecyclerView = (RecyclerView) rootView.findViewById(R.id.ingredient_recycler_view);
        mStepRecyclerView = (RecyclerView) rootView.findViewById(R.id.step_recycler_view);

        // Create an instance of Ingredient Layout Manager and set it on Recycler View
        mIngredientLayoutManager = new LinearLayoutManager(getActivity());
        mIngredientRecyclerView.setLayoutManager(mIngredientLayoutManager);

        // Create an instance of Step Layout Manager and set it on Recycler View
        mStepLayoutManager = new LinearLayoutManager(getActivity());
        mStepRecyclerView.setLayoutManager(mStepLayoutManager);

        // Call customizing method
        customizeRecyclerViews();

        // Instantiate Ingredient Adapter and set it on Recycler View
        IngredientAdapter ingredientAdapter;
        ingredientAdapter = new IngredientAdapter(mIngredientList);
        mIngredientRecyclerView.setAdapter(ingredientAdapter);

        // Instantiate Step Adapter and set it on Recycler View
        StepAdapter stepAdapter;
        stepAdapter = new StepAdapter(mStepList, recipeName, mListener, mTwoPane);
        mStepRecyclerView.setAdapter(stepAdapter);

        // return the rootView
        return rootView;
    }

    private void customizeRecyclerViews() {
        // Set Recycler Views to have fix size
        mIngredientRecyclerView.setHasFixedSize(true);
        mStepRecyclerView.setHasFixedSize(true);

        // Create dividers between items in the Ingredient Recycler View
        DividerItemDecoration ingredientDividerItemDecoration =
                new DividerItemDecoration(mIngredientRecyclerView.getContext(),
                        mIngredientLayoutManager.getOrientation());
        // Add these dividers to the view
        mIngredientRecyclerView.addItemDecoration(ingredientDividerItemDecoration);

        // Create dividers between items in the Step Recycler View
        DividerItemDecoration stepDividerItemDecoration =
                new DividerItemDecoration(mStepRecyclerView.getContext(),
                        mStepLayoutManager.getOrientation());
        // Add these dividers to the view
        mStepRecyclerView.addItemDecoration(stepDividerItemDecoration);
    }
}
