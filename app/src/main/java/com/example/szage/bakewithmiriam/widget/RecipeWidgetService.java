package com.example.szage.bakewithmiriam.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.szage.bakewithmiriam.R;
import com.example.szage.bakewithmiriam.models.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 *  RecipeWidgetService provide the views for this collection
 */

public class RecipeWidgetService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.i("WidgetService", "WidgetService is working");
        return new RecipeRemoteViewsFactory(intent, this.getApplicationContext());
    }
}

/**
 *  RecipeRemoteViewsFactory creates and returns the item for the collection
 */
class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    ArrayList<Ingredient> mIngredientList = new ArrayList();

    // Constructor
    public RecipeRemoteViewsFactory(Intent intent, Context context) {
        mContext = context;
        mIngredientList = intent.getParcelableArrayListExtra("ingredients");
    }

    @Override
    public void onCreate() {
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // Instantiate Remote View
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        // Get the Ingredient Object
        Ingredient ingredient = mIngredientList.get(position);
        // Get the string values
        String quantity = ingredient.getQuantity();
        String measure = ingredient.getMeasure();
        String ingredientName = ingredient.getIngredientName();

        Log.i("WidgetService", "recipe ingredient is " + ingredientName);
        // Attach these values to matching views
        remoteViews.setTextViewText(R.id.widget_ingredient_quantity, mIngredientList.get(position).getQuantity());
        remoteViews.setTextViewText(R.id.widget_ingredient_measure, mIngredientList.get(position).getMeasure());
        remoteViews.setTextViewText(R.id.widget_recipe_ingredient, mIngredientList.get(position).getIngredientName());
        // return the remote views
        return remoteViews;
    }

    /**
     * When the widget gets updated get the saved json format of ingredients
     */
    @Override
    public void onDataSetChanged() {
        Log.i("WidgetService", "onDataSetChanged is working");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String json = sharedPreferences.getString("savedJsonFormat", "");
        if (!json.equals("")) {
            Gson gson = new Gson();
            mIngredientList = gson.fromJson(json, new TypeToken<ArrayList<Ingredient>>() {
            }.getType());
        }
    }

    /**
     * @return number of elements in the ingredient list
     */
    @Override
    public int getCount() {
        if (mIngredientList == null) return 0;
        return mIngredientList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public void onDestroy() {}
}
