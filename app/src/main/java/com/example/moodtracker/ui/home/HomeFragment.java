package com.example.moodtracker.ui.home;

//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.example.moodtracker.R;
//
//public class HomeFragment extends Fragment {
//
//    private HomeViewModel homeViewModel;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        return root;
//    }
//
//
//}

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodtracker.R;
import com.example.moodtracker.data.Journal;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    JournalAdapter adapter; // Create Object of the Adapter class
    //DatabaseReference mbase; // Create object of the Firebase Realtime Database
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        //setContentView(R.layout.activity_main);
//
//        // Create a instance of the database and get its reference
//        mbase = FirebaseDatabase.getInstance().getReference();
//
//        recyclerView = this.getView().findViewById(R.id.recycler1);
//
//        // To display the Recycler view linearly
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        // Class provided by the FirebaseUI to make a query in the database to fetch appropriate data
//        FirebaseRecyclerOptions<Journal> options
//                = new FirebaseRecyclerOptions.Builder<Journal>()
//                .setQuery(mbase, Journal.class)
//                .build();
//        // Connecting object of required Adapter class to the Adapter class itself
//        adapter = new JournalAdapter(options);
//        // Connecting Adapter class with the Recycler view*/
//        recyclerView.setAdapter(adapter);
//    }


    CollectionReference collectionReference;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create a instance of the database and get its reference
        //mbase = FirebaseDatabase.getInstance().getReference().child("journals");
        collectionReference = FirebaseFirestore.getInstance().collection("journals");

        recyclerView = this.getView().findViewById(R.id.recycler1);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



//        Log.wtf("DATA ::", "Expected ");
//        mbase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot ref : snapshot.getChildren()) {
//                    Log.wtf("REF:::::", ref.getValue().toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                error.toException().printStackTrace();
//            }
//        });
//        mbase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if(task.isSuccessful()){
//                    for (DataSnapshot ref : task.getResult().getChildren()) {
//                        Log.wtf("REF:::::", ref.getValue().toString());
//                    }
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                e.printStackTrace();
//            }
//        });

        // Class provided by the FirebaseUI to make a query in the database to fetch appropriate data



        FirestoreRecyclerOptions<Journal> options
                = new FirestoreRecyclerOptions.Builder<Journal>()
                .setQuery(collectionReference.whereEqualTo("uid", user.getUid()), Journal.class)
                .build();
        // Connecting object of required Adapter class to the Adapter class itself
//
//        Calendar cal = Calendar.getInstance();
//        cal.set(2021, 04, 23);
//        Date sDate = cal.getTime();
//        cal.set(2021, 04, 25);
//
//
//        try {
//            new FirebaseData(null, null).getDate(sDate, cal.getTime(), getContext());
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        adapter = new JournalAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);
    }

    // Function to tell the app to start getting data from database on starting of the activity
    @Override public void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting data from database on stopping of the activity
    @Override public void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}