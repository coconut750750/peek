package edu.illinois.finalproject.main;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/***
 * Created by Brandon on 12/5/17.
 *
 * Source: https://medium.com/@BashaChris/the-android-viewpager-has-become-a-fairly-popular-component-among-android-apps-its-simple-6bca403b16d4
 */

public class MainPageTransformer implements ViewPager.PageTransformer {

    float prevPosition = 0;

    @Override
    public void transformPage(View view, float position) {
        view.setTranslationX(view.getWidth() * -position);

        if(position <= -1.0F || position >= 1.0F) {
            view.setAlpha(0.0F);
        } else if( position == 0.0F ) {
            view.setAlpha(1.0F);
        } else {
            view.setAlpha(1.0F - Math.abs(position));
        }
    }

    public boolean scrollingRight(float position) {
        return position > prevPosition;
    }
}
