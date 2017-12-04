package edu.illinois.finalproject.upload;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.io.File;

import edu.illinois.finalproject.clarifai.ClarifaiAsync;
import edu.illinois.finalproject.clarifai.ClarifaiManager;
import edu.illinois.finalproject.R;

/**
 * This activity shows the user details about the photo they just took. It will display a map
 * with a marker at the location where the photo was taken and the actual photo itself.
 */
public class UploadActivity extends AppCompatActivity {

    public static final String CAPTURED_PHOTO_NAME = "photoName";
    public static final int DEFAULT_ZOOM = 15;

    private Bitmap capturedBitmap;
    private ImageView capturedImageView;
    private LatLng photoCoord;

    private int currentPage = 0;

    private FragmentTransaction transaction;

    private UploadLocationFragment locationFragment;
    private AddTagFragment tagFragment;

    /**
     * Creates the activity and sets the views to private instance variables. It receives and intent
     * to retrieve the location of the picture that was taken. Then, it sets the image view to
     * that picture's bitmap while deleting the photo itself from the phone.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        setupToolbar();

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
                                photoCoord = new LatLng(location.getLatitude(), location.getLongitude());
                            }
                        }
                    });
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        capturedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageData = stream.toByteArray();
        locationFragment = new UploadLocationFragment();
        tagFragment = AddTagFragment.newInstance(imageData);

        commitFragment(locationFragment);
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

    public void onBackButtonPressed() {
        switch (currentPage) {
            case 0:
                onBackPressed();
                finish();
                break;
            case 1:
                commitFragment(locationFragment);
                break;
        }

        currentPage -= 1;
    }

    public void onNextButtonPressed() {
        switch (currentPage) {
            case 0:
                commitFragment(tagFragment);
                break;
            case 1:
                break;
        }
        currentPage += 1;
    }

    public void commitFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    /**
     * https://stackoverflow.com/questions/26651602/display-back-arrow-on-toolbar-android
     */
    public void setupToolbar() {
        setTitle("Confirm Location");

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Drawable backArrow = getResources().getDrawable(R.drawable.back_arrow);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.upload_menu, menu);

        MenuItem nextArrow = menu.findItem(R.id.action_next);

        final Drawable nextArrowIcon = nextArrow.getIcon();
        nextArrowIcon.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent),
                PorterDuff.Mode.SRC_ATOP);

        nextArrow.setIcon(nextArrowIcon);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackButtonPressed();
                return true;
            case R.id.action_next:
                onNextButtonPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}