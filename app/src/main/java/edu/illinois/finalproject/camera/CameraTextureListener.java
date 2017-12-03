package edu.illinois.finalproject.camera;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.List;

/***
 * Created by Brandon on 12/3/17.
 */

public class CameraTextureListener implements TextureView.SurfaceTextureListener {

    private Camera mCamera;
    private TextureView mTextureView;
    private FrameLayout mFrameLayout;

    public CameraTextureListener(Camera camera, TextureView textureView, FrameLayout frameLayout) {
        mCamera = camera;
        mTextureView = textureView;
        mFrameLayout = frameLayout;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        //Log.d("asdf", "onSurfaceTextureAvailable: surface texture available");
        displayCamera(surfaceTexture);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        // need to implement but don't need functionality
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        releaseCamera();
        return true;
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
                mTextureView.getMeasuredWidth(), mTextureView.getMeasuredHeight());
        params.setPreviewSize(optimalSize.width, optimalSize.height);

        mCamera.setParameters(params);

        updateFrameLayout(optimalSize.width, optimalSize.height);

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

    private void updateFrameLayout(int width, int height) {
        float previewProportion = (float) width / (float) height;

        int screenWidth =  ((Activity)mFrameLayout.getContext()).getWindowManager().getDefaultDisplay()
                .getWidth();
        int screenHeight =    ((Activity)mFrameLayout.getContext()).getWindowManager().getDefaultDisplay()
                .getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;

        Log.d("preview prop", "w: "+width +" h: "+height+" prop: "+(double)width/height);

        // Get the SurfaceView layout parameters
        ViewPager.LayoutParams lp = (ViewPager.LayoutParams)mFrameLayout.getLayoutParams();

        if (previewProportion < screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / previewProportion);
        } else {
            lp.width = (int) (previewProportion * (float) screenHeight);
            lp.height = screenHeight;
        }

        Log.d("frame layout prop first", "w: "+lp.width +" h: "+lp.height+" prop: "+(double)lp.width/lp.height);

        Log.d("frame layout prop", "w: "+lp.width +" h: "+lp.height+" prop: "+(double)lp.width/lp.height);

        mFrameLayout.setLayoutParams(lp);
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}
