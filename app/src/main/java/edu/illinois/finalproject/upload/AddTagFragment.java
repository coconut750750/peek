package edu.illinois.finalproject.upload;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.clarifai.ClarifaiAsync;

/**
 *
 */
public class AddTagFragment extends Fragment {
    
    public static final String IMAGE_KEY = "tagsArray";
    private TagsAdapter tagsAdapter;

    public AddTagFragment() {
        tagsAdapter = new TagsAdapter();
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param imageBytes
     * @return A new instance of fragment test.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTagFragment newInstance(byte[] imageBytes) {
        AddTagFragment fragment = new AddTagFragment();
        Bundle args = new Bundle();
        args.putByteArray(IMAGE_KEY, imageBytes);
        fragment.setArguments(args);
        return fragment;
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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        TagsAdapter tagsAdapter = ((UploadActivity) getActivity()).getTagsAdapter();
        mRecyclerView.setAdapter(tagsAdapter);
        tagsAdapter.notifyDataSetChanged();

        return view;
    }
}
