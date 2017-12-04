package edu.illinois.finalproject.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

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

    private TextView locationTextView;
    private GoogleApiClient googleApiClient;
    private Bitmap capturedBitmap;
    private double lat;
    private double lon;
    private GoogleMap gMap;

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

        // Adds the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.app_name));
            // add back button
            // https://stackoverflow.com/questions/15686555/display-back-button-on-action-bar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImageView capturedImageView = (ImageView) findViewById(R.id.captured_image);
        locationTextView = (TextView) findViewById(R.id.coordinate_text);

        Intent passedIntent = getIntent();
        String photoName = passedIntent.getStringExtra(CAPTURED_PHOTO_NAME);
        String photoPath = getFileStreamPath(photoName).getPath();

        // read bitmap data from File object
        // source: https://stackoverflow.com/questions/8710515/reading-an-image-file-into-
        // bitmap-from-sdcard-why-am-i-getting-a-nullpointerexc
        // need to rotate image 90 degrees because camera saves image in landscape mode by default
        capturedBitmap = rotateImage(BitmapFactory.decodeFile(photoPath));
        capturedImageView.setImageBitmap(capturedBitmap);

        // delete the file from SD card
        File photoFile = new File(photoPath);
        photoFile.delete();

        MapView mapView = (MapView) findViewById(R.id.map);
        mapManager = new MapManager(this, mapView, new ArrayList<LatLng>(), true);
        mapManager.startMap(savedInstanceState);
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

    /**
     * Adds the back button to the menu bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}