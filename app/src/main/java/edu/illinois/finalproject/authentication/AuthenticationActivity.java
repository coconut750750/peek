package edu.illinois.finalproject.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.main.MainActivity;
import edu.illinois.finalproject.main.ProgressDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * This activity is the first activity that will be loaded on the app if the user has not chosen
 * an account to use for this app. It uses Firebase's Google Sign-in method.
 * <p>
 * Source: https://www.youtube.com/watch?v=MFWZLYFD8yI
 */
public class AuthenticationActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final int SIGN_IN = 1234; // app defined result code

    public static FirebaseAuth mAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    public static GoogleApiClient mGoogleApiClient;

    /**
     * When this activity is created, it configures the Google API Client used to sign in to
     * Firebase with Google.
     *
     * @param savedInstanceState passed by the Android System
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        // Configure Google Sign in options, the object that lets the user choose which Google
        // account to use.
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Configure Firebase Authentication object and listener
        mAuth = FirebaseAuth.getInstance();

        // This listener is activated when the user successfully signed in
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent mainActivityIntent = new Intent(
                            AuthenticationActivity.this, MainActivity.class);
                    mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    ProgressDialog.hide();
                    startActivity(mainActivityIntent);
                    overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
                }
            }
        };

        SignInButton googleSignIn = (SignInButton) findViewById(R.id.google_sign_in);
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    /**
     * When this activity starts, put the Firebase Auth configurations together and connect the
     * Google API Client
     */
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        mGoogleApiClient.connect();
    }

    /**
     * When this activity stops, disable the Authentication Listener
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * This method is called when the user signs into Google. Once the user does, sign into
     * Firebase with that account.
     *
     * @param requestCode passed by the Android System, should be the same as the app define integer
     * @param resultCode  passed by the Android System
     * @param data        passed by the Android System
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();

                firebaseAuthWithGoogle(account);
            }
        }
    }

    /**
     * This method is called when the user is signed into Google. Using the Google Account, the app
     * signs into Firebase.
     *
     * @param account the Google account the user signed in with
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider
                .getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential);
    }

    private void signIn() {
        ProgressDialog.show(this, getResources().getString(R.string.signing_in));
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // need to implement, but no functionality needed
    }

    /**
     * This method enables the library used to assign fonts to text on runtime
     * Source: https://github.com/chrisjenx/Calligraphy
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
