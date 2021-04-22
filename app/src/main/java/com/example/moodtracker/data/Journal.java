package com.example.moodtracker.data;

import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.natural_language_understanding.v1.model.Features;
import com.ibm.watson.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.natural_language_understanding.v1.model.SentimentOptions;
import com.ibm.watson.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.tone_analyzer.v3.model.ToneOptions;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import static android.content.Context.MODE_PRIVATE;

public class Journal {
    protected String text; // text of the journal
    protected AnalyzerNLP out; // analysis of journal

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Journal(String entry_text) {
        text = entry_text;
        out = getSentiment(text);
    }

    public ToneAnalysis getTone(){
        return out.tone;
    }

    public AnalysisResults getSentiment(){
        return out.sentiment;
    }

    public AnalysisResults getKeyword(){
        return out.keys;
    }

    /**
     * Static private class to represent analyze result
     */
    static private class AnalyzerNLP {
        ToneAnalysis tone;
        AnalysisResults keys;
        AnalysisResults sentiment;

        AnalyzerNLP(ToneAnalysis a, AnalysisResults b, AnalysisResults c) {
            tone = a;
            keys = b;
            sentiment = c;
        }
    }

    /**
     *
     * @param input journal entry
     * @return AnalyzerNLP
     */
    private AnalyzerNLP getSentiment(String input) {
        AtomicReference<ToneAnalysis> tone = new AtomicReference<>(new ToneAnalysis());
        AtomicReference<AnalysisResults> keys = new AtomicReference<>(new AnalysisResults());
        AtomicReference<AnalysisResults> score = new AtomicReference<>(new AnalysisResults());
        // Authenticate the API
        Authenticator authenticator = new IamAuthenticator("ZaupUklyab6ixv-73XYreQZLWOAfYv85DnjxUyklz4ry");
        ToneAnalyzer service = new ToneAnalyzer("2017-09-21", authenticator);
        service.setServiceUrl("https://api.us-east.tone-analyzer.watson.cloud.ibm.com/instances/c2f34cbf-a90a-4785-88d8-a41c0a08ac8c");

        Authenticator authenticator_nlp = new IamAuthenticator("SO_Deq8xw8V3uT1inqIAqzyMi2c7ku4OBhcwg97Nb5f6");
        NaturalLanguageUnderstanding service_nlp = new NaturalLanguageUnderstanding("2019-07-12", authenticator_nlp);
        service_nlp.setServiceUrl("https://api.us-east.natural-language-understanding.watson.cloud.ibm.com/instances/4dd4583a-125a-47b2-bec8-4bf171ffca01");

        //Run Network API Call on separate thread
        //Use separate thread so to not block the UI thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Tone Analysis
                    ToneOptions toneOptions = new ToneOptions.Builder()
                            .text(input)
                            .build();
                    tone.set(service.tone(toneOptions).execute().getResult());

                    // Keyword Analysis
                    KeywordsOptions keywords = new KeywordsOptions.Builder()
                            .sentiment(true)
                            .emotion(true)
                            .limit(3)
                            .build();
                    Features features = new Features.Builder()
                            .keywords(keywords)
                            .build();
                    AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                            .text(input)
                            .features(features)
                            .build();
                    keys.set(service_nlp.analyze(parameters).execute().getResult());

                    // General Sentiment Analysis
                    SentimentOptions sentiment = new SentimentOptions.Builder()
                            .document(true)
                            .build();
                    Features features_2 = new Features.Builder()
                            .sentiment(sentiment)
                            .build();
                    AnalyzeOptions parameters_2 = new AnalyzeOptions.Builder()
                            .text(input)
                            .features(features_2)
                            .build();
                    score.set(service_nlp.analyze(parameters_2).execute().getResult());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return new AnalyzerNLP(tone.get(), keys.get(), score.get());
    }
}
