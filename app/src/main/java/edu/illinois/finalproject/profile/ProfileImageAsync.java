package edu.illinois.finalproject.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Brandon on 12/10/17.
 */

public class ProfileImageAsync extends AsyncTask<Uri, Integer, Bitmap> {

    private ImageView profileView;

    public ProfileImageAsync(ImageView profileImageView) {
        profileView = profileImageView;
    }

    @Override
    protected Bitmap doInBackground(Uri... uris) {
        try {
            URL url = new URL(uris[0].toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }

        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();

        Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        float radius = (height + width) / 4f;
        canvas.drawCircle(width / 2, height / 2, radius, paint);

        profileView.setImageBitmap(canvasBitmap);
    }
}
