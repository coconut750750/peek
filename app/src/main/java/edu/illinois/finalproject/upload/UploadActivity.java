package edu.illinois.finalproject.upload;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.clarifai.ClarifaiAsync;
import edu.illinois.finalproject.firebase.Picture;
import edu.illinois.finalproject.main.ProgressDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * This activity shows the user details about the photo they just took. It will display a map
 * with a marker at the location where the photo was taken and the actual photo itself.
 */
public class UploadActivity extends AppCompatActivity {
    public static final String DATA_FORMAT = "dd MMMM yyyy_hh:mm a";

    public static final String PHOTOS_REF = "photo_ids";
    public static final String USER_PHOTOS_REF = "user_photos";
    public static final String CAPTURED_PHOTO_NAME = "photoName";
    public static final int DEFAULT_ZOOM = 15;

    private Bitmap capturedBitmap;
    private LatLng photoCoord;

    private int currentPage = 0;

    private FragmentTransaction transaction;

    private ConfirmLocationFragment locationFragment;
    private AddTagFragment tagFragment;
    private TagsAdapter tagsAdapter;
    private TextView toolbarTitle;

    private ClarifaiAsync clarifaiAsync;

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

        Intent passedIntent = getIntent();
        String photoName = passedIntent.getStringExtra(CAPTURED_PHOTO_NAME);
        String photoPath = getFileStreamPath(photoName).getPath();

        // read bitmap data from File object
        // source: https://stackoverflow.com/questions/8710515/reading-an-image-file-into-
        // bitmap-from-sdcard-why-am-i-getting-a-nullpointerexc
        // need to rotate image 90 degrees because camera saves image in landscape mode by default
        capturedBitmap = rotateImage(BitmapFactory.decodeFile(photoPath));

        // delete the file from SD card
        File photoFile = new File(photoPath);
        photoFile.delete();

        // get current location
        // https://developer.android.com/training/location/retrieve-current.html
        FusedLocationProviderClient mFusedLocationClient;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        boolean fineLocationPermission = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
        boolean coarseLocationPermission = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
        if (fineLocationPermission || coarseLocationPermission) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                photoCoord = new LatLng(location.getLatitude(),
                                        location.getLongitude());
                            }
                        }
                    });
        }

        tagsAdapter = new TagsAdapter(this);
        clarifaiAsync = new ClarifaiAsync(tagsAdapter);
        clarifaiAsync.execute(capturedBitmap);

        tagFragment = new AddTagFragment();
        locationFragment = new ConfirmLocationFragment();
        commitFragment(tagFragment);
    }

    public TagsAdapter getTagsAdapter() {
        return tagsAdapter;
    }

    public Bitmap getCapturedBitmap() {
        return capturedBitmap;
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

    public void commitFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        if (currentPage == 0) {
            transaction.setCustomAnimations(R.anim.right_slide_in, R.anim.left_slide_out);
        } else {
            transaction.setCustomAnimations(R.anim.left_slide_in, R.anim.right_slide_out);
        }

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    public void onBackButtonPressed() {
        switch (currentPage) {
            case 0:
                onBackPressed();
                finish();
                break;
            case 1:
                toolbarTitle.setText(getResources().getString(R.string.add_tags));
                commitFragment(tagFragment);
                break;
        }

        currentPage -= 1;
    }

    public void onNextButtonPressed() {
        switch (currentPage) {
            case 0:
                toolbarTitle.setText(getResources().getString(R.string.confirm_location));
                commitFragment(locationFragment);
                break;
            case 1:
                // show a progress dialog to users
                ProgressDialog.show(this, getResources().getString(R.string.uploading) );
                uploadPictureToFirebase();
                break;
        }
        currentPage += 1;
    }

    /**
     *
     */
    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getResources().getString(R.string.add_tags));
        setSupportActionBar(toolbar);

        ImageView backButton = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackButtonPressed();
            }
        });

        ImageView nextButton = (ImageView) toolbar.findViewById(R.id.toolbar_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextButtonPressed();
            }
        });
    }

    /**
     * https://github.com/chrisjenx/Calligraphy
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void uploadPictureToFirebase() {
        // get photo id and user info
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference photoRef = FirebaseDatabase.getInstance()
                .getReference(PHOTOS_REF).push();
        final String photoId = photoRef.getKey();
        final String userId = user.getUid();
        final String uploadLoc = String.format("%s/%s.jpg", userId,  photoId);
        final String userName = user.getDisplayName();

        // put photo into storage
        StorageReference uploadRef = FirebaseStorage.getInstance().getReference().child(uploadLoc);

        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        capturedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteOutputStream);
        byte[] imageData = byteOutputStream.toByteArray();

        // create picture object to upload to firebase
        final String timeStamp = new SimpleDateFormat(DATA_FORMAT, Locale.ENGLISH)
                .format(new Date());
        final List<String> tags = tagsAdapter.getClickedTags();
        uploadRef.putBytes(imageData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ProgressDialog.hide();

                String downloadUri = taskSnapshot.getDownloadUrl().toString();
                Picture capturedPicture =  new Picture(uploadLoc, downloadUri, photoCoord, tags, userName, timeStamp);
                photoRef.setValue(capturedPicture);
                // once all data of picture is aggregated, finish the upload activity
                finish();
            }
        });

        // add picture id to user's list of picture ids
        DatabaseReference userPhotoRef = FirebaseDatabase.getInstance()
                .getReference(USER_PHOTOS_REF)
                .child(userId);
        userPhotoRef.push().setValue(photoId);
    }
}