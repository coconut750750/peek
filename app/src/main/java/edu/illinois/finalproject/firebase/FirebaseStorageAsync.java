package edu.illinois.finalproject.firebase;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.illinois.finalproject.map.MapManager;

/**
 * Created by Brandon on 12/9/17.
 */

public class FirebaseStorageAsync extends AsyncTask<Picture, Integer, Picture> {
    private MapManager mapManager;

    public FirebaseStorageAsync(MapManager mapManager) {
        this.mapManager = mapManager;
    }

    @Override
    protected Picture doInBackground(Picture... pictures) {
        Picture downloadPic = pictures[0];
        String uri = downloadPic.getUri();
        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            downloadPic.setBitmap(BitmapFactory.decodeStream(input));

            return downloadPic;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Picture picture) {
        if (picture != null) {
//            int newWidth = bitmap.getWidth() / 3;
//            int newHeight = bitmap.getHeight() / 3;
//            Bitmap displayPicture = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);

            mapManager.addPicture(picture);
            mapManager.displayPicture(picture);
        }
    }
}
