package com.example.szage.bakewithmiriam.widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.szage.bakewithmiriam.R;
import com.example.szage.bakewithmiriam.models.Ingredient;
import com.example.szage.bakewithmiriam.models.Recipe;

import java.util.ArrayList;

/**
 * Provider for recipe list widget that handles app widget broadcasts
 */

public class RecipeWidgetProvider extends AppWidgetProvider {

    Recipe mCurrentRecipe;
    ArrayList<Ingredient> mIngredientList = new ArrayList<>();
    public static final String ACTION_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";
    RemoteViews mRemoteViews;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.i("RecipeWidgetProvider", "ingredient list is " + mIngredientList.size());

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {
            // Create intent that starts the RecipeWidgetService
            Intent intent = new Intent(context, RecipeWidgetService.class);
            intent.putExtra("ingredients", mIngredientList);
            // When intents are compared, the extras are ignored, so we need to embed the extras
            // into the data so that the extras will not be ignored
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            // Instantiate the RemoteViews object for widget layout
            mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_list);

            // Set up the collection by the adapter based on the android version
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, mRemoteViews);
            } else {
                setRemoteAdapterV11(context, mRemoteViews);
            }

            // Set current recipe name on the widget label text view
            if (mCurrentRecipe != null) {
                mRemoteViews.setTextViewText(R.id.widget_label, mCurrentRecipe.getRecipeName());
            } else {
                mRemoteViews.setEmptyView(R.id.recipe_widget_list, R.id.widget_list_empty);
            }

            // inform app widget manager to perform an update on current app widget
            appWidgetManager.updateAppWidget(appWidgetId, mRemoteViews);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.recipe_widget_list);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // Get an instance of  App Widget Manager
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        // Get the App's Widget component
        ComponentName myWidget = new ComponentName(context.getApplicationContext(), RecipeWidgetProvider.class);
        // Get the  App Widget Ids
        int appWidgetIds [] = appWidgetManager.getAppWidgetIds(myWidget);

        // Get the action
        String action = intent.getAction();
        // if the action is ACTION_APPWIDGET_UPDATE
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            // Get the extras of the intent
            Bundle bundle = intent.getExtras();
            // In case that extras are not null
            if (bundle != null) {
                // Get the Recipe object from the intent
                mCurrentRecipe = (Recipe) intent.getParcelableExtra("recipe");
                // If the Recipe object has been selected (is not null)
                if (mCurrentRecipe != null) {
                    // Get the list of Ingredients
                    mIngredientList = mCurrentRecipe.getIngredientList();
                }
                // Check that the appWidgetIds is valid
                if (appWidgetIds != null && appWidgetIds.length > 0) {
                    // Make sure to update the widget every time a recipe gets selected
                    this.onUpdate(context, appWidgetManager, appWidgetIds);
                }
            }
        }
    }

    //
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(final Context context, @NonNull final RemoteViews views) {
        Log.i("setRemoteAdapter", " is working");
        views.setRemoteAdapter(R.id.recipe_widget_list,
                new Intent(context, RecipeWidgetService.class));
    }

    @SuppressWarnings("deprecation")
    private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        Log.i("setRemoteAdapterV11", " is working");
        views.setRemoteAdapter(0, R.id.recipe_widget_list,
                new Intent(context, RecipeWidgetService.class));
    }

}
