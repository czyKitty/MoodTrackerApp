package com.example.moodtracker.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ibm.watson.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;

import java.util.Date;

public class FirebaseData {

    private static final String TAG = "fetching db data";

    public String uid;
    public Date date;
    public String text;
    public AnalysisResults sentiment;
    public AnalysisResults keywords;
    public ToneAnalysis tones;


    public FirebaseFirestore db;
    public CollectionReference collectionRef;

    public FirebaseData() {
        db = FirebaseFirestore.getInstance(); // firebase db
        collectionRef = db.collection("journals");

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentFirebaseUser.getUid();
        Log.d(TAG, String.valueOf(collectionRef));

    }


    public String getSentimentScore(){
        final String[] result = new String[1];
        collectionRef.whereEqualTo("uid", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + "=>" + document.getString("sentiment"));
                        result[0] = document.getString("sentiment");

                    }
                }
            }
        });

        return result[0];
    }

    public String[] getKeywords(){

        final String[] result = new String[1];
        collectionRef.whereEqualTo("uid", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + "=>" + document.getString("keywords"));
                        result[0] = document.getString("keywords");
                    }
                }
            }
        });
        return result;
    }

    public String[] getTones(){
        final String[] result = new String[1];
        collectionRef.whereEqualTo("uid", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + "=>" + document.getString("tones"));
                        result[0] = document.getString("tones");
                    }
                }
            }
        });
        return result;

    }
//    /**
//     * Method get keywords with positive mood
//     * @return set of positive keywords
//     */
//    public ArrayList<String> getPosKeywords(){
//        ArrayList<String> keywords = new ArrayList<String>();
//        for (QueryDocumentSnapshot document : documents) {
//            if((Double) document.get("sentiment") > 0) {
//                keywords.add((String) document.get("keyword"));
//            }
//        }
//        return keywords;
//    }

    /**
     * Method get keywords with negative mood
     * @return set of negative keywords
     */
//    public ArrayList<String> getNegKeywords(){
//        ArrayList<String> keywords = new ArrayList<String>();
//        for (QueryDocumentSnapshot document : documents) {
//            if((Double) document.get("sentiment") < 0) {
//                keywords.add((String) document.get("keyword"));
//            }
//        }
//        return keywords;
//    }

    /**
     * Method get tones
     * @return
     */
//    public String getTones(){
//        ArrayList<String> tones = new ArrayList<String>();
//        for (QueryDocumentSnapshot document : documents) {
//            tones.add((double) document.get("tone"));
//        }
//        return tones;
//    }

//    public ArrayList<Double> getSentimentScore() {
//        ArrayList<Double> sentiments = new ArrayList<Double>();
//        for (QueryDocumentSnapshot document : documents) {
//            sentiments.add((double) document.get("sentiment"));
//        }
//        return sentiments;
//    }
}
