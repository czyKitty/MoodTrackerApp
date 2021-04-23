package com.example.moodtracker.ui.talk;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moodtracker.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;

import com.ibm.watson.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.natural_language_understanding.v1.model.Features;
import com.ibm.watson.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.natural_language_understanding.v1.model.SentimentOptions;
import com.ibm.watson.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.tone_analyzer.v3.model.ToneOptions;

import com.example.moodtracker.data.Journal;
import com.example.moodtracker.data.Journal.Analysis_nlp;
import java.util.concurrent.atomic.AtomicReference;


public class TalkFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance(); // firebase db
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private TalkViewModel dashboardViewModel;
    Button btnSubmit;
    EditText txtJournal;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(TalkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_talk, container, false);
        //Test
//        try {
//            newEntry("Today went by really fast. I was outdoors most of the day.");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        btnSubmit = (Button) root.findViewById(R.id.submitEntry);
        txtJournal = (EditText) root.findViewById(R.id.txt_talk);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    newEntry(txtJournal.getText().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void newEntry(String input) throws InterruptedException {
        Analysis_nlp out = sentiment(input);
        Journal j = new Journal(input, user, out);
        try {
        db.collection("journals").add(j).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            private static final String TAG = "SUCCESS";
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot Added with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            private static final String TAG = "ERROR";
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        }); } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Analysis_nlp sentiment(String input) throws InterruptedException {
        AtomicReference<ToneAnalysis> tone = new AtomicReference<>(new ToneAnalysis());
        AtomicReference<AnalysisResults> keys = new AtomicReference<>(new AnalysisResults());
        AtomicReference<AnalysisResults> score= new AtomicReference<>(new AnalysisResults());
        //        Authenticate the API
        Authenticator authenticator = new IamAuthenticator("ZaupUklyab6ixv-73XYreQZLWOAfYv85DnjxUyklz4ry");
        ToneAnalyzer service = new ToneAnalyzer("2017-09-21", authenticator);
        service.setServiceUrl("https://api.us-east.tone-analyzer.watson.cloud.ibm.com/instances/c2f34cbf-a90a-4785-88d8-a41c0a08ac8c");

        Authenticator authenticator_nlp = new IamAuthenticator("SO_Deq8xw8V3uT1inqIAqzyMi2c7ku4OBhcwg97Nb5f6");
        NaturalLanguageUnderstanding service_nlp = new NaturalLanguageUnderstanding("2019-07-12", authenticator_nlp);
        service_nlp.setServiceUrl("https://api.us-east.natural-language-understanding.watson.cloud.ibm.com/instances/4dd4583a-125a-47b2-bec8-4bf171ffca01");

//        Run Network API Call on separate thread
//        Use separate thread so to not block the UI thread
        Thread thread = new Thread(){
            public void run(){
                System.out.println("Thread Running");
                try {
                    // Tone Analysis
                    ToneOptions toneOptions = new ToneOptions.Builder()
                            .text(input)
                            .build();
                    tone.set(service.tone(toneOptions).execute().getResult());

                    // Keyword Analysis
                    KeywordsOptions keywords = new KeywordsOptions.Builder()
                            .sentiment(true)
                            .emotion(true)
                            .limit(3)
                            .build();
                    Features features = new Features.Builder()
                            .keywords(keywords)
                            .build();
                    AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                            .text(input)
                            .features(features)
                            .build();
                    keys.set(service_nlp.analyze(parameters).execute().getResult());

                    // General Sentiment Analysis
                    SentimentOptions sentiment = new SentimentOptions.Builder()
                            .document(true)
                            .build();
                    Features features_2 = new Features.Builder()
                            .sentiment(sentiment)
                            .build();
                    AnalyzeOptions parameters_2 = new AnalyzeOptions.Builder()
                            .text(input)
                            .features(features_2)
                            .language("en")
                            .build();
                    AnalysisResults response = service_nlp
                            .analyze(parameters_2)
                            .execute()
                            .getResult();
                    score.set(response);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        thread.join();
        return new Analysis_nlp(tone.get(), keys.get(), score.get());
    }

}