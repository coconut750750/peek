package edu.illinois.finalproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import edu.illinois.finalproject.camera.CameraFragment;
import edu.illinois.finalproject.map.MapFragment;

/***
 * Created by Brandon on 12/2/17.
 */

public class PageSwipeAdapter extends FragmentStatePagerAdapter {

    public PageSwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Returns the page fragment corresponding to the location of the page swiper. The first
     * page will be the map; the second, camera; the third, profile.
     * @param position int position of the location of the page swiper
     * @return the Fragment at the position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CameraFragment();
            case 1:
                return new MapFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
