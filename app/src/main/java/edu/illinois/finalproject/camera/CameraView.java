package edu.illinois.finalproject.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

/***
 * This class is a surface view object that will be put onto the layout to display the image of the
 * camera to the user.
 * Got foundational code from here: https://developer.android.com/guide/topics/media/camera.html
 */

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera camera;

    public CameraView(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    /**
     * When the surface is created, tell the camera object to draw its preview onto the holder
     * object. Then, set parameters to the camera such as orientation and auto focus.
     *
     * @param holder the SurfaceHolder object the camera will draw its preview onto.
     */
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);

            // make camera portrait mode
            final int ANGLE = 90;
            camera.setDisplayOrientation(ANGLE);

            // let camera auto focus
            Camera.Parameters params = camera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

            // sets the aspect ratio of the camera
            List<Camera.Size> previewSizes = camera.getParameters().getSupportedPreviewSizes();
            Camera.Size optimalSize = getOptimalPreviewSize(previewSizes,
                    holder.getSurfaceFrame().width(), holder.getSurfaceFrame().height());
            params.setPreviewSize(optimalSize.width, optimalSize.height);

            camera.setParameters(params);

            camera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.d(TAG, "Camera released");
        }
    }

    /**
     * When this surface is destroyed, stop the camera, release it, and set it to null so that
     * the application can continue running.
     *
     * @param holder the SurfaceHolder object of the camera
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * Gets the optimal preview size from the supported sizes with respect to the given width and
     * heights.
     * SourcE: https://stackoverflow.com/questions/19577299/android-camera-preview-stretched
     */
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> supportSizes, int w, int h) {
        double targetRatio = (double) h / w;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        for (Camera.Size size : supportSizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(ratio - targetRatio);
            }
        }
        return optimalSize;
    }
}
