package edu.illinois.finalproject;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    // app defined constant to determine the permission that was requested
    public static final int PERMISSIONS_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(new PageSwipeAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(1);

        askForPermissions();
    }

    /**
     * Asks for the permissions that this apps needs: camera, writing external storage, and
     * accessing location data
     */
    private void askForPermissions() {
        String[] permissionsToAsk = {
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if(!hasPermissions(permissionsToAsk)){
            ActivityCompat.requestPermissions(this, permissionsToAsk, PERMISSIONS_ALL);
        }
    }

    /**
     * Checks to see if application has a list of permissions. If not, the app will request them.
     * Source: https://stackoverflow.com/questions/34342816/android-6-0-multiple-permissions
     * @param permissions the list of permissions to check
     * @return false if one permissions is not granted. true if all permissions are granted
     */
    public boolean hasPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
