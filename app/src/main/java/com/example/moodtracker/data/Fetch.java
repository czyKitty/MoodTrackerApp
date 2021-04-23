package com.example.moodtracker.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class Fetch {

    public FirebaseFirestore db = FirebaseFirestore.getInstance(); // firebase db
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public Task<QuerySnapshot> docs;

    public Task<QuerySnapshot> FirebaseData(Date start, Date end) {
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
                        } else {
                            String TAG = "ERROR";
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    return snapshot;
    }

}
