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
import com.example.moodtracker.ui.home.HomeFragment;
import com.example.moodtracker.ui.track.LineChartActivity;
import com.example.moodtracker.ui.track.PieChartActivity;
import com.example.moodtracker.ui.track.WordNegActivity;
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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
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

    }

    /**
     * get original journal text
     * @param sTime
     * @param eTime
     * @param activity
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void getText(Date sTime, Date eTime, Context activity) throws ExecutionException, InterruptedException {
        ArrayList<String> texts = new ArrayList<String>();
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
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String TAG = "SUCCESS";
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> data = document.getData();
                                ArrayList texts = (ArrayList) data.get("text");
                                texts.forEach((n) -> System.out.println(n));
                                Iterator iter = texts.iterator();
                                while (iter.hasNext()) {
                                    HashMap map = (HashMap) iter.next();
                                    // if statement here
                                    texts.add(map.toString());
                                }
                            }
                            System.out.println("Thread Finished");
                            Intent intent = new Intent(activity, HomeFragment.class);
                            Bundle b = new Bundle();
                            b.putStringArrayList("texts", texts);
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
     * get Date of journal from firebase
     * @param sTime
     * @param eTime
     * @param activity
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void getDate(Date sTime, Date eTime, Context activity) throws ExecutionException, InterruptedException {
        ArrayList<String> date = new ArrayList<String>();
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
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String TAG = "SUCCESS";
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> data = document.getData();
                                ArrayList dates = (ArrayList) data.get("date");
                                dates.forEach((n) -> System.out.println(n));
                                Iterator iter = dates.iterator();
                                while (iter.hasNext()) {
                                    HashMap map = (HashMap) iter.next();
                                    // if statement here
                                    date.add(map.toString());
                                }
                            }
                            System.out.println("Thread Finished");
                            Intent intent = new Intent(activity, HomeFragment.class);
                            Bundle b = new Bundle();
                            b.putStringArrayList("dates", date);
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
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String TAG = "SUCCESS";
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> data = document.getData();
                                ArrayList keys = (ArrayList) data.get("keywords");
                                keys.forEach((n) -> System.out.println(n));
                                Iterator iter = keys.iterator();
                                while (iter.hasNext()) {
                                    HashMap map = (HashMap) iter.next();
                                    // if statement here
                                    HashMap sentimentMap = (HashMap) map.get("sentiment");
                                    Double score = new Double ((sentimentMap.get("score")).toString());
                                    if (score > 0) {
                                        keywords.add((String) map.get("text"));
                                    }
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
     */
    public void getNegKeywords(Date sTime, Date eTime, Context activity) throws ExecutionException, InterruptedException {
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
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String TAG = "SUCCESS";
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> data = document.getData();
                                ArrayList keys = (ArrayList) data.get("keywords");
                                keys.forEach((n) -> System.out.println(n));
                                Iterator iter = keys.iterator();
                                while (iter.hasNext()) {
                                    HashMap map = (HashMap) iter.next();
                                    HashMap sentimentMap = (HashMap) map.get("sentiment");
                                    Double score = new Double ((sentimentMap.get("score")).toString());
                                    if (score< 0 ){
                                        // if statement here
                                        keywords.add((String) map.get("text"));
                                    }
                                }
                            }
                            System.out.println("Thread Finished");
                            Intent intent = new Intent(activity, WordNegActivity.class);
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
     * Method get tones from firebase
     * @return
     */
    public void getTones(Date sTime, Date eTime, Context activity) throws ExecutionException, InterruptedException {
        ArrayList<String> tones = new ArrayList<String>();
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
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String TAG = "SUCCESS";
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> data = document.getData();
                                HashMap toneMap = (HashMap) data.get("tones");
                                ArrayList tone = (ArrayList) toneMap.get("tones");
                                Iterator iter = tone.iterator();
                                while (iter.hasNext()) {
                                    HashMap map = (HashMap) iter.next();
                                    tones.add((String) map.get("toneName"));
                                }
                            }
                            System.out.println("Thread Finished");
                            Intent intent = new Intent(activity, PieChartActivity.class);
                            Bundle b = new Bundle();
                            b.putStringArrayList("tones", tones);
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
     * get sentimentScore from firebase
     * @param sTime
     * @param eTime
     * @param activity
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void getSentimentScore(Date sTime, Date eTime, Context activity) throws ExecutionException, InterruptedException {
        HashMap<String, Double> sentimentScores = new HashMap<String, Double>();
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
//                            getSentimentScore(task);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String TAG = "SENTIMENT - SUCCESS";
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> data = document.getData();
                                String currentDate = (String) data.get("date").toString();
                                HashMap sentimentMap = (HashMap) data.get("sentiment");
                                Collection <String> values = sentimentMap.values();

                                ArrayList sentiment = new ArrayList<String>(values);
                                sentiment = new ArrayList<String>(sentiment.subList(0,(sentiment.size()-1)));
                                sentiment.forEach((n) -> System.out.println("sentiment map is "+n));

                                HashMap sentimentPair = (HashMap) sentiment.get(0);

                                sentimentScores.put(currentDate, (Double) sentimentPair.get("score"));
                                System.out.println("the sentiment score hashmap is: "+ sentimentScores);
                            }
                            System.out.println("Thread Finished");
                            Intent intent = new Intent(activity, LineChartActivity.class);
                            Bundle b = new Bundle();
                            b.putSerializable("sentimentScoreTable", sentimentScores);
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
}
