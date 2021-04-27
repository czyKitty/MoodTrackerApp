package com.example.moodtracker.ui.track;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
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
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class WordNegActivity extends AppCompatActivity {

    ImageButton btnBack;
    AnyChartView anyChartView;

    DatePickerDialog start;
    DatePickerDialog end;

    EditText edtStart;
    EditText edtEnd;
    Button btnDate;
    Date startDate = new Date();
    Date endDate = new Date();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_layout);

        //define view variables
        anyChartView = findViewById(R.id.chartView);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));
        btnBack = (ImageButton)findViewById(R.id.btn_back);
        Bundle extras = getIntent().getExtras();
        ArrayList<String> keys = extras.getStringArrayList("keywords");

        //initialize datepicker option.
        edtStart=(EditText) findViewById(R.id.edtDate);
        edtEnd=(EditText) findViewById(R.id.edtDate2);
        edtStart.setInputType(InputType.TYPE_NULL);
        edtEnd.setInputType(InputType.TYPE_NULL);
        btnDate=(Button) findViewById(R.id.btnDate);

        edtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                start = new DatePickerDialog(WordNegActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtStart.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                Calendar mCalender = Calendar.getInstance();
                                mCalender.set(Calendar.YEAR, year);
                                mCalender.set(Calendar.MONTH, monthOfYear);
                                mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                startDate.setTime(mCalender.getTimeInMillis());

                            }
                        }, year, month, day);
                start.show();
            }
        });

        edtEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                end = new DatePickerDialog(WordNegActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtEnd.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                Calendar mCalender = Calendar.getInstance();
                                mCalender.set(Calendar.YEAR, year);
                                mCalender.set(Calendar.MONTH, monthOfYear);
                                mCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                endDate.setTime(mCalender.getTimeInMillis());

                            }
                        }, year, month, day);
                end.show();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FirebaseData fetch = new FirebaseData(startDate, endDate);
                    fetch.getNegKeywords(startDate, endDate, WordNegActivity.this);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //back to track page
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        drawPlot(keys);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void drawPlot(ArrayList<String> keys){
        List<DataEntry> seriesData = new ArrayList<>();
        Map<String, Long> counts =
                keys.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        for(Map.Entry<String, Long> entry : counts.entrySet()){
            seriesData.add(new CategoryValueDataEntry(entry.getKey(), "negative", Math.toIntExact(entry.getValue())));
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
