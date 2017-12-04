package edu.illinois.finalproject.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.TextureView;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.List;

/**
 * This is the TextureListener object that the Camera Fragment uses. It projects the Camera preview
 * onto the TextureView and allows the Camera to start its preview.
 */

public class CameraTextureListener implements TextureView.SurfaceTextureListener {

    private Camera mCamera;
    private TextureView mTextureView;

    public CameraTextureListener(Camera camera, TextureView textureView) {
        mCamera = camera;
        mTextureView = textureView;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        displayCamera(surfaceTexture);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        releaseCamera();
        return true;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        // need to implement but don't need functionality
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        // need to implement but don't need functionality
    }

    /**
     * This method is called when the Surface (used to display the Camera preview) is available. It
     * configures the Camera parameters to fit the full screen, then starts the preview.
     *
     * @param surfaceTexture the SurfaceTexture object the Camera will draw on
     */
    public void displayCamera(SurfaceTexture surfaceTexture) {
        if (mCamera == null) {
            return;
        }

        // make camera portrait mode
        final int ANGLE = 90;
        mCamera.setDisplayOrientation(ANGLE);

        Camera.Parameters params = mCamera.getParameters();

        // let camera have access to auto focus mode
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        // set the preview of the camera to the optimal size for the screen
        Camera.Size optimalSize = getOptimalPreviewSize(params.getSupportedPreviewSizes(),
                mTextureView.getMeasuredWidth(), mTextureView.getMeasuredHeight());
        params.setPreviewSize(optimalSize.width, optimalSize.height);

        mCamera.setParameters(params);

        // updates the size of the view to avoid distortion
        updateViewSize(optimalSize);

        try {
            mCamera.setPreviewTexture(surfaceTexture);
        } catch (IOException t) {
            // don't need extra functionality
        }

        mCamera.startPreview();
    }

    /**
     * Gets the optimal preview size from the supported sizes with respect to the given width and
     * heights.
     * Source: https://stackoverflow.com/questions/19577299/android-camera-preview-stretched
     */
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> supportSizes, int w, int h) {
        double targetRatio = (double) w / h;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        for (Camera.Size size : supportSizes) {
            double ratio = (double) size.height / size.width;
            if (Math.abs(ratio - targetRatio) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(ratio - targetRatio);
            }
        }
        return optimalSize;
    }

    /**
     * After finding out the optimal size for the Camera preview, the size of the TextureView must
     * also be adjusted to further avoid distortion. This usually means the TextureView will
     * acquire a width or height larger than the screen size since the available Camera aspect
     * ratios will most likely differ from the screen aspect ratio.
     *
     * I found the method to achieve this here:
     * https://stackoverflow.com/questions/29352406/android-full-screen-camera-while-keeping-the-
     * camera-selected-ratio
     *
     * @param optimalSize
     */
    private void updateViewSize(Camera.Size optimalSize) {
        // must swap width and height because Camera default orientation is landscape
        int width = optimalSize.height;
        int height = optimalSize.width;
        float previewProportion = (float) width / (float) height;

        int screenWidth = mTextureView.getMeasuredWidth();
        int screenHeight = mTextureView.getMeasuredHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;

        FrameLayout.LayoutParams mTextureViewLayoutParams =
                (FrameLayout.LayoutParams) mTextureView.getLayoutParams();

        if (previewProportion < screenProportion) {
            // if the preview ratio is less than the screen ratio, it means the width of the
            // preview is smaller than the screen's. So, set the screen width to the preview width
            // and calculate a new screen height (usually larger than screen height)
            mTextureViewLayoutParams.width = screenWidth;
            mTextureViewLayoutParams.height = (int) ((float) screenWidth / previewProportion);
        } else {
            mTextureViewLayoutParams.height = screenHeight;
            mTextureViewLayoutParams.width = (int) (previewProportion * (float) screenHeight);
        }

        mTextureView.setLayoutParams(mTextureViewLayoutParams);
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}
