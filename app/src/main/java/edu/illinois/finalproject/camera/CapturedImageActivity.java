package edu.illinois.finalproject.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.Locale;

import edu.illinois.finalproject.R;

/**
 * This activity shows the user details about the photo they just took. It will display a map
 * with a marker at the location where the photo was taken and the actual photo itself.
 */
public class CapturedImageActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks {

    public static final String CAPTURED_PHOTO_NAME = "photoName";
    public static final int DEFAULT_ZOOM = 15;

    private TextView locationTextView;
    private GoogleApiClient googleApiClient;
    private Bitmap capturedBitmap;
    private double lat;
    private double lon;
    private GoogleMap gMap;

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

        // create a google api client to access location
        // source: https://stackoverflow.com/questions/38242917/how-can-i-get-the-current-location-
        // android-studio
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        startMap(savedInstanceState);
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
     * When the Google API client connects, this method is called. The location is retrieved by
     * using Location Services. From the Location object, the latitude and longitude can be
     * retrieved.
     * Source: https://stackoverflow.com/questions/38242917/how-can-i-get-the-current-location-
     * android-studio
     *
     * @param connectionHint default param that is not used
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // permissions not granted so no map interaction
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            lat = lastLocation.getLatitude();
            lon = lastLocation.getLongitude();

            // moves camera of the map to the location of the picture with zoom of DEFAULT_ZOOM
            // adds a marker to show where picture was taking
            // shows where the user is currently
            // https://developers.google.com/maps/documentation/android-api/map-with-marker
            LatLng photoCoord = new LatLng(lat, lon);
            locationTextView.setText(String.format(Locale.ENGLISH, "Latitude: %s\nLongitude: %s",
                    lat, lon));
            gMap.setMyLocationEnabled(true); // displays current location
            populateMap(photoCoord);

        }
    }

    /**
     * This method is called if the connection to the google api client is suspended. Error handling
     * is done in this method. For the purpose of the tech demonstration, no error handling will be
     * used.
     */
    @Override
    public void onConnectionSuspended(int i) {

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

    /**
     * Sets up the mapView and sets the Google Map object. Configured map settings to a default
     * map type.
     *
     * @param savedInstanceState a bundle object the map view needs to create itself.
     */
    private void startMap(Bundle savedInstanceState) {
        // starts and displays the map view
        // https://stackoverflow.com/questions/16536414/how-to-use-mapview-in-android-using-google-map-v2
        MapView mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
        mapView.onResume();
    }

    /**
     * Populates the map with a marker at the given latitude and longitude. The title of the marker
     * will be "photo" but this will be changed to show the actual photo.
     *
     * @param photoCoord the LatLng object of the photo
     */
    private void populateMap(LatLng photoCoord) {
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(photoCoord, DEFAULT_ZOOM));
        gMap.addMarker(new MarkerOptions().position(photoCoord).title("Photo"));

        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
    }
}
