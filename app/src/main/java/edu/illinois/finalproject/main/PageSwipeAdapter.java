package edu.illinois.finalproject.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.finalproject.camera.CameraFragment;
import edu.illinois.finalproject.map.MapFragment;
import edu.illinois.finalproject.profile.ProfileFragment;

/**
 * This is the adapter that the ViewPager (in the MainActivity) uses to control which Fragment is
 * displayed to the user. Depending on the position of the adapter, the PageSwipeAdapter will return
 * a certain Fragment.
 */

public class PageSwipeAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> pageFragments;

    public PageSwipeAdapter(FragmentManager fm) {
        super(fm);
        pageFragments = new ArrayList<>();
        pageFragments.add(new MapFragment());
        pageFragments.add(new CameraFragment());
        pageFragments.add(new ProfileFragment());
    }

    /**
     * Returns the page fragment corresponding to the location of the page swiper. The first
     * page will be the map; the second, camera; the third, profile.
     *
     * @param position int position of the location of the page swiper
     * @return the Fragment at the position
     */
    @Override
    public Fragment getItem(int position) {
        return pageFragments.get(position);
    }

    @Override
    public int getCount() {
        return pageFragments.size();
    }
}
