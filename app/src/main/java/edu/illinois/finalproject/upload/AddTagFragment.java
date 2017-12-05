package edu.illinois.finalproject.upload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import edu.illinois.finalproject.R;

/**
 *
 */
public class AddTagFragment extends Fragment {

    public AddTagFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_tag, container, false);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.tag_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        TagsAdapter tagsAdapter = ((UploadActivity) getActivity()).getTagsAdapter();
        mRecyclerView.setAdapter(tagsAdapter);
        tagsAdapter.notifyDataSetChanged();

        final ImageView capturedImageView = (ImageView) view.findViewById(R.id.captured_image);
        final Bitmap capturedBitmap = ((UploadActivity) getActivity()).getCapturedBitmap();

        // finds a cropped section of the picture to show the user
        // source: https://stackoverflow.com/questions/6908604/android-crop-center-of-bitmap
        ViewTreeObserver vto = capturedImageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                capturedImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = capturedImageView.getMeasuredWidth();
                int height = capturedImageView.getMeasuredHeight();

                Bitmap croppedBitmap = Bitmap.createBitmap(
                        capturedBitmap,
                        0,
                        (capturedBitmap.getHeight() - height) / 2,
                        width,
                        height
                );

                capturedImageView.setImageBitmap(croppedBitmap);

            }
        });

        return view;
    }

}
