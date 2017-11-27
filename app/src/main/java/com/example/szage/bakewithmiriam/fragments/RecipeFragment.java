package com.example.szage.bakewithmiriam.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create an Empty array list for recipes
        ArrayList<Recipe> recipeArrayList= new ArrayList<>();

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        // Check if the argument has any data sent by the Activity
        if (getArguments() != null) {
            // If it has, get it into the recipe list
            recipeArrayList = getArguments().getParcelableArrayList("RecipeList");
            // Otherwise Log the issue.
        } else Log.e(TAG, String.valueOf(R.string.no_recipes));

        // Create RecyclerView
        RecyclerView recipeRecyclerView;
        // Attach the view to the Recycler View Object
        recipeRecyclerView = (RecyclerView) rootView.findViewById(R.id.recipe_recycler_view);

        // Create and Instantiate the Layout Managers
        LinearLayoutManager recipeLayoutManager;
        recipeLayoutManager = new LinearLayoutManager(getActivity());
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        // Get the boolean variable from the resources
        boolean isTablet = getResources().getBoolean(R.bool.has_two_panes);

        if (isTablet) {
            // If the device that runs the app is a tablet
            // Set the Layout Manager on the Recycler View
            recipeRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            // If the device that runs the app is a phone
            // Set the Grid Manager on the Recycler View
            recipeRecyclerView.setLayoutManager(recipeLayoutManager);
        }

        // Create Recipe Adapter
        RecipeAdapter recipeAdapter;
        // Instantiate Recipe Adapter
        recipeAdapter = new RecipeAdapter(recipeArrayList);
        // Set the adapter on Recycler View
        recipeRecyclerView.setAdapter(recipeAdapter);

        // return the rootView
        return rootView;
    }
}
