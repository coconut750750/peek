package edu.illinois.finalproject.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.firebase.Picture;
import edu.illinois.finalproject.main.ProgressDialog;

import static edu.illinois.finalproject.authentication.AuthenticationActivity.mGoogleApiClient;
import static edu.illinois.finalproject.upload.UploadActivity.PHOTOS_REF;
import static edu.illinois.finalproject.upload.UploadActivity.USER_PHOTOS_REF;

/**
 *
 */
public class ProfileFragment extends Fragment {

    public static final String URI_KEY = "uri";

    private FirebaseUser user;
    private UserUploadsAdapter postsAdapter;
    private TextView numPostsView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.page_profile, container, false);

        // set up toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        // get post text view
        numPostsView = (TextView) view.findViewById(R.id.num_posts);

        // set up profile image using glide
        ImageView profileImage = (ImageView) view.findViewById(R.id.profile_image);
        Glide.with(this).load(user.getPhotoUrl()).into(profileImage);

        // set up recycler view of pics
        RecyclerView postRecyclerView = (RecyclerView) view.findViewById(R.id.posts_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        postRecyclerView.setLayoutManager(layoutManager);
        postsAdapter = new UserUploadsAdapter(getContext());
        postRecyclerView.setAdapter(postsAdapter);

        // retrieve firebase uris
        DatabaseReference uploadsRef = FirebaseDatabase.getInstance().getReference(USER_PHOTOS_REF)
                .child(user.getUid());
        uploadsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, String>> t =
                        new GenericTypeIndicator<HashMap<String, String>>() {};
                HashMap<String, String> userPhotos = dataSnapshot.getValue(t);
                if (userPhotos != null) {
                    for (String uid : userPhotos.keySet()) {
                        getImageBitmap(userPhotos.get(uid));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // need to implement, no functionality needed
            }
        });

        return view;
    }

    private void getImageBitmap(String imageId) {
        DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference(PHOTOS_REF)
                .child(imageId);
        imageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Picture picture = dataSnapshot.getValue(Picture.class);

                postsAdapter.addImages(picture);
                postsAdapter.notifyDataSetChanged();

                numPostsView.setText(String.format(Locale.ENGLISH, "%d", postsAdapter.getItemCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // need to implement, no functionality needed
            }
        });
    }
}
