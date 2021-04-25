package com.example.moodtracker.ui.talk;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moodtracker.R;

import java.util.ArrayList;

public class SpeechToTextActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    private EditText txtJournal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_talk);
        txtJournal = (EditText) findViewById(R.id.txt_talk);
    }

    public void onClick(View v)
    {
        //Create an Intent with “RecognizerIntent.ACTION_RECOGNIZE_SPEECH” action
        //ACTION_RECOGNIZE_SPEECH starts an activity that will prompt user for speech and send through a speech recognizer
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        try {
            //Start the Activity and wait for the response
            //startActivityForResult is called in order to receive something from the activity
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException a) {
        }
    }

    //Handle the results
    @Override
    //onActivityResult is called when the activity called by startActivityForResult is done
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    //returns an ArrayList of String through the intent
                    //The array contains possible interpretations of what the user said into the microphone
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtJournal.setText(result.get(0));
                }
                break;
            }
        }
    }
}