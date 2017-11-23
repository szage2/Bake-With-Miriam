package com.example.szage.bakewithmiriam.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.szage.bakewithmiriam.R;
import com.example.szage.bakewithmiriam.adapters.RecipeAdapter;
import com.example.szage.bakewithmiriam.models.Recipe;

import java.util.ArrayList;


/**
 * Recipe Fragment displays a list of recipes.
 */

public class RecipeFragment extends Fragment {

    private static final String TAG = RecipeFragment.class.getSimpleName();

    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private ArrayList<Recipe> mRecipeArrayList;
    private LinearLayoutManager mRecipeLayoutManager;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create an Empty array list for recipes
        mRecipeArrayList = new ArrayList<>();

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        // Check if the argument has any data sent by the Activity
        if (getArguments() != null) {
            // If it has, get it into the recipe list
            mRecipeArrayList = getArguments().getParcelableArrayList("RecipeList");
            // Otherwise Log the issue.
        } else Log.i(TAG, "recipes are not exist");

        // Attach the view to the Recycler View Object
        mRecipeRecyclerView = (RecyclerView) rootView.findViewById(R.id.recipe_recycler_view);

        // Instantiate the Layout Manager
        mRecipeLayoutManager = new LinearLayoutManager(getActivity());

        // Set the Layout Manager on the Recycler View
        mRecipeRecyclerView.setLayoutManager(mRecipeLayoutManager);

        // Instantiate Recipe Adapter
        mRecipeAdapter = new RecipeAdapter(mRecipeArrayList);
        // Set the adapter on Recycler View
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);

        // return the rootView
        return rootView;
    }
}
