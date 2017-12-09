package edu.illinois.finalproject.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.illinois.finalproject.R;

/**
 *
 */
public class ProfileFragment extends Fragment {

    // these pictures are for demonstration purposes
    private final String PICTURE1 = "https://www.catersnews.com/wp-content/uploads/2014/12/" +
            "3_CATERS_FOX_DIVE_04-800x498.jpg";
    private final String PICTURE2 = "https://www.petmd.com/sites/default/files/petmd-" +
            "cat-happy-10.jpg";
    private final String PICTURE3 = "https://www.cesarsway.com/sites/newcesarsway/files/styles/" +
            "large_article_preview/public/Common-dog-behaviors-explained.jpg?itok=FSzwbBoi";

    public ProfileFragment() {
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
        View view = inflater.inflate(R.layout.page_profile, container, false);

        //toolbar_upload
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        RecyclerView postRecyclerView = (RecyclerView) view.findViewById(R.id.posts_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        postRecyclerView.setLayoutManager(linearLayoutManager);

        ProfilePictureAdapter pictureAdapter = new ProfilePictureAdapter(null);
        postRecyclerView.setAdapter(pictureAdapter);

        return view;
    }


}
