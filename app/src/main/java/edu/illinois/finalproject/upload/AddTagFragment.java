package edu.illinois.finalproject.upload;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        List<String> tags = new ArrayList<>();
        tags.add("beach");
        tags.add("sand");
        tags.add("sun");
        TagsAdapter tagsAdapter = new TagsAdapter(tags);
        mRecyclerView.setAdapter(tagsAdapter);
        tagsAdapter.notifyDataSetChanged();

        return view;
    }
}
