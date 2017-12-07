package edu.illinois.finalproject.main;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/***
 * Created by Brandon on 12/5/17.
 *
 * Source: https://medium.com/@BashaChris/the-android-viewpager-has-become-a-fairly-popular
 * -component-among-android-apps-its-simple-6bca403b16d4
 */

public class MainPageTransformer implements ViewPager.PageTransformer {

    public static final float PAST_PAGE = 1.0f;
    public static final float ON_PAGE = 0.0f;

    public static final float INVISIBLE = 0.0f;
    public static final float VISIBLE = 1.0f;

    @Override
    public void transformPage(View view, float position) {
        //view.setTranslationX(view.getWidth() * -position);

        if(Math.abs(position) >= PAST_PAGE) {
            view.setAlpha(INVISIBLE);
        } else if( position == ON_PAGE ) {
            view.setAlpha(VISIBLE);
        } else {
            view.setAlpha(VISIBLE - Math.abs(position));
        }
    }
}
