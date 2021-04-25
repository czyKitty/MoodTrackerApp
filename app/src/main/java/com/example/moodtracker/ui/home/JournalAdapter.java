package com.example.moodtracker.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodtracker.R;
import com.example.moodtracker.data.Journal;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

// FirebaseRecyclerAdapter provides functions to bind, adapt and show database contents in a Recycler View
public class JournalAdapter extends FirestoreRecyclerAdapter<Journal, JournalAdapter.journalViewholder> {

    public JournalAdapter(@NonNull FirestoreRecyclerOptions<Journal> options)
    {
        super(options);
    }

    // Function to bind the view in Card view (journal.xml) with data in model class (journal.class)
    @Override
    protected void onBindViewHolder(@NonNull journalViewholder holder, int position, @NonNull Journal model)
    {
        // Add text from model class (journal.class) to appropriate view in Card view (journal.xml)
        holder.jtext.setText(model.getText());

        // Add date from model class (journal.class)to appropriate view in Card view (journal.xml)
        holder.jdate.setText(model.getDate().toString());
    }

    // Function to tell the class about the Card view (journal.xml) in which the data will be shown
    @NonNull
    @Override
    public journalViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal, parent, false);
        return new JournalAdapter.journalViewholder(view);
    }

    // Sub Class to create references of the views in Card view (journal.xml)
    class journalViewholder extends RecyclerView.ViewHolder {
        TextView jdate, jtext;
        public journalViewholder(@NonNull View itemView)
        {
            super(itemView);
            jdate = itemView.findViewById(R.id.jdate);
            jtext = itemView.findViewById(R.id.jtext);
        }
    }
}