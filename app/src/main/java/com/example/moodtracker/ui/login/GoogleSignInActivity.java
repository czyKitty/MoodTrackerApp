package com.example.moodtracker.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moodtracker.MainActivity;
import com.example.moodtracker.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class GoogleSignInActivity extends AppCompatActivity {

    SignInButton signInButton;
    //Button signOutButton;
    TextView statusTextView;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_ui);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        CharSequence text = "Ready to sign in! ";
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        statusTextView = (TextView) findViewById(R.id.statusTextView);
        statusTextView.setText("ready to sign in ");

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, text, duration).show();
                Log.d(TAG, "i am Logging in");
                signIn();
            }
        });
        //signOutButton = (Button) findViewById(R.id.signOutButton);
        /*
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "i am Logging out");
                signOut();
            }
        });
        */

        mAuth = FirebaseAuth.getInstance();

    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
                startActivity(new Intent(this, MainActivity.class));
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }

    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    // [END auth_with_google]

    private void handleSignInResult(GoogleSignInResult result){
        Log.d(TAG, "handleSignInResult: "+ result.isSuccess());
        if (result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            statusTextView.setText("hello, " + acct.getDisplayName());
        }
    }
    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d(TAG, "signing in!!");
    }
    // [END signin]

    private void updateUI(FirebaseUser user) {

    }

    private void signOut(){
        statusTextView.setText("Signed out");
        FirebaseAuth.getInstance().signOut();
    }


}