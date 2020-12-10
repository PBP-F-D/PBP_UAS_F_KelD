package com.tubespbp.petshop;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddToCartActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void addToCartActivityTest() {
        ViewInteraction cardView = onView(
                allOf(withId(R.id.cardDogs),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.core.widget.NestedScrollView")),
                                        0),
                                1),
                        isDisplayed()));
        cardView.perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.btn_tambah), withText("Add to cart"),
                        childAtPosition(
                                allOf(withId(R.id.rootView),
                                        childAtPosition(
                                                withId(R.id.rv_katalog),
                                                0)),
                                3),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(android.R.id.button1), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.btn_tambah), withText("Add to cart"),
                        childAtPosition(
                                allOf(withId(R.id.rootView),
                                        childAtPosition(
                                                withId(R.id.rv_katalog),
                                                0)),
                                3),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction editText = onView(
                allOf(childAtPosition(
                        allOf(withId(android.R.id.custom),
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        0)),
                        0),
                        isDisplayed()));
        editText.perform(replaceText("0"), closeSoftKeyboard());

        ViewInteraction materialButton5 = onView(
                allOf(withId(android.R.id.button1), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton5.perform(scrollTo(), click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.btn_tambah), withText("Add to cart"),
                        childAtPosition(
                                allOf(withId(R.id.rootView),
                                        childAtPosition(
                                                withId(R.id.rv_katalog),
                                                0)),
                                3),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction editText2 = onView(
                allOf(childAtPosition(
                        allOf(withId(android.R.id.custom),
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        0)),
                        0),
                        isDisplayed()));
        editText2.perform(replaceText("1"), closeSoftKeyboard());

        ViewInteraction materialButton7 = onView(
                allOf(withId(android.R.id.button1), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton7.perform(scrollTo(), click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.btn_tambah), withText("Add to cart"),
                        childAtPosition(
                                allOf(withId(R.id.rootView),
                                        childAtPosition(
                                                withId(R.id.rv_katalog),
                                                0)),
                                3),
                        isDisplayed()));
        materialButton8.perform(click());

        ViewInteraction editText3 = onView(
                allOf(childAtPosition(
                        allOf(withId(android.R.id.custom),
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        0)),
                        0),
                        isDisplayed()));
        editText3.perform(replaceText("2"), closeSoftKeyboard());

        ViewInteraction materialButton9 = onView(
                allOf(withId(android.R.id.button1), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton9.perform(scrollTo(), click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_dashboard), withContentDescription("Cart"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
