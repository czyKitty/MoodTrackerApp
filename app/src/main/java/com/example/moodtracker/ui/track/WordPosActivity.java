package com.example.moodtracker.ui.track;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.CategoryValueDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.TagCloud;
import com.anychart.scales.OrdinalColor;

public class WordPosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.chart_layout);

        //AnyChartView anyChartView = findViewById(R.id.chartView);
        //anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        TagCloud tagCloud = AnyChart.tagCloud();
    }
}
