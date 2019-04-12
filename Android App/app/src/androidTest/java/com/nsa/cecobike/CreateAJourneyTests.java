package com.nsa.cecobike;


import android.app.Application;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateAJourneyTests extends Thread {
    private List data;
    private LocationManager locationManager;
    private String mocLocationProvider;
    private String LOG_TAG = "faren";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    @Test
    public void createAJourneyTests() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.start_journey_button), withText("Start my journey"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        1),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());
//        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        Location l = new Location("dsdd");

        l.setLatitude(51.11);
        l.setLongitude(-3.11);

        l.getLatitude();
        l.getLongitude();

//        String[] parts = str.split(",");
//        double latitude = Double.valueOf(parts[0]);
//        double longitude = Double.valueOf(parts[1]);
//        double altitude = Double.valueOf(parts[2]);
//        Location location = new Location(mocLocationProvider);
//        location.setLatitude(latitude);
//        location.setLongitude(longitude);
//
//        Log.e(LOG_TAG, location.toString());

        // set the time in the location. If the time on this location
        // matches the time on the one in the previous set call, it will be
        // ignored
//        location.setTime(System.currentTimeMillis());
//        locationManager.setTestProviderLocation(mocLocationProvider, location);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        l.setLatitude(51.4771);
        l.setLongitude(-3.17408);
        MockLocation(l, 51.4771, -3.17408);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        MockLocation(l, 51.4809 , -3.16618);

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.finish_journey_button), withText("Finish my Journey"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("Ok"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        appCompatButton3.perform(scrollTo(), click());

//        ViewInteraction appCompatButton4 = onView(
//                allOf(withId(R.id.close_button), withText("Close"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withClassName(is("android.widget.FrameLayout")),
//                                        1),
//                                0),
//                        isDisplayed()));
//        appCompatButton4.perform(click());
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
    private static void MockLocation(Location location, Double Lat, Double Lon){
        location.setLatitude(Lat);
        location.setLongitude(Lon);
    }
}
