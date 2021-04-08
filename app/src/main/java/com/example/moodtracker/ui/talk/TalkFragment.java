package com.example.moodtracker.ui.talk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.moodtracker.R;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;

import com.ibm.watson.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.tone_analyzer.v3.model.ToneOptions;

import java.util.concurrent.atomic.AtomicBoolean;


public class TalkFragment extends Fragment {

    private TalkViewModel dashboardViewModel;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private String input;
    private String sentiment;
    private Runnable mytask;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(TalkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_talk, container, false);
//        input = edtInput.getText().toString();
//        running.set(true);
//        new Thread(mytask).start();
        return root;
    }
    public void sentiment(String input) {
        //        Authenticate the API
        Authenticator authenticator = new IamAuthenticator("ZaupUklyab6ixv-73XYreQZLWOAfYv85DnjxUyklz4ry");
        ToneAnalyzer service = new ToneAnalyzer("2017-09-21", authenticator);
        service.setServiceUrl("https://api.us-east.tone-analyzer.watson.cloud.ibm.com/instances/c2f34cbf-a90a-4785-88d8-a41c0a08ac8c");

//        Run Network API Call on seperate thread
//        Use seperate thread so to not block the UI thread
        mytask = () -> {
            while (running.get()){
                try  {
//        Build API Request
                    ToneOptions toneOptions = new ToneOptions.Builder()
                            .text(input)
                            .build();
                    ToneAnalysis tone = service.tone(toneOptions).execute().getResult();
                    sentiment = tone.toString();
                    running.set(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }};
    }
}