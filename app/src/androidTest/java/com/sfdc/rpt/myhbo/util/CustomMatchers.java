package com.sfdc.rpt.myhbo.util;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Utility for custom matchers to be used by Espresso
 * Created by ahetawal on 3/12/16.
 */
public class CustomMatchers {

    /**
     * Method used for matching the title for a CollapsibleToolbarLayout
     * @param textMatcher
     * @return
     */
    public static Matcher<Object> withTitle(final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, CollapsingToolbarLayout>(CollapsingToolbarLayout.class) {
            @Override public boolean matchesSafely(CollapsingToolbarLayout toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }
            @Override public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }
        };
    }
}
