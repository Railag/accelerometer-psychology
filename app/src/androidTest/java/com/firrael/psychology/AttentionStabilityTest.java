package com.firrael.psychology;


import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AttentionStabilityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void attentionStabilityTest() {

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.testsButton), isDisplayed()));
        relativeLayout.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.attentionStabilityButton), withText("Устойчивость внимания"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.startButton), withText("Начать"), isDisplayed()));
        appCompatButton.perform(click());

        Random random = new Random();

        try {
            while (true) {

                boolean left = random.nextBoolean();

                if (left) {
                    ViewInteraction appCompatButton2 = onView(
                            allOf(withId(R.id.leftButton), isDisplayed()));
                    appCompatButton2.perform(click());
                } else {
                    ViewInteraction appCompatButton3 = onView(
                            allOf(withId(R.id.rightButton), isDisplayed()));
                    appCompatButton3.perform(click());
                }

                // Added a sleep statement to match the app's execution delay.
                // The recommended way to handle such scenarios is to use Espresso idling resources:
                // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
                try {
                    Thread.sleep(1050);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                onView(withId(R.id.rightButton)).check(matches(isDisplayed()));
            }
        } catch (NoMatchingViewException ignored) {
            ViewInteraction appCompatImageButton = onView(
                    allOf(withContentDescription("Navigate up"),
                            withParent(withId(R.id.toolbar)),
                            isDisplayed()));
            appCompatImageButton.perform(click());
        }
    }

}
