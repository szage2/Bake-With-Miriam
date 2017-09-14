package com.example.szage.bakewithmiriam.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.szage.bakewithmiriam.activities.DetailActivity;
import com.example.szage.bakewithmiriam.R;
import com.example.szage.bakewithmiriam.models.Recipe;

import java.util.ArrayList;

/**
 * Recipe Adapter provides the layout and clickable view for each list item in Recipe Array List.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {


    private static final String TAG = RecipeAdapter.class.getSimpleName();
    private ArrayList<Recipe> mRecipeArrayList;

    // Constructor
    public RecipeAdapter(ArrayList<Recipe> recipeList) {
        mRecipeArrayList = recipeList;
    }

    /**
     * Provide a direct reference to each of the views within a data item.
     * Used to cache the views within the item layout for fast access.
     */
    public static class RecipeHolder extends RecyclerView.ViewHolder {
        public TextView recipeText;
        public TextView servingsText;
        public ImageView recipeImage;

        // Constructor, finds each subviews
        public RecipeHolder(View itemView) {
            super(itemView);
            recipeText = (TextView) itemView.findViewById(R.id.recipe_name);
            servingsText = (TextView) itemView.findViewById(R.id.recipe_servings);
            recipeImage = (ImageView) itemView.findViewById(R.id.recipe_image);
        }
    }

    /**
     * Returns the holder
     */
    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflates the layout
        View itemView = (View) LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recipe_list_item, parent, false);

        // Return a new holder instance
        return new RecipeHolder(itemView);
    }

    /**
     * Involves populating data into the item through holder
     *
     * @param holder that has the views
     * @param position of the desired value in the list
     */
    @Override
    public void onBindViewHolder(RecipeHolder holder, final int position) {

        final String servings = "servings: ";

        holder.recipeText.setText(mRecipeArrayList.get(position).getRecipeName().toString());
        holder.servingsText.setText(servings + mRecipeArrayList.get(position).getServings());

        // TODO Display image with Picasso

        // Making recipe text clickable
        holder.recipeText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Recipe recipeData = mRecipeArrayList.get(position);

                // Intent that navigates to Detail Activity
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                // Intent passes data to Detail Activity
                intent.putExtra("recipe", recipeData);

                // Start the Activity with intent
                v.getContext().startActivity(intent);
            }
        });
    }

    /**
     * Returns the total amount of items in the list.
     */
    @Override
    public int getItemCount() {
        if (mRecipeArrayList.size() != 0) {
            Log.i(TAG,"list size is " + mRecipeArrayList.size());
            return mRecipeArrayList.size();
        } else return 0;
    }
}
