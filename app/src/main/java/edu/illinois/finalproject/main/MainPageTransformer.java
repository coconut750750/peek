package edu.illinois.finalproject.main;

import android.support.v4.view.ViewPager;
import android.view.View;

/***
 * Created by Brandon on 12/5/17.
 *
 * This is the PageTransformer used by the Main ViewPager in the MainActivity.
 * Source: https://medium.com/@BashaChris/the-android-viewpager-has-become-a-fairly-popular
 * -component-among-android-apps-its-simple-6bca403b16d4
 */

public class MainPageTransformer implements ViewPager.PageTransformer {

    private static final float PAST_PAGE = 1.0f; // position if the current view is fully hidden
    private static final float ON_PAGE = 0.0f; // position if the user is completely on current view

    private static final float INVISIBLE = 0.0f; // the alpha value for transparent
    private static final float VISIBLE = 1.0f; // the alpha value for opaque

    /**
     * This is the primary method that will be invoked each time the ViewPager's position changes.
     * All it does is adjusts the Alpha Values of the views depending on the location of the
     * ViewPager. This gives the effect of a view being faded out.
     *
     * @param view     the view that will be adjusted
     * @param position the position of the ViewPager
     */
    @Override
    public void transformPage(View view, float position) {
        if (Math.abs(position) >= PAST_PAGE) {
            view.setAlpha(INVISIBLE);
        } else if (position == ON_PAGE) {
            view.setAlpha(VISIBLE);
        } else {
            view.setAlpha(VISIBLE - Math.abs(position));
        }
    }
}
