package edu.illinois.finalproject.upload;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import edu.illinois.finalproject.R;

/**
 *
 */
public class AddTagFragment extends Fragment {

    private Bitmap capturedBitmap;
    private TagsAdapter clarifaiTagsAdapter;
    private TagsAdapter customTagsAdapter;
    private EditText tagsEdit;

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

        setupTagRecyclers(view);

        final ImageView capturedImageView = (ImageView) view.findViewById(R.id.captured_image);
        capturedBitmap = ((UploadActivity) getActivity()).getCapturedBitmap();

        tagsEdit = (EditText) view.findViewById(R.id.edit_tags_text);
        ImageView submitTags = (ImageView) view.findViewById(R.id.submit_tags);
        submitTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customTag = tagsEdit.getText().toString();
                if (!"".equals(customTag)) {
                    customTagsAdapter.addTags(customTag);
                    customTagsAdapter.notifyDataSetChanged();
                    tagsEdit.setText("");
                }
            }
        });

        // finds a cropped section of the picture to show the user
        // source: https://stackoverflow.com/questions/6908604/android-crop-center-of-bitmap
        ViewTreeObserver vto = capturedImageView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                capturedImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = capturedImageView.getMeasuredWidth();
                int height = capturedImageView.getMeasuredHeight();

                int xStart = 0;
                int yStart = (capturedBitmap.getHeight() - height) / 2;
                Bitmap croppedBitmap = Bitmap.createBitmap(capturedBitmap, xStart, yStart,
                        width, height);

                capturedBitmap = croppedBitmap;
                capturedImageView.setImageBitmap(croppedBitmap);
            }
        });

        return view;
    }

    private void setupTagRecyclers(View view) {
        RecyclerView clarifaiRecyclerView = (RecyclerView) view.findViewById(R.id.clarifai_tag_recycler);
        LinearLayoutManager clarifaiLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        clarifaiRecyclerView.setLayoutManager(clarifaiLayoutManager);

        RecyclerView customRecyclerView = (RecyclerView) view.findViewById(R.id.custom_tag_recycler);
        LinearLayoutManager customLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        customRecyclerView.setLayoutManager(customLayoutManager);

        clarifaiTagsAdapter = ((UploadActivity) getActivity()).getClarifaiTagsAdapter();
        clarifaiRecyclerView.setAdapter(clarifaiTagsAdapter);
        clarifaiTagsAdapter.notifyDataSetChanged();

        customTagsAdapter = ((UploadActivity) getActivity()).getCustomTagsAdapter();
        customRecyclerView.setAdapter(customTagsAdapter);
        customTagsAdapter.notifyDataSetChanged();
    }
}
