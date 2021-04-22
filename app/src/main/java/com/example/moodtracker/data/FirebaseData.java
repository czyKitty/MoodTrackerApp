package com.example.moodtracker.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirebaseData {

    public FirebaseFirestore db;
    CollectionReference collectionRef;
    ArrayList<Journal> journals = new ArrayList<Journal>();

    List<QueryDocumentSnapshot> documents;

    public FirebaseData(String uid, Date startTime) {
        db = FirebaseFirestore.getInstance(); // firebase db
        //String userID = db.collection("users").getId();
        // find collection for uid
        collectionRef = db.collection(uid);
        ApiFuture<QuerySnapshot> future = db.collection(collectionRef).get();
        documents = future.get().getDocuments();
    }

    /**
     * Method get keywords with positive mood
     * @return set of positive keywords
     */
    public ArrayList<String> getPosKeywords(){
        ArrayList<String> keywords = new ArrayList<String>();
        for (QueryDocumentSnapshot document : documents) {
            if((Double) document.get("sentiment") > 0) {
                keywords.add((String) document.get("keyword"));
            }
        }
        return keywords;
    }

    /**
     * Method get keywords with negative mood
     * @return set of negative keywords
     */
    public ArrayList<String> getNegKeywords(){
        ArrayList<String> keywords = new ArrayList<String>();
        for (QueryDocumentSnapshot document : documents) {
            if((Double) document.get("sentiment") < 0) {
                keywords.add((String) document.get("keyword"));
            }
        }
        return keywords;
    }

    /**
     * Method get tones
     * @return
     */
    public String getTones(){
        ArrayList<String> tones = new ArrayList<String>();
        for (QueryDocumentSnapshot document : documents) {
            tones.add((double) document.get("tone"));
        }
        return tones;
    }

    public ArrayList<Double> getSentimentScore() {
        ArrayList<Double> sentiments = new ArrayList<Double>();
        for (QueryDocumentSnapshot document : documents) {
            sentiments.add((double) document.get("sentiment"));
        }
        return sentiments;
    }
}
