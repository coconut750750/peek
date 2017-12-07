package edu.illinois.finalproject.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brandon on 12/6/17.
 */

public class BitmapRetrieval extends AsyncTask<String, Integer, List<Bitmap>> {

    private ProfilePictureAdapter adapter;

    public BitmapRetrieval(ProfilePictureAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected List<Bitmap> doInBackground(String... strings) {
        List<Bitmap> bitmaps = new ArrayList<>();
        for (String url : strings) {
            bitmaps.add(getBitmapFromURL(url));
        }
        return bitmaps;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
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
    protected void onPostExecute(List<Bitmap> bitmap) {
        adapter.setImages(bitmap);
        adapter.notifyDataSetChanged();
    }
}
