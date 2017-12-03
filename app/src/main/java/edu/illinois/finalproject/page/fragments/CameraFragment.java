package edu.illinois.finalproject.page.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import edu.illinois.finalproject.CameraView;
import edu.illinois.finalproject.R;

/**
 *
 */
public class CameraFragment extends Fragment {

    private Camera camera;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);


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


}
