package com.example.szage.bakewithmiriam.fragments;

import android.net.Uri;
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
 * Detail Fragment displays a list of Ingredients and another list one of preparation Steps.
 */

public class DetailFragment extends Fragment {

    private static final String TAG = DetailFragment.class.getSimpleName();
    private RecyclerView mIngredientRecyclerView;
    private RecyclerView mStepRecyclerView;
    private IngredientAdapter mIngredientAdapter;
    private StepAdapter mStepAdapter;
    private ArrayList<Ingredient> mIngredientList;
    private ArrayList<Step> mStepList;
    private LinearLayoutManager ingredientLayoutManager;
    private LinearLayoutManager stepLayoutManager;
    private Recipe mRecipe;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create Empty array list for recipes and steps
        mIngredientList = new ArrayList<>();
        mStepList = new ArrayList<>();

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // Check if the argument has any data sent by the Activity
        if (getArguments() != null) {
            // If it has, get it into the Recipe object
            mRecipe = getArguments().getParcelable("recipe");
        } else Log.i(TAG, "recipe details are not exist");

        // Get the list of ingredients and list of steps
        mIngredientList = mRecipe.getIngredientList();
        mStepList = mRecipe.getStepList();

        // Get the particular recipe's name
        String recipeName = mRecipe.getRecipeName();

        // Attach the views to the Recycler View Objects
        mIngredientRecyclerView = (RecyclerView) rootView.findViewById(R.id.ingredient_recycler_view);
        mStepRecyclerView = (RecyclerView) rootView.findViewById(R.id.step_recycler_view);

        // Create a new instance of Linear Layout Manager
        ingredientLayoutManager = new LinearLayoutManager(getActivity());
        mIngredientRecyclerView.setLayoutManager(ingredientLayoutManager);

        stepLayoutManager = new LinearLayoutManager(getActivity());
        mStepRecyclerView.setLayoutManager(stepLayoutManager);

        // Call customizing method
        customizeRecyclerViews();

        // Instantiate Ingredient and Step Adapters
        mIngredientAdapter = new IngredientAdapter(mIngredientList);
        mIngredientRecyclerView.setAdapter(mIngredientAdapter);

        // Set adapters on Recycler Views
        mStepAdapter = new StepAdapter(mStepList, recipeName);
        mStepRecyclerView.setAdapter(mStepAdapter);

        // return the rootView
        return rootView;
    }

    private void customizeRecyclerViews() {
        // Set Recycler Views to have fix size
        mIngredientRecyclerView.setHasFixedSize(true);
        mStepRecyclerView.setHasFixedSize(true);

        // Create dividers between recipe items in the Ingredient Recycler View
        DividerItemDecoration ingredientDividerItemDecoration =
                new DividerItemDecoration(mIngredientRecyclerView.getContext(),
                        ingredientLayoutManager.getOrientation());
        // Add these dividers to the view
        mIngredientRecyclerView.addItemDecoration(ingredientDividerItemDecoration);

        // Create dividers between recipe items in the Step Recycler View
        DividerItemDecoration stepDividerItemDecoration =
                new DividerItemDecoration(mStepRecyclerView.getContext(),
                        stepLayoutManager.getOrientation());
        // Add these dividers to the view
        mStepRecyclerView.addItemDecoration(stepDividerItemDecoration);
    }
}
