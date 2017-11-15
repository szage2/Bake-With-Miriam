package com.example.szage.bakewithmiriam;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.szage.bakewithmiriam.activities.RecipeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

/**
 * Created by szage on 2017. 11. 13..
 */

@RunWith(AndroidJUnit4.class)
public class RecipeRecyclerViewBasicTest {

    @Rule
    public ActivityTestRule<RecipeActivity> activityTestRule =
            new ActivityTestRule<>(RecipeActivity.class);


    public class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }

    @Test
    public void recipeActivityTest() {
        onView(withId(R.id.recipe_recycler_view))
                // Scroll to position 0 and Click on item
                .perform(RecyclerViewActions.scrollToPosition(0))
                .perform(click())
                // Check that it's text "Nutella Pie"
                .check(matches(hasDescendant(withText("Nutella Pie"))));

        // Check the number of listed items in the recycler view
        onView(withId(R.id.recipe_recycler_view)).check(new RecyclerViewItemCountAssertion(4));
    }

    @Test
    public void launchDetailActivityTest() {
        onView(ViewMatchers.withId(R.id.recipe_recycler_view))
                // Launching the activity by finding and checking on the 1st item
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));

        //Check that the recycler view for ingredients is displayed in the activity
        onView(withId(R.id.ingredient_recycler_view)).check(matches(isDisplayed()));

        //Check that the recycler view for steps has the text "Recipe Introduction"
        onView(withId(R.id.step_recycler_view)).check(matches(hasDescendant(withText("Recipe Introduction"))));
    }
}
