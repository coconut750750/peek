package edu.illinois.finalproject.firebase;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

/***
 * Created by Brandon on 12/3/17.
 */

public class Picture {

    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lon";

    public String storageLocation;
    public String uri;
    public HashMap<String, Double> coord;
    public List<String> tags;
    public String name;
    public String datetime;
    private Bitmap bitmap;

    public Picture(String storageLocation, String uri, LatLng coord, List<String> tags, String name,
                   String datetime) {
        this.storageLocation = storageLocation;
        this.uri = uri;
        this.tags = tags;
        this.name = name;
        this.datetime = datetime;
        this.coord = new HashMap<>();
        this.coord.put(LATITUDE, coord.latitude);
        this.coord.put(LONGITUDE, coord.longitude);
    }

    public Picture(Bitmap bitmap, List<String> tags, String name, String datetime) {
        this.bitmap = bitmap;
        this.tags = tags;
        this.name = name;
        this.datetime = datetime;
    }

    public Picture() {
        // need this to retrieve Picture from firebase
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public HashMap<String, Double> getCoord() {
        return coord;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getUri() {
        return uri;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
