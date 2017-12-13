package edu.illinois.finalproject.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import edu.illinois.finalproject.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * This is the main activity and users will see when using the app. It consists of three main
 * Fragments that are controlled by a ViewPager. The three fragments are the map, camera, and
 * profile fragment. The MainActivity is also in charge of asking the user for permissions.
 */
public class MainActivity extends AppCompatActivity {
    // app defined constant to determine the permission that was requested
    public static final int PERMISSIONS_ALL = 1;

    // the Firebase node where all the userIds are stored
    public static final String USER_ID_REF = "user_ids";

    // each of these will be true if the user accepts the permission
    public static boolean fineLocationPermission;
    public static boolean coarseLocationPermission;

    // the ViewPager pages of each page
    public static final int MAP_PAGE = 0;
    public static final int CAMERA_PAGE = 1;
    public static final int PROFILE_PAGE = 2;

    private ViewPager mViewPager;
    private Button mapButton;
    private Button profileButton;

    private FirebaseAuth mAuth;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // configure buttons
        mapButton = (Button) findViewById(R.id.map_menu_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(MAP_PAGE, true);
            }
        });

        profileButton = (Button) findViewById(R.id.profile_menu_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(PROFILE_PAGE, true);
            }
        });

        // configure viewpager
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(new PageSwipeAdapter(getSupportFragmentManager()));
        mViewPager.setPageTransformer(false, new MainPageTransformer());
        mViewPager.setCurrentItem(1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
                float absolutePos = pos + posOffset;
                float posRelativeToCamera = Math.abs(absolutePos - CAMERA_PAGE);
                adjustButtonMargins(posRelativeToCamera);
            }

            @Override
            public void onPageSelected(int position) {
                // need to implement but no functionality needed
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // need to implement but no functionality needed
            }
        });

        askForPermissions();

        // firebase operations
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        addUserToFirebase(user);
    }

    private void addUserToFirebase(final FirebaseUser user) {
        DatabaseReference userIdRef = FirebaseDatabase.getInstance()
                .getReference(USER_ID_REF)
                .child(user.getUid());
        userIdRef.setValue(user.getEmail());
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

        if (!hasPermissions(permissionsToAsk)) {
            ActivityCompat.requestPermissions(this, permissionsToAsk, PERMISSIONS_ALL);
        }

        fineLocationPermission = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
        coarseLocationPermission = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Checks to see if application has a list of permissions. If not, the app will request them.
     * Source: https://stackoverflow.com/questions/34342816/android-6-0-multiple-permissions
     *
     * @param permissions the list of permissions to check
     * @return false if one permissions is not granted. true if all permissions are granted
     */
    public boolean hasPermissions(String... permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method enables the library used to assign fonts to text on runtime
     * Source: https://github.com/chrisjenx/Calligraphy
     *
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * move the menu buttons off the screen
     *
     * @param posRelativeToCamera
     */
    public void adjustButtonMargins(float posRelativeToCamera) {
        float displacementMult = 0.5f;
        float initialDisplacement = 0.5f;
        posRelativeToCamera = posRelativeToCamera * displacementMult + initialDisplacement;

        int margin = (int) getResources().getDimension(R.dimen.menu_button_margin);
        int percentMargin = (int) (margin * posRelativeToCamera);

        RelativeLayout.LayoutParams mapButtonLayout =
                (RelativeLayout.LayoutParams) mapButton.getLayoutParams();
        mapButtonLayout.setMarginStart(percentMargin);
        mapButton.setLayoutParams(mapButtonLayout);

        RelativeLayout.LayoutParams profileButtonLayout =
                (RelativeLayout.LayoutParams) profileButton.getLayoutParams();
        profileButtonLayout.setMarginEnd(percentMargin);
        profileButton.setLayoutParams(profileButtonLayout);
    }
}
