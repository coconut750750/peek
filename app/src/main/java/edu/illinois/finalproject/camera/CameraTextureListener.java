package edu.illinois.finalproject.camera;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.TextureView;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.List;

/***
 * Created by Brandon on 12/3/17.
 */

public class CameraTextureListener implements TextureView.SurfaceTextureListener {

    private Camera mCamera;
    private FrameLayout mFrameLayout;
    private TextureView mTextureView;

    public CameraTextureListener(Camera camera, TextureView textureView, FrameLayout frameLayout) {
        mCamera = camera;
        mFrameLayout = frameLayout;
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

    public void displayCamera(SurfaceTexture surfaceTexture) {
        if (mCamera == null) {
            return;
        }

        // make camera portrait mode
        final int ANGLE = 90;
        mCamera.setDisplayOrientation(ANGLE);

        Camera.Parameters params = mCamera.getParameters();

        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        Camera.Size optimalSize = getOptimalPreviewSize(params.getSupportedPreviewSizes(),
                mFrameLayout.getMeasuredWidth(), mFrameLayout.getMeasuredHeight());
        params.setPreviewSize(optimalSize.width, optimalSize.height);

        mCamera.setParameters(params);

        updateFrameLayout(optimalSize);

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
     * https://stackoverflow.com/questions/29352406/android-full-screen-camera-while-keeping-the-camera-selected-ratio
     * @param optimalSize
     */
    private void updateFrameLayout(Camera.Size optimalSize) {
        // must swap width and height because Camera default orientation is landscape
        int width = optimalSize.height;
        int height = optimalSize.width;

        Log.d("asdf", "w: "+width+" h: "+height);

        float previewProportion = (float) width / (float) height;

        int screenWidth = mTextureView.getMeasuredWidth();
        int screenHeight = mTextureView.getMeasuredHeight();

        Log.d("asdf", "w: "+screenWidth+" h: "+screenHeight);


        float screenProportion = (float) screenWidth / (float) screenHeight;

        FrameLayout.LayoutParams mTextureViewLayoutParams =
                (FrameLayout.LayoutParams)mTextureView.getLayoutParams();

        if (previewProportion < screenProportion) {
            // if the preview ratio is less than the screen ratio, it means the width of the
            // preview is smaller than the screen's, so, set the screen width to the preview width
            // and calculate a new screen height
            mTextureViewLayoutParams.width = screenWidth;
            mTextureViewLayoutParams.height = (int) ((float) screenWidth / previewProportion);
        } else {
            mTextureViewLayoutParams.width = (int) (previewProportion * (float) screenHeight);
            mTextureViewLayoutParams.height = screenHeight;
        }

        Log.d("asdf", "w: "+mTextureViewLayoutParams.width+" h: "+mTextureViewLayoutParams.height);


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
