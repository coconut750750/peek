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
import edu.illinois.finalproject.main.ProgressDialog;
import edu.illinois.finalproject.picture.Picture;

import static edu.illinois.finalproject.authentication.AuthenticationActivity.mGoogleApiClient;
import static edu.illinois.finalproject.upload.UploadActivity.PHOTOS_REF;
import static edu.illinois.finalproject.upload.UploadActivity.USER_PHOTOS_REF;

/**
 * The Profile Fragment used by the MainActivity. It controls the display of the pictures uploaded
 * by the current user by using a UserUploadsAdapter. It also has a button to log out of the app.
 */
public class ProfileFragment extends Fragment {

    private FirebaseUser user;
    private UserUploadsAdapter postsAdapter;
    private TextView numPostsView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * When Fragment is created, instantiates the user instance variable
     *
     * @param savedInstanceState passed by the Android System
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * When the View is created, sets up toolbar, instantiates the TextView, numPostsView, sets up
     * the profile image using the Glide Library, sets up Recycler view for the pictures, retrieve
     * the Picture URIs from Firebase, and finally sets up the Sign out button
     *
     * @param inflater           the LayoutInflater used to create a View from a Layout resource
     * @param container          passed by the Android System
     * @param savedInstanceState passed by the Android System
     * @return the view to display
     */
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
        postsAdapter = new UserUploadsAdapter();
        postRecyclerView.setAdapter(postsAdapter);

        // retrieve Firebase URIs. When retrieved, add the Picture object to the Adapter
        DatabaseReference uploadsRef = FirebaseDatabase.getInstance().getReference(USER_PHOTOS_REF)
                .child(user.getUid());
        uploadsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, String>> typeIndicator =
                        new GenericTypeIndicator<HashMap<String, String>>() {
                        };

                HashMap<String, String> userPhotos = dataSnapshot.getValue(typeIndicator);
                if (userPhotos != null) {
                    for (String uid : userPhotos.keySet()) {
                        addImageToAdapter(userPhotos.get(uid));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // need to implement, no functionality needed
            }
        });

        // set up sign out button
        setupSignOutButton(view);

        return view;
    }

    /**
     * Given a ImageId, retrieve the Picture Object from Firebase, then adds that Picture Object
     * to the postsAdatper.
     *
     * @param imageId the firebase generate ID of a picture
     */
    private void addImageToAdapter(String imageId) {
        DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference(PHOTOS_REF)
                .child(imageId);
        imageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Picture picture = dataSnapshot.getValue(Picture.class);

                postsAdapter.addImages(picture);
                postsAdapter.notifyDataSetChanged();

                numPostsView.setText(String.
                        format(Locale.ENGLISH, "%d", postsAdapter.getItemCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // need to implement, no functionality needed
            }
        });
    }

    /**
     * Sets up SignOut button such that when it is clicked, it logs out of Firebase and out of
     * Google.
     *
     * @param view the view to retrieve the sign out button from
     */
    private void setupSignOutButton(View view) {
        view.findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog.show(getContext(), getResources().getString(R.string.signing_out));
                // connects to google api client, then signs out of Google
                // source: https://stackoverflow.com/questions/38039320/googleapiclient-is-not-
                // connected-yet-on-logout-when-using-firebase-auth-with-g

                mGoogleApiClient.connect();
                mGoogleApiClient.registerConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(@Nullable Bundle bundle) {
                                FirebaseAuth.getInstance().signOut();

                                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                        new ResultCallback<Status>() {
                                            @Override
                                            public void onResult(@NonNull Status status) {
                                                if (status.isSuccess()) {
                                                    getActivity().finish();
                                                }
                                            }

                                        }
                                );
                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                // implementation needed but no functionality needed
                            }
                        }
                );
            }
        });
    }
}
