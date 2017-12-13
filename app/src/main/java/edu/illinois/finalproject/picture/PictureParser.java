package edu.illinois.finalproject.picture;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import edu.illinois.finalproject.R;

/**
 * Created by Brandon on 12/10/17.
 * This is a static class that is used to insert the info of a Picture object into the Picture
 * detail view (which is mainly used by the MapMarkerAdapter and the UserUploadsAdapter). These
 * methods require that the Picture must have a name, a datetime, a bitmap/uri, and a list of tags.
 */

public class PictureParser {

    // the number of columns for the PictureInfoTagsAdapter
    private static final int GRID_LAYOUT_SPAN = 2;
    // the character separating the date and time of the publish time of the picture
    private static final String DATETIME_SEPARATOR = "_";
    // how big the bitmap should be displayed compared to is original size
    private static final float DISPLAY_BITMAP_SCALE = 0.5f;

    /**
     * This is the only public method that can be called. It take in a Picture Object and puts its
     * details into the pictureInfoView.
     *
     * @param completePicture the Picture object that has all the requirements
     * @param pictureInfoView the layout that has all the views to hold the details
     * @return the final view with all the elements in its place
     */
    public static View insertPicInfo(Picture completePicture, View pictureInfoView) {
        insertPicName(completePicture.getName(), pictureInfoView);

        insertPicDatetime(completePicture.getDatetime(), pictureInfoView);

        if (completePicture.getBitmap() != null) {
            insertPicImage(completePicture.getBitmap(), pictureInfoView);
        } else {
            insertPicImage(completePicture.getUri(), pictureInfoView);
        }

        insertPicTags(completePicture.getTags(), pictureInfoView);

        return pictureInfoView;
    }

    /**
     * Inserts name into the layout
     *
     * @param name            String name of picture uploader
     * @param pictureInfoView the info view for the picture
     */
    private static void insertPicName(String name, View pictureInfoView) {
        TextView nameText = (TextView) pictureInfoView.findViewById(R.id.uploader_name);
        nameText.setText(name);
    }

    /**
     * Inserts datetime into the layout
     *
     * @param datetime        String datetime of picture upload time
     * @param pictureInfoView the info view for the picture
     */
    private static void insertPicDatetime(String datetime, View pictureInfoView) {
        // set datetime
        String[] datetimeArr = datetime.split(DATETIME_SEPARATOR);
        String date = datetimeArr[0];
        TextView dateText = (TextView) pictureInfoView.findViewById(R.id.date);
        dateText.setText(date);
        String time = datetimeArr[1];
        TextView timeText = (TextView) pictureInfoView.findViewById(R.id.time);
        timeText.setText(time);
    }

    /**
     * Inserts bitmap into the layout, using the Glide Library. Used if the Bitmap of the
     * Picture object is null.
     *
     * @param uri             String uri of where the bitmap is stored.
     * @param pictureInfoView the info view for the picture
     */
    private static void insertPicImage(String uri, View pictureInfoView) {
        // set image
        final ImageView imageView = (ImageView) pictureInfoView.findViewById(R.id.info_image);

        //https://github.com/codepath/android_guides/wiki/Displaying-Images-with-the-Glide-Library
        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                imageView.setImageBitmap(getScaledBitmap(bitmap));
            }
        };

        Glide.with(pictureInfoView.getContext())
                .load(uri)
                .asBitmap()
                .into(target);
    }

    /**
     * Inserts bitmap into the layout
     *
     * @param bitmap          Bitmap of picture
     * @param pictureInfoView the info view for the picture
     */
    private static void insertPicImage(Bitmap bitmap, View pictureInfoView) {
        // set image
        final ImageView imageView = (ImageView) pictureInfoView.findViewById(R.id.info_image);

        imageView.setImageBitmap(getScaledBitmap(bitmap));
    }

    /**
     * Returns a new Bitmap that is Scaled according to {DISPLAY_BITMAP_SCALE}
     *
     * @param bitmap the bitmap to be resized
     */
    private static Bitmap getScaledBitmap(Bitmap bitmap) {
        int newWidth = (int) (bitmap.getWidth() * DISPLAY_BITMAP_SCALE);
        int newHeight = (int) (bitmap.getHeight() * DISPLAY_BITMAP_SCALE);
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
    }

    /**
     * Inserts picture tags into the layout, using the PictureInfoTagsAdapter
     * @param tags list of tags of picture
     * @param pictureInfoView the info view for the picture
     */
    private static void insertPicTags(List<String> tags, View pictureInfoView) {
        Context context = pictureInfoView.getContext();
        RecyclerView tagsRecycler = (RecyclerView) pictureInfoView
                .findViewById(R.id.info_tags_recycler);
        tagsRecycler.setLayoutManager(new GridLayoutManager(context,
                GRID_LAYOUT_SPAN, GridLayoutManager.VERTICAL, false));

        PictureInfoTagsAdapter infoTagsAdapter = new PictureInfoTagsAdapter(tags);
        tagsRecycler.setAdapter(infoTagsAdapter);
    }
}