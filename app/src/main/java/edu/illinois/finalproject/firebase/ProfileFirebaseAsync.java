package edu.illinois.finalproject.firebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.illinois.finalproject.profile.UserUploadsAdapter;

/**
 * Created by Brandon on 12/10/17.
 */

public class ProfileFirebaseAsync extends AsyncTask<Picture, Integer, Picture> {

    private UserUploadsAdapter adapter;

    public ProfileFirebaseAsync(UserUploadsAdapter adapter) {
        this.adapter = adapter;
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
    protected void onPostExecute(Picture downloadedPicture) {
        if (downloadedPicture == null) {
            return;
        }
        super.onPostExecute(downloadedPicture);
        adapter.addImages(downloadedPicture);
        adapter.notifyDataSetChanged();
    }
}
