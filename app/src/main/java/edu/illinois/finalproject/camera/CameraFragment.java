package edu.illinois.finalproject.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.illinois.finalproject.R;

import static android.content.Context.MODE_PRIVATE;

/**
 *
 */
public class CameraFragment extends Fragment {
    // app defined constant to determine the permission that was requested
    public static final int PERMISSIONS_ALL = 1;

    // extension of the saved picture
    public static final String PHOTO_EXTENSION = ".jpg";
    // the format of the date that will be used to give the saved picture a name
    public static final String DATA_FORMAT = "yyyyMMdd_HHmmss";

    private Camera camera;

    /**
     * Picture Callback object the camera will use when taking a photo. When a picture is taken,
     * the data of the picture will be saved onto the phone. This is because the bitmap of the
     * picture will most likely be to big to transfer to another activity through a bundle. So,
     * instead, the location of the saved photo will be passed. After accessing the photo in the
     * next activity, the photo file will be deleted.
     */
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // saves picture data into a file to transfer the data to the next activity
            // source: https://developer.android.com/guide/topics/media/camera.html
            String timeStamp = new SimpleDateFormat(DATA_FORMAT, Locale.ENGLISH)
                    .format(new Date());
            String photoPath = timeStamp + PHOTO_EXTENSION;

            try {
                // creates a file output stream to write the photo data to
                FileOutputStream fileOutStream = getActivity().openFileOutput(photoPath, MODE_PRIVATE);
                fileOutStream.write(data);
                fileOutStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // moves to the next activity, the photo detail activity
            //startCapturedImageActivity(photoPath);
        }
    };

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        askForPermissions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        Button captureButton = (Button) view.findViewById(R.id.take_reset_picture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture(null, null, pictureCallback);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // if camera permission granted, start camera
        int cameraPermissionCheck = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA);
        if (cameraPermissionCheck == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        }
    }

    /**
     * This is called when a permission has been granted or denied. If the camera permission has
     * been accepted, start the camera.
     *
     * @param requestCode the app defined code that will identify which permission has feedback
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_ALL:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                }
        }
    }

    /**
     * Gets an instance of a Camera object. Using a deprecated version of Camera to support older
     * versions of android.
     *
     * @return A camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * Starts the camera by getting a camera instance and displaying the camera's image to the
     * layout.
     */
    private void startCamera() {
        // Create an instance of Camera
        camera = getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        CameraView mPreview = new CameraView(getContext(), camera);
        FrameLayout preview = (FrameLayout) (getActivity().findViewById(R.id.camera_view));
        preview.addView(mPreview);
    }

    /**
     * Asks for the permissions that this apps needs: camera, writing external storage, and
     * accessing location data
     */
    private void askForPermissions() {
        String[] permissionsToAsk = {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if(!hasPermissions(permissionsToAsk)){
            ActivityCompat.requestPermissions(getActivity(), permissionsToAsk, PERMISSIONS_ALL);
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
                if (ActivityCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
