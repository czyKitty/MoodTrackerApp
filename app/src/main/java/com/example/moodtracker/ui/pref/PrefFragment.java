package com.example.moodtracker.ui.pref;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.moodtracker.R;
import com.example.moodtracker.ui.login.GoogleSignInActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PrefFragment extends Fragment {

    Button signOutButton;
    TextView userInfo, userId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View prefView = inflater.inflate(R.layout.fragment_pref, container, false);

        // initialize view components
        userInfo = (TextView) prefView.findViewById(R.id.txtInfo);
        userId = (TextView) prefView.findViewById(R.id.txtInfo2);
        signOutButton = (Button)prefView.findViewById(R.id.signOutButton);

        // display user info
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userInfo.setText("You are log in as: "+user.getDisplayName());
        userId.setText("Your uid is: "+FirebaseAuth.getInstance().getCurrentUser().getUid());

        // sign out
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), GoogleSignInActivity.class));
            }
        });
        return prefView;
    }
}