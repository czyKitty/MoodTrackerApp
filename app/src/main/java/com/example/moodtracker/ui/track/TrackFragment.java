package com.example.moodtracker.ui.track;

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
import com.example.moodtracker.ui.talk.TalkViewModel;

public class TrackFragment extends Fragment {

    private TrackViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(TrackViewModel.class);
        View root = inflater.inflate(R.layout.fragment_track, container, false);
        return root;
    }
}