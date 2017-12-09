package edu.illinois.finalproject.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.illinois.finalproject.firebase.Picture;

/**
 * Created by Brandon on 12/9/17.
 */

public class FirebaseStorageAsync extends AsyncTask<Picture, Integer, Bitmap> {
    private StorageReference rootStorage;

    private MapManager mapManager;
    private LatLng bitmapGeoLocation;

    public FirebaseStorageAsync(MapManager mapManager, LatLng bitmapGeoLocation) {
        this.mapManager = mapManager;
        this.bitmapGeoLocation = bitmapGeoLocation;
        rootStorage = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected Bitmap doInBackground(Picture... pictures) {
        String uri = pictures[0].getUri();
        try {
            URL url = new URL(uri);
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
        if (bitmap != null) {
            int newWidth = bitmap.getWidth() / 3;
            int newHeight = bitmap.getHeight() / 3;
            Bitmap displayBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);

            mapManager.addBitmap(displayBitmap, bitmapGeoLocation);
            mapManager.displayBitmap(displayBitmap);
        }
    }
}
