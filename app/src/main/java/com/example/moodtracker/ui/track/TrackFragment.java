package com.example.moodtracker.ui.track;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moodtracker.R;

public class TrackFragment extends Fragment {

    Button btn_line, btn_pie, btn_wordPos, btn_wordNeg;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // define view
        View trackView = inflater.inflate(R.layout.fragment_track, container, false);
        // initialization
        btn_line = (Button)trackView.findViewById(R.id.btn_line);
        btn_pie = (Button)trackView.findViewById(R.id.btn_pie);
        btn_wordPos = (Button)trackView.findViewById(R.id.btn_wordPos);
        btn_wordNeg = (Button)trackView.findViewById(R.id.btn_wordNeg);

        // button listener
        btn_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LineChartActivity.class));
            }
        });

        btn_pie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PieChartActivity.class));
            }
        });

        btn_wordPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WordPosActivity.class));
            }
        });

        btn_wordNeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WordNegActivity.class));
            }
        });

        return trackView;
    }
}