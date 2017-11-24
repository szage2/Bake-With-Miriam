package com.example.szage.bakewithmiriam.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.szage.bakewithmiriam.R;
import com.example.szage.bakewithmiriam.activities.StepActivity;
import com.example.szage.bakewithmiriam.fragments.DetailFragment;
import com.example.szage.bakewithmiriam.models.Step;

import java.util.ArrayList;

/**
 * Step Adapter provides the layout and clickable view for each list item in Step Array List.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {

    private ArrayList<Step> mStepList = new ArrayList<>();
    private String mRecipeName;
    private DetailFragment.OnStepClickListener mListener;
    private boolean mTwoPane;

    // Constructor
    public StepAdapter(ArrayList<Step> steps, String recipeName,
                       DetailFragment.OnStepClickListener listener, boolean twoPane) {
        mStepList = steps;
        mRecipeName = recipeName;
        mListener = listener;
        mTwoPane = twoPane;
    }

    /**
     * Returns the holder
     */
    @Override
    public StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflates the layout
        View itemView = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_item, parent, false);

        // Return a new holder instance
        return new StepHolder(itemView);
    }

    /**
     * Involves populating data into the item through holder
     *
     * @param holder that has the views
     * @param position of the desired value in the list
     */
    @Override
    public void onBindViewHolder(StepHolder holder, final int position) {
        holder.stepShortDescription.setText(mStepList.get(position).getShortDescription());

        // Making step description clickable
        holder.stepShortDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the selected Step
                final Step step = mStepList.get(position);

                // Notifies the activity about OnClick
                mListener.handleOnClick(position);

                // In case of one pain mode
                if (!mTwoPane) {
                    // Intent that navigates to Step Activity
                    Intent stepIntent = new Intent(v.getContext(), StepActivity.class);

                    // Intent passes data to Step Activity
                    stepIntent.putExtra("step", step);
                    stepIntent.putExtra("recipeName", mRecipeName);
                    stepIntent.putExtra("steps", mStepList);
                    stepIntent.putExtra("stepListIndex", position);

                    // Start the Activity with intent
                    v.getContext().startActivity(stepIntent);
                }
            }
        });
    }

    /**
     * Returns the total amount of items in the list.
     */
    @Override
    public int getItemCount() {
        if (mStepList != null) {
            return mStepList.size();
        }else return 0;
    }

    /**
     * Provide a direct reference to each of the views within a data item.
     * Used to cache the views within the item layout for fast access.
     */
    public class StepHolder extends RecyclerView.ViewHolder {
        TextView stepShortDescription;

        // Constructor, finds subviews
        public StepHolder(View itemView) {
            super(itemView);
            stepShortDescription = (TextView) itemView.findViewById(R.id.recipe_short_description);
        }
    }
}
