package com.example.moodtracker.ui.track;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.chart.common.dataentry.CategoryValueDataEntry;
import com.example.moodtracker.data.FirebaseData;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.example.moodtracker.R;

import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PieChartActivity extends AppCompatActivity {

    ImageButton btnBack;
    Spinner selectTime;
    AnyChartView anyChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_layout);
        Bundle extras = getIntent().getExtras();
        ArrayList<String> tones = extras.getStringArrayList("tones");

        //define view variables
        anyChartView = findViewById(R.id.chartView);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        btnBack = (ImageButton)findViewById(R.id.btn_back);
        selectTime = (Spinner)findViewById(R.id.select_time);

        //initialize spinner option.
        String[] items = new String[]{"Past Week", "Past Month", "Past 3 Months"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        selectTime.setAdapter(adapter);

        drawPlot(tones);

        //spinner change listener
//        selectTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                String timeFrame = selectTime.getSelectedItem().toString();
//                Calendar cal = Calendar.getInstance();
//                Date endDate = cal.getTime();
//                Date startDate;
//
//                if(timeFrame.equals("Past Week")){
//                    cal.add(Calendar.DAY_OF_YEAR, -7);
//                    startDate = cal.getTime();
//                }else if (timeFrame.equals("Past Month")){
//                    cal.add(Calendar.MONTH, -1);
//                    startDate = cal.getTime();
//                }else{
//                    cal.add(Calendar.MONTH, -3);
//                    startDate = cal.getTime();
//                }
//
//                FirebaseData fetch = new FirebaseData(startDate, endDate);
//
//                try {
//                    fetch.getNegKeywords(startDate, endDate, getApplicationContext());
//                } catch (ExecutionException | InterruptedException e) {
//                    e.printStackTrace();
//                }            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//            }
//        });

        //back to track page
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }

    protected void drawPlot(ArrayList<String> tones) {
        // initialize data
        List<DataEntry> seriesData = new ArrayList<>();
        for(String tone: tones){
            seriesData.add(new ValueDataEntry(tone, Collections.frequency(tones, tone)));
        }

        Pie pie = AnyChart.pie();

        // click display
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(PieChartActivity.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        // set data
        pie.data(seriesData);

        // place to view
        anyChartView.setChart(pie);
    }

}