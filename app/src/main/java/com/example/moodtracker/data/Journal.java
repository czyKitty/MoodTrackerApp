package com.example.moodtracker.data;

import android.os.Build;

import androidx.annotation.RequiresApi;
import com.google.firebase.auth.FirebaseUser;
import com.ibm.watson.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.natural_language_understanding.v1.model.KeywordsResult;
import com.ibm.watson.natural_language_understanding.v1.model.SentimentResult;
import com.ibm.watson.tone_analyzer.v3.model.DocumentAnalysis;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;

import java.util.Date;
import java.util.List;

public class Journal {
    public String uid;
    public Date date;
    public String text;
    public SentimentResult sentiment;
    public List<KeywordsResult> keywords;
    public DocumentAnalysis tones;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Journal(String input, FirebaseUser user, Analysis_nlp out) {
        if (user != null) {
            uid = user.getUid();
        }
        text = input;
        date = new Date();
        sentiment = out.sentiment.getSentiment();
        keywords = out.keys.getKeywords();
        tones = out.tone.getDocumentTone();
    }
    public static class Analysis_nlp {
        ToneAnalysis tone;
        AnalysisResults keys;
        AnalysisResults sentiment;
        public Analysis_nlp(ToneAnalysis a, AnalysisResults b, AnalysisResults c)
        {
            tone = a;
            keys = b;
            sentiment = c;
        }
    }
}
