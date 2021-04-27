package com.example.moodtracker.ui.track;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.chart.common.dataentry.CategoryValueDataEntry;
import com.example.moodtracker.data.FirebaseData;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
    AnyChartView anyChartView;

    DatePickerDialog start;
    DatePickerDialog end;

    EditText edtStart;
    EditText edtEnd;
    Button btnDate;
    Date startDate = new Date();
    Date endDate = new Date();

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
                start = new DatePickerDialog(PieChartActivity.this,
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
                end = new DatePickerDialog(PieChartActivity.this,
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
                    fetch.getTones(startDate, endDate, PieChartActivity.this);
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

        drawPlot(tones);


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