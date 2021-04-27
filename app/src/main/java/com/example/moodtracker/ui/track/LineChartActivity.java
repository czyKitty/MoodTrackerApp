package com.example.moodtracker.ui.track;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
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
    AnyChartView anyChartView;
    HashMap<String, Double> scores = new HashMap<String, Double>();

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
                start = new DatePickerDialog(LineChartActivity.this,
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
                end = new DatePickerDialog(LineChartActivity.this,
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
                    fetch.getSentimentScore(startDate, endDate, LineChartActivity.this);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        drawPlot(scores);


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

