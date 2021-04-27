package com.example.moodtracker.ui.home;

import android.text.Html;
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

    public String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    // Function to bind the view in Card view (journal.xml) with data in model class (journal.class)
    @Override
    protected void onBindViewHolder(@NonNull journalViewholder holder, int position, @NonNull Journal model)
    {
        // Add text from model class (journal.class) to appropriate view in Card view (journal.xml)
        holder.jtext.setText(model.getText());

        // Add date from model class (journal.class)to appropriate view in Card view (journal.xml)
        //holder.jdate.setText(model.getDate().toString());

        // turn date into string into array
        String lineOfDate = (model.getDate().toString().toUpperCase());
        String[] separated = lineOfDate.split(" ");
        //holder.jdate.setText(separated[2]+' '+separated[1]+' '+separated[5]);

        // Add date (number of day of the month ONLY) from model class (journal.class)to appropriate view in Card view (journal.xml)
        holder.jday.setText(separated[0]);

        // separate date parts for ui
        String month_yr = separated[1]+' '+separated[5];
        String day_num = getColoredSpanned(separated[2], "#29BDC0");
        String date_rest = getColoredSpanned(month_yr, "#000000");

        // Add date (month and year) from model class (journal.class)to appropriate view in Card view (journal.xml)
        holder.jdate.setText(Html.fromHtml(day_num+" "+date_rest));

        //get rid of the seconds part of the time
        String[] separated2 = separated[3].split(":");
        holder.jtime.setText(Html.fromHtml(separated2[0]+':'+separated2[1]));

//        //convert 24hr to 12 hour for ui
//        //return converted time and am/pm
//        int time = Integer.parseInt(separated2[0]);
//        String converted = "";
//        String[] = ["",""];
//        String AM = "AM";
//        String PM = "PM";
//        String ampm = "";
//        public String[] convertTime(time) {
//            String converted = "";
//            if (time == 0) {
//                converted = "12";
//
//
//            }
//            else if time == 1
//    }

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
        TextView jdate, jtext, jday, jtime;
        public journalViewholder(@NonNull View itemView)
        {
            super(itemView);
            jdate = itemView.findViewById(R.id.jdate);
            jtext = itemView.findViewById(R.id.jtext);
            jday = itemView.findViewById(R.id.jday);
            jtime = itemView.findViewById(R.id.jtime);

        }
    }
}