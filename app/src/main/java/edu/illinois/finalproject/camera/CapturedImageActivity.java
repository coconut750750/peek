package edu.illinois.finalproject.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.util.ArrayList;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.map.MapManager;

/**
 * This activity shows the user details about the photo they just took. It will display a map
 * with a marker at the location where the photo was taken and the actual photo itself.
 */
public class CapturedImageActivity extends AppCompatActivity {

    public static final String CAPTURED_PHOTO_NAME = "photoName";
    public static final int DEFAULT_ZOOM = 15;

    private Bitmap capturedBitmap;
    ImageView capturedImageView;

    private MapManager mapManager;

    /**
     * Creates the activity and sets the views to private instance variables. It receives and intent
     * to retrieve the location of the picture that was taken. Then, it sets the image view to
     * that picture's bitmap while deleting the photo itself from the phone.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captured_image);

        setTitle("Confirm Location");

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);

        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        capturedImageView = (ImageView) findViewById(R.id.captured_image);

        Intent passedIntent = getIntent();
        String photoName = passedIntent.getStringExtra(CAPTURED_PHOTO_NAME);
        String photoPath = getFileStreamPath(photoName).getPath();

        // read bitmap data from File object
        // source: https://stackoverflow.com/questions/8710515/reading-an-image-file-into-
        // bitmap-from-sdcard-why-am-i-getting-a-nullpointerexc
        // need to rotate image 90 degrees because camera saves image in landscape mode by default
        capturedBitmap = rotateImage(BitmapFactory.decodeFile(photoPath));

        // finds a cropped section of the picture to show the user
        // source: https://stackoverflow.com/questions/6908604/android-crop-center-of-bitmap
        ViewTreeObserver vto = capturedImageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                capturedImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = capturedImageView.getMeasuredWidth();
                int height = capturedImageView.getMeasuredHeight();

                Bitmap croppedBitmap = Bitmap.createBitmap(
                        capturedBitmap,
                        0,
                        (capturedBitmap.getHeight() - height) / 2,
                        width,
                        height
                );

                capturedImageView.setImageBitmap(croppedBitmap);

            }
        });

        // delete the file from SD card
        File photoFile = new File(photoPath);
        photoFile.delete();

        MapView mapView = (MapView) findViewById(R.id.map);
        mapManager = new MapManager(this, mapView, new ArrayList<LatLng>(), true);
        mapManager.startMap(savedInstanceState);

        // https://developer.android.com/training/location/retrieve-current.html
        FusedLocationProviderClient mFusedLocationClient;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng photoCoord = new LatLng(location.getLatitude(), location.getLongitude());
                            }
                        }
                    });
        }
    }

    /**
     * Rotates a bitmap 90 degrees since camera saves image in landscape mode. Source:
     * https://stackoverflow.com/questions/9015372/how-to-rotate-a-bitmap-90-degrees
     *
     * @param source the original bitmap from the file
     * @return a bitmap that is rotated to portrait mode
     */
    public static Bitmap rotateImage(Bitmap source) {
        final float ANGLE_OFFSET = 90;
        Matrix matrix = new Matrix();
        matrix.postRotate(ANGLE_OFFSET);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}