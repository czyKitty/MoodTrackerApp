package com.example.moodtracker.ui.track;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.example.moodtracker.R;
import com.example.moodtracker.data.FirebaseData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LineChartActivity extends AppCompatActivity {

    ImageButton btnBack;
    Spinner selectTime;
    AnyChartView anyChartView;
    HashMap<String, Double> scores = new HashMap<String, Double>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_layout);
        Bundle extras = getIntent().getExtras();
        if (!scores.isEmpty()){
            scores = (HashMap<String, Double>) extras.getSerializable("sentimentScoreTable");
            System.out.println("Score is not null: "+ scores);
        }
        else{
            scores.put("2021-4-25", 0.85);
            scores.put("2021-4-24", 0.5);
            System.out.println("Score is null: "+ scores);

        }



        //define view variables
        anyChartView = findViewById(R.id.chartView);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        btnBack = (ImageButton)findViewById(R.id.btn_back);
        selectTime = (Spinner)findViewById(R.id.select_time);


        //initialize spinner option.
        String[] items = new String[]{"Past Week", "Past Month", "Past 3 Months"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        selectTime.setAdapter(adapter);

        drawPlot(scores);


        //spinner change listener
//        selectTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
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
//                    fetch.getSentimentScore(startDate, endDate, getApplicationContext());
//                } catch (ExecutionException | InterruptedException e) {
//                    e.printStackTrace();
//                }            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//            }
//
//        });

        //back to track page
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * DrawPlot based on selected timeFrame
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void drawPlot(HashMap<String, Double> scores){

        // add data
        List<DataEntry> seriesData = new ArrayList<>();

        scores.forEach((k, v) -> {
            seriesData.add(new ValueDataEntry(String.valueOf(k), v));

        });

        // define cartesian coord
        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        // define position mod
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        // set x and y labels
        cartesian.yAxis(0).title("Mood level");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);


        // place data in set
        Set set = Set.instantiate();
        set.data(seriesData);

        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

        // add series to chart
        Line series1 = cartesian.line(series1Mapping);
        series1.name("Mood trend");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);


        // define legend
        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
    }
}

