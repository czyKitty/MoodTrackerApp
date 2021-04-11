package com.example.moodtracker.ui.track;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.CategoryValueDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.TagCloud;
import com.anychart.scales.OrdinalColor;
import com.example.moodtracker.R;

import java.util.ArrayList;
import java.util.List;

public class WordPosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_layout);

        AnyChartView anyChartView = findViewById(R.id.chartView);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        TagCloud tagCloud = AnyChart.tagCloud();

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
        List<DataEntry> data = new ArrayList<>();
        data.add(new CategoryValueDataEntry("China", "asia", 1383220000));

        // place data into chart
        tagCloud.data(data);

        // place chart to view
        anyChartView.setChart(tagCloud);
    }
}