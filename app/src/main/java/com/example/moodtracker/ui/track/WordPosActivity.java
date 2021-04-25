package com.example.moodtracker.ui.track;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.CategoryValueDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.TagCloud;
import com.anychart.scales.OrdinalColor;
import com.example.moodtracker.R;
import com.example.moodtracker.data.FirebaseData;

import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WordPosActivity extends AppCompatActivity {

    ImageButton btnBack;
    Spinner selectTime;
    AnyChartView anyChartView;
    FirebaseData fetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_layout);
        Bundle extras = getIntent().getExtras();
        ArrayList<String> keys = extras.getStringArrayList("keywords");
        System.out.println(keys);


        //define view variables
        anyChartView = findViewById(R.id.chartView);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        btnBack = (ImageButton)findViewById(R.id.btn_back);
        selectTime = (Spinner)findViewById(R.id.select_time);

        TagCloud tagCloud = AnyChart.tagCloud();

        //initialize spinner option.
        String[] items = new String[]{"Past Week", "Past Month", "Past 3 Months"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        selectTime.setAdapter(adapter);

        //back to track page
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Define title of the plot
        tagCloud.title("World Population");

        // settings
        OrdinalColor ordinalColor = OrdinalColor.instantiate();
        ordinalColor.colors(new String[]{
                "#26959f", "#f18126", "#3b8ad8", "#60727b", "#e24b26"
        });
        tagCloud.colorScale(ordinalColor);
        tagCloud.angles(new Double[]{-90d, 0d, 90d});

        tagCloud.colorRange().enabled(true);
        tagCloud.colorRange().colorLineSize(15d);

        // add data
//        ArrayList<String> keys = fetch.keywords;
        List<DataEntry> data = new ArrayList<>();
//        data.add(new CategoryValueDataEntry("China", "asia", 1383220000));
        for (String temp : keys) {
            data.add(new CategoryValueDataEntry(temp,"positive", 1));
        }


        // place data into chart
        tagCloud.data(data);

        // place chart to view
        anyChartView.setChart(tagCloud);
    }
}