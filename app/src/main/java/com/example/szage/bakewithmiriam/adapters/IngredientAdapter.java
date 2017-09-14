package com.example.szage.bakewithmiriam.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.szage.bakewithmiriam.R;
import com.example.szage.bakewithmiriam.models.Ingredient;

import java.util.ArrayList;

/**
 * Ingredient Adapter provides the layout for each list item in Ingredient Array List.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {

    private ArrayList<Ingredient> mIngredientList = new ArrayList<>();

    // Constructor
    public IngredientAdapter(ArrayList<Ingredient> ingredients) {
        mIngredientList = ingredients;
    }

    /**
     * Returns the holder
     */
    @Override
    public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflates the layout
        View itemView = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_list_item, parent, false);

        // Return a new holder instance
        return new IngredientHolder(itemView);
    }

    /**
     * Involves populating data into the item through holder
     *
     * @param holder that has the views
     * @param position of the desired value in the list
     */
    @Override
    public void onBindViewHolder(IngredientHolder holder, int position) {

        holder.ingredientName.setText(mIngredientList.get(position).getIngredientName());
        holder.measure.setText(mIngredientList.get(position).getMeasure());
        holder.quantity.setText(mIngredientList.get(position).getQuantity());
    }

    /**
     * Returns the total amount of items in the list.
     */
    @Override
    public int getItemCount() {
        if (mIngredientList != null) {
            return mIngredientList.size();
        } else return 0;
    }

    /**
     * Provide a direct reference to each of the views within a data item.
     * Used to cache the views within the item layout for fast access.
     */
    public class IngredientHolder extends RecyclerView.ViewHolder {
        public TextView ingredientName;
        public TextView measure;
        public TextView quantity;

        // Constructor, finds each subviews
        public IngredientHolder(View itemView) {
            super(itemView);
            ingredientName = (TextView) itemView.findViewById(R.id.recipe_ingredient);
            measure = (TextView) itemView.findViewById(R.id.ingredient_measure);
            quantity = (TextView) itemView.findViewById(R.id.ingredient_quantity);
        }
    }
}
