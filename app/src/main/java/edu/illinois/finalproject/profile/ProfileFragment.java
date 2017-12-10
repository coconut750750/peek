package edu.illinois.finalproject.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.authentication.AuthenticationActivity;
import edu.illinois.finalproject.main.ProgressDialog;

import static edu.illinois.finalproject.authentication.AuthenticationActivity.mGoogleApiClient;

/**
 *
 */
public class ProfileFragment extends Fragment {

    private FirebaseUser user;

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

        // set up profile image
        ImageView profileImage = (ImageView) view.findViewById(R.id.profile_image);
        new ProfileImageAsync(profileImage).execute(user.getPhotoUrl());

        // set up recycler view of pics
        RecyclerView postRecyclerView = (RecyclerView) view.findViewById(R.id.posts_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        postRecyclerView.setLayoutManager(linearLayoutManager);

        ProfilePictureAdapter pictureAdapter = new ProfilePictureAdapter(null);
        postRecyclerView.setAdapter(pictureAdapter);

        // set up sign out button
        setupSignOutButton(view);

        return view;
    }

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
