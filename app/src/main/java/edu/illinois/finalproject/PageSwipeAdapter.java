package edu.illinois.finalproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import edu.illinois.finalproject.page.fragments.CameraFragment;

/***
 * Created by Brandon on 12/2/17.
 */

public class PageSwipeAdapter extends FragmentStatePagerAdapter {

    public PageSwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new CameraFragment();
    }

    @Override
    public int getCount() {
        return 1;
    }
}
