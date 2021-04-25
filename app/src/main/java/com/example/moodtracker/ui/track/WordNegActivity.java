package com.example.moodtracker.ui.track;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.CategoryValueDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.TagCloud;
import com.anychart.scales.OrdinalColor;
import com.example.moodtracker.R;
import com.example.moodtracker.data.FirebaseData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class WordNegActivity extends AppCompatActivity {

    ImageButton btnBack;
    Spinner selectTime;
    AnyChartView anyChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_layout);

        //define view variables
        anyChartView = findViewById(R.id.chartView);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        btnBack = (ImageButton)findViewById(R.id.btn_back);
        selectTime = (Spinner)findViewById(R.id.select_time);

        //initialize spinner option.
        String[] items = new String[]{"Past Week", "Past Month", "Past 3 Months"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        selectTime.setAdapter(adapter);

        drawPlot(selectTime.getSelectedItem().toString());

        //spinner change listener
        selectTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                drawPlot(selectTime.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        //back to track page
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void drawPlot(String timeFrame){
        Calendar cal = Calendar.getInstance();
        Date endDate = cal.getTime();
        Date startDate;
        if(timeFrame.equals("Past Week")){
            cal.add(Calendar.DAY_OF_YEAR, -7);
            startDate = cal.getTime();
        }else if (timeFrame.equals("Past Month")){
            cal.add(Calendar.MONTH, -1);
            startDate = cal.getTime();
        }else{
            cal.add(Calendar.MONTH, -3);
            startDate = cal.getTime();
        }

        FirebaseData data = new FirebaseData(startDate, endDate);
        // initialize data
        List<DataEntry> seriesData = new ArrayList<>();
        try {
            ArrayList<String> keys = data.getData(startDate, endDate, "key");
            // get each tones and number of appearance
            for(String key: keys){
                seriesData.add(new CategoryValueDataEntry(key, "negative", Collections.frequency(keys, key)));
            }
        } catch (Exception e) {
            Log.d("ERROR", "Extract Data Failed ");
        }

        // Define title of the plot
        TagCloud tagCloud = AnyChart.tagCloud();
        tagCloud.title("Key Terms for negative Mood");

        // settings
        OrdinalColor ordinalColor = OrdinalColor.instantiate();
        ordinalColor.colors(new String[]{
                "#26959f", "#f18126", "#3b8ad8", "#60727b", "#e24b26"
        });
        tagCloud.colorScale(ordinalColor);
        tagCloud.angles(new Double[]{-90d, 0d, 90d});

        tagCloud.colorRange().enabled(true);
        tagCloud.colorRange().colorLineSize(15d);

        // place data into chart
        tagCloud.data(seriesData);

        // place chart to view
        anyChartView.setChart(tagCloud);
    }
}
