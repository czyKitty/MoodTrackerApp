package com.example.moodtracker.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirebaseData {

    public FirebaseFirestore db = FirebaseFirestore.getInstance(); // firebase db
    public String userID = db.collection("users").getId();

    Map<String, Object> journal1 = new HashMap<>();

    public CollectionReference getColRef() {
        CollectionReference collectionRef = db.collection("journals");
        return collectionRef;
    }

    public String getKeywords(){
        CollectionReference collectionRef = getColRef();
        String TAG = "GET KEYWORDS";

        final String[] result = {""};
        collectionRef.whereEqualTo("keyWords", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + "=>" + document.get("keyWords"));
                        result[0] = (String) document.get("sentimentScore");
                    }
                } else {
                    Log.d(TAG, "Error getting documents", task.getException());
                }
            }
        });
        return result[0];
    }
    public String getDate(){
        CollectionReference collectionRef = getColRef();
        String TAG = "GET DATE";

        final String[] result = {""};
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + "=>" + document.get("date"));
                        result[0] = (String) document.get("date");
                    }
                } else {
                    Log.d(TAG, "Error getting documents", task.getException());
                }
            }
        });
        return result[0];
    }


    public String getTones(){

        CollectionReference collectionRef = getColRef();
        String TAG = "GET TONES";

        final String[] result = {""};
        collectionRef.whereEqualTo("tones", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + "=>" + document.get("tones"));
                        result[0] = (String) document.get("tones");
                    }
                } else {
                    Log.d(TAG, "Error getting documents", task.getException());
                }
            }
        });
        return result[0];
    }

    public double[] getSentimentScore(){
        CollectionReference collectionRef = getColRef();
        String TAG = "GET SENTIMENT SCORE";
        final double[] result = {0};

        collectionRef.whereEqualTo("sentiment", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + "=>" + document.get("sentiment"));
                        result[0] = (double) document.get("sentiment");
                    }
                } else {
                    Log.d(TAG, "Error getting documents", task.getException());
                }
            }
        });

        return result;
    }
}
