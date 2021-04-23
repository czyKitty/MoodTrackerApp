package com.example.moodtracker.ui.track;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.moodtracker.R;
import com.example.moodtracker.data.Fetch;
import com.example.moodtracker.data.FirebaseData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.commons.lang3.time.DateUtils;

import java.security.spec.ECField;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class TrackFragment extends Fragment {

    Button btn_line, btn_pie, btn_wordPos, btn_wordNeg;

    public View onCreateView(LayoutInflater inflater,
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
                try {
//                    Fetch firebase = new Fetch();
//                    Task<QuerySnapshot> documents = firebase.FirebaseData(DateUtils.addDays(Calendar.getInstance().getTime(), -7), new Date());
//                    for (QueryDocumentSnapshot document : documents.getResult()) {
//                        String TAG = "SUCCESS REAL";
//                        Log.d(TAG, document.getId() + " => " + document.getData());
//                        Map<String, Object> temp = document.getData();
//                    }
                    startActivity(new Intent(getActivity(), LineChartActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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