package edu.illinois.finalproject.camera;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.illinois.finalproject.R;

import static android.content.Context.MODE_PRIVATE;

/**
 *
 */
public class CameraFragment extends Fragment implements TextureView.SurfaceTextureListener {
    // extension of the saved picture
    public static final String PHOTO_EXTENSION = ".jpg";
    // the format of the date that will be used to give the saved picture a name
    public static final String DATA_FORMAT = "yyyyMMdd_HHmmss";

    private TextureView mTextureView;
    private Camera mCamera;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        mTextureView = (TextureView) view.findViewById(R.id.camera_view);
        mTextureView.setSurfaceTextureListener(this);

        Button captureButton = (Button) view.findViewById(R.id.take_reset_picture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.takePicture(null, null, pictureCallback);
            }
        });

        return view;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        mCamera = getCameraInstance();
        if (mCamera == null) {
            return;
        }

        // make camera portrait mode
        final int ANGLE = 90;
        mCamera.setDisplayOrientation(ANGLE);

        Camera.Parameters params = mCamera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        mCamera.setParameters(params);

        try {
            mCamera.setPreviewTexture(surfaceTexture);
        } catch (IOException t) {
            // don't need extra functionality
        }

        mCamera.startPreview();
        //mTextureView.setAlpha(1.0f);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        // need to implement but don't need functionality
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        // need to implement but don't need functionality
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
}
