package edu.illinois.finalproject.picture;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Brandon on 12/3/17.
 * This is the Picture JSON Object that will be stored into the Firebase Database and that will be
 * used to retrieve JSON Objects from Firebase.
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

    /**
     * This constructor is used when a Picture is being ready to be uploading to Firebase
     *
     * @param storageLocation location where the picture is stored in firebase
     * @param uri             uri of the image
     * @param coord           geo location of the picture
     * @param tags            list of tags of the picture
     * @param name            string name of the uploader
     * @param datetime        string when the picture was taken
     */
    public Picture(String storageLocation, String uri, LatLng coord, List<String> tags, String name,
                   String datetime) {
        this(tags, name, datetime);
        this.coord = new HashMap<>();
        this.coord.put(LATITUDE, coord.latitude);
        this.coord.put(LONGITUDE, coord.longitude);
        this.storageLocation = storageLocation;
        this.uri = uri;
    }

    /**
     * This constructor is used when a Picture is ready to be displayed to the Map, but not ready
     * to be uplaoded to Firebase
     *
     * @param bitmap   bitmap of the image
     * @param tags     list of tags of the image
     * @param name     name of uploader
     * @param datetime string when the picture was taken
     */
    public Picture(Bitmap bitmap, List<String> tags, String name, String datetime) {
        this(tags, name, datetime);
        this.bitmap = bitmap;

    }

    public Picture(List<String> tags, String name, String datetime) {
        this.tags = tags;
        this.name = name;
        this.datetime = datetime;
    }

    public Picture() {
        // need this empty constructor to retrieve Picture objects from Firebase
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

    public void setCoord(HashMap<String, Double> coord) {
        this.coord = coord;
    }
}
