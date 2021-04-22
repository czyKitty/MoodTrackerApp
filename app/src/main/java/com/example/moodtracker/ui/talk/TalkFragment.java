package com.example.moodtracker.ui.talk;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moodtracker.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.example.moodtracker.data.Journal;

import static android.content.Context.MODE_PRIVATE;

public class TalkFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance(); // firebase db

    private String input;
    Button btnTalk, btnSubmit;
    EditText txtJournal;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View talkView = inflater.inflate(R.layout.fragment_talk, container, false);
        // define view component
        // btnTalk = (Button) talkView.findViewById(R.id.btnTalk);
        btnSubmit = (Button) talkView.findViewById(R.id.submitEntry);
        txtJournal = (EditText) talkView.findViewById(R.id.txt_talk);

        // when click talk button, use google speech to text

        // when click submit button, add entry to firebase
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get uid from preference
                SharedPreferences sh = getActivity().getSharedPreferences("AUTHENTICATION_FILE_NAME", MODE_PRIVATE);
                String uid = sh.getString("UID","");

                //storeJournal("123", txtJournal.getText().toString());
                storeJournal(uid,"Today was an amazing day for me. I hope tomorrow is good also.");
                Toast toast=Toast.makeText(getActivity().getApplicationContext(),"SUCCESSFULLY ADDED JOURNALS",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        return talkView;
    }

    /**
     * @param uid: unique id for user
     * @param input: journal entry
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void storeJournal(String uid, String input) {
        try {
            // add new document to collection "uid"
            db.collection(uid).document(Calendar.getInstance().getTime()).add(createEntry(input)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param input: single journal text
     * @return journal entry to store in firebase
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Map<String, Object> createEntry(String input){
        Map<String, Object> journal = new HashMap<>();
        Journal j = new Journal(input);
        journal.put("journal", j); // original journal text
        journal.put("sentiment", j.getSentiment()); // sentiment score of text
        journal.put("keyword", j.getKeyword()); // keyword of the text
        journal.put("tone", j.getTone()); // tone of the text

        return journal;
    }
}