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
import android.widget.EditText;
import android.widget.ImageView;

import edu.illinois.finalproject.R;

/**
 * The first Fragment to be displayed in the Upload Activity. It gives the users options to select
 * tags from the Clarifai API, and lets the users create their own tags.
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

    /**
     * Sets up RecyclerViews, sets up the tags EditTextView, and sets up the ImageView.
     *
     * @param inflater           passed by Android System, used to inflate the layout for this fragment
     * @param container          passed by Android System
     * @param savedInstanceState passed by Android System
     * @return the fragment view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_tag, container, false);

        setupTagRecyclers(view);
        setupEditText(view);
        setupImage(view);

        return view;
    }

    /**
     * Sets up the two TagAdapters
     *
     * @param view the Fragment View
     */
    private void setupTagRecyclers(View view) {
        clarifaiTagsAdapter = ((UploadActivity) getActivity()).getClarifaiTagsAdapter();
        setupRecycler(view, R.id.clarifai_tag_recycler, clarifaiTagsAdapter);

        customTagsAdapter = ((UploadActivity) getActivity()).getCustomTagsAdapter();
        setupRecycler(view, R.id.custom_tag_recycler, customTagsAdapter);
    }

    /**
     * Sets up a single Recycler view by getting it from the view, setting the layout manager,
     * setting the adapter, and notifying the adapter that the dataset changed.
     *
     * @param view    the Fragment View
     * @param id      int id of the RecyclerView
     * @param adapter the adapter for that RecyclerView
     */
    private void setupRecycler(View view, int id, TagsAdapter adapter) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(id);
        LinearLayoutManager customLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(customLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * Sets up the EditTextView such that when the imageView is clicked, whatever is in the tags
     * view will be added to the customTagsAdapter
     *
     * @param view the Fragment View
     */
    private void setupEditText(View view) {
        tagsEdit = (EditText) view.findViewById(R.id.edit_tags_text);
        ImageView submitTags = (ImageView) view.findViewById(R.id.submit_tags);
        submitTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] customTags = tagsEdit.getText().toString().split(" ");
                for (String tag : customTags) {
                    customTagsAdapter.addTags(tag);
                    customTagsAdapter.notifyDataSetChanged();
                    tagsEdit.setText("");
                }
            }
        });
    }

    /**
     * Finds and displays a cropped section of the picture to display into the ImageView.
     * <p>
     * Source: https://stackoverflow.com/questions/6908604/android-crop-center-of-bitmap
     *
     * @param view the Fragment View
     */
    private void setupImage(View view) {
        final ImageView capturedImageView = (ImageView) view.findViewById(R.id.captured_image);
        capturedBitmap = ((UploadActivity) getActivity()).getCapturedBitmap();

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
    }
}
