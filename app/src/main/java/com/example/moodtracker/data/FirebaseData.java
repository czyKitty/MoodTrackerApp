package com.example.moodtracker.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.moodtracker.MainActivity;
import com.example.moodtracker.ui.track.WordPosActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class FirebaseData {

    public Future<QuerySnapshot> results;
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public QuerySnapshot documents;
    public Fetch firebase;
    public Date startTime;
    public Date endTime;
    Task<QuerySnapshot> tasks;
    public static ArrayList<String> keywords;
    private Object lock = new Object();

    public FirebaseData(Date sTime, Date eTime) {
        startTime = sTime;
        endTime = eTime;
//        try {
////            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
//            firebase = new Fetch(startTime, endTime);
//            documents = firebase.FirebaseData(startTime, endTime);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    public MainActivity activity;


//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        this.activity = activity;
//
//    }

    /**
     * Method get keywords with positive mood
     * @return set of positive keywords
     */
    public void getPosKeywords(Date sTime, Date eTime, Context activity) throws ExecutionException, InterruptedException {
        ArrayList<String> keywords = new ArrayList<String>();
        CountDownLatch done = new CountDownLatch(1);
        System.out.println("Thread Running");
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // firebase db
        Task<QuerySnapshot> snapshot = db.collection("journals")
                .whereEqualTo("uid", user.getUid())
                .whereGreaterThanOrEqualTo("date", sTime)
                .whereLessThanOrEqualTo("date", eTime)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        tasks = task;
                        if (task.isSuccessful()) {
//                            getPosKey(task);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String TAG = "SUCCESS";
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> data = document.getData();
                                ArrayList keys = (ArrayList) data.get("keywords");
                                keys.forEach((n) -> System.out.println(n));
                                Iterator iter = keys.iterator();
                                while (iter.hasNext()) {
                                    HashMap map = (HashMap) iter.next();
                                    keywords.add((String) map.get("text"));
                                }
                            }
                            System.out.println("Thread Finished");
                            Intent intent = new Intent(activity, WordPosActivity.class);
                            Bundle b = new Bundle();
                            b.putStringArrayList("keywords", keywords);
                            intent.putExtras(b);
                            activity.startActivity(intent);

                        } else {
                            String TAG = "ERROR";
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        System.out.println("Thread Finished");
    }

    /**
     * Method get keywords with negative mood
     * @return set of negative keywords
     */
//    public ArrayList<String> getNegKeywords() throws ExecutionException, InterruptedException {
//        ArrayList<String> keywords = new ArrayList<String>();
//        for (QueryDocumentSnapshot document : documents.get().getResult()) {
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

//    public ArrayList<Double> getSentimentScore() throws ExecutionException, InterruptedException {
//        ArrayList<Double> sentiments = new ArrayList<Double>();
//        for (QueryDocumentSnapshot document : documents.get().getResult()) {
//            sentiments.add((double) document.get("sentiment"));
//        }
//        return sentiments;
//    }
}
