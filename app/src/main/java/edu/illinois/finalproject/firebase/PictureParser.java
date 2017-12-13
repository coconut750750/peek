package edu.illinois.finalproject.firebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.map.MapInfoTagsAdapter;

/**
 * Created by Brandon on 12/10/17.
 */

public class PictureParser {

    public static final int GRID_LAYOUT_SPAN = 2;
    private static final String DATETIME_SEPARATOR = "_";
    private static final float DISPLAY_BITMAP_SCALE = 0.5f;

    public static View insertPicInfo(Picture completePicture, View pictureInfoView) {
        insertPicName(completePicture.getName(), pictureInfoView);

        insertPicDatetime(completePicture.getDatetime(), pictureInfoView);

        insertPicImage(completePicture.getUri(), pictureInfoView);

        insertPicTags(completePicture.getTags(), pictureInfoView);

        return pictureInfoView;
    }

    private static void insertPicName(String name, View pictureInfoView) {
        TextView nameText = (TextView) pictureInfoView.findViewById(R.id.uploader_name);
        nameText.setText(name);
    }

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

    private static void insertPicImage(String uri, View pictureInfoView) {
        // set image
        final ImageView imageView = (ImageView) pictureInfoView.findViewById(R.id.info_image);

        //https://github.com/codepath/android_guides/wiki/Displaying-Images-with-the-Glide-Library
        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                int newWidth = (int) (bitmap.getWidth() * DISPLAY_BITMAP_SCALE);
                int newHeight = (int) (bitmap.getHeight() * DISPLAY_BITMAP_SCALE);
                Bitmap displayBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
                imageView.setImageBitmap(displayBitmap);
            }
        };

        Glide.with(pictureInfoView.getContext())
                .load(uri)
                .asBitmap()
                .into(target);
    }

    private static void insertPicTags(List<String> tags, View pictureInfoView) {
        // set tags
        Context context = pictureInfoView.getContext();
        RecyclerView tagsRecycler = (RecyclerView) pictureInfoView.findViewById(R.id.info_tags_recycler);
        tagsRecycler.setLayoutManager(new GridLayoutManager(context, GRID_LAYOUT_SPAN, GridLayoutManager.VERTICAL, false));
        MapInfoTagsAdapter infoTagsAdapter = new MapInfoTagsAdapter(context, tags);
        tagsRecycler.setAdapter(infoTagsAdapter);
    }
}