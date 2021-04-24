package com.example.moodtracker.data;

import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;

import java.util.concurrent.Callable;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Fetch {

    public FirebaseFirestore db = FirebaseFirestore.getInstance(); // firebase db
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public Task<QuerySnapshot> documents;
    private final Date start;
    private final Date end;

    public Fetch(Date s, Date e){
        start=s; end=e;

    }


    public QuerySnapshot FirebaseData(Date start, Date end) throws InterruptedException {
        QuerySnapshot[] result = new QuerySnapshot[1];
        CountDownLatch done = new CountDownLatch(1);
        Thread th = new Thread() {
            public void run() {
                System.out.println("Thread Running");
                db = FirebaseFirestore.getInstance(); // firebase db
                Task<QuerySnapshot> snapshot = db.collection("journals")
                        .whereEqualTo("uid", user.getUid())
                        .whereGreaterThanOrEqualTo("date", start)
                        .whereLessThanOrEqualTo("date", end)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String TAG = "SUCCESS";
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                    }
                                    result[0] = task.getResult();
                                    System.out.println("Thread Finished");
                                    done.countDown();
                                } else {
                                    String TAG = "ERROR";
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });


            }
        };
        th.start();
        th.join();
        try {
            done.await(); //it will wait till the response is received from firebase.
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        return result[0];

    };



}
