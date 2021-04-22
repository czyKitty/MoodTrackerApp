package com.example.moodtracker.data;

import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
//    protected AnalyzerNLP out; // analysis of journal
    protected Date date;
    protected String userID;
    AtomicReference<ToneAnalysis> tone;
    AtomicReference<AnalysisResults> keys;
    AtomicReference<AnalysisResults> sentiment;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Journal(String entry_text, FirebaseUser user) throws InterruptedException {
        text = entry_text;
        AnalyzerNLP out = getSentiment(text);
        tone = out.tone;
        keys = out.keys;
        sentiment = out.sentiment;
        date = new Date();
        userID = user.getUid();
    }

    public AtomicReference<ToneAnalysis> getTone(){
        return tone;
    }

    public AtomicReference<AnalysisResults> getSentiment(){
        return sentiment;
    }

    public AtomicReference<AnalysisResults> getKeyword(){
        return keys;
    }

    public Date getDate(){
        return date;
    }

    /**
     * Static private class to represent analyze result
     */
    static private class AnalyzerNLP {
        AtomicReference<ToneAnalysis> tone;
        AtomicReference<AnalysisResults> keys;
        AtomicReference<AnalysisResults> sentiment;

//        AnalyzerNLP(ToneAnalysis a, AnalysisResults b, AnalysisResults c) {
//            tone = a;
//            keys = b;
//            sentiment = c;
//        }

        public AnalyzerNLP(AtomicReference<ToneAnalysis> t, AtomicReference<AnalysisResults> k, AtomicReference<AnalysisResults> score) {
            tone = t;
            keys = k;
            sentiment = score;
        }
    }

    /**
     *
     * @param input journal entry
     * @return AnalyzerNLP
     */
    private AnalyzerNLP getSentiment(String input) throws InterruptedException {
        AtomicReference<ToneAnalysis> tone = new AtomicReference<>(new ToneAnalysis());
        AtomicReference<AnalysisResults> keys = new AtomicReference<>(new AnalysisResults());
        AtomicReference<AnalysisResults> score = new AtomicReference<>(new AnalysisResults());
        // Authenticate the API
        Authenticator authenticator = new IamAuthenticator("ZaupUklyab6ixv-73XYreQZLWOAfYv85DnjxUyklz4ry");
        ToneAnalyzer service = new ToneAnalyzer("2017-09-21", authenticator);
        service.setServiceUrl("https://api.us-east.tone-analyzer.watson.cloud.ibm.com/instances/c2f34cbf-a90a-4785-88d8-a41c0a08ac8c");

        Authenticator authenticator1 = new IamAuthenticator("SO_Deq8xw8V3uT1inqIAqzyMi2c7ku4OBhcwg97Nb5f6");
        NaturalLanguageUnderstanding service1 = new NaturalLanguageUnderstanding("2019-07-12", authenticator1);
        service1.setServiceUrl("https://api.us-east.natural-language-understanding.watson.cloud.ibm.com/instances/4dd4583a-125a-47b2-bec8-4bf171ffca01");

        Authenticator authenticator2 = new IamAuthenticator("SO_Deq8xw8V3uT1inqIAqzyMi2c7ku4OBhcwg97Nb5f6");
        NaturalLanguageUnderstanding service2 = new NaturalLanguageUnderstanding("2019-07-12", authenticator2);
        service2.setServiceUrl("https://api.us-east.natural-language-understanding.watson.cloud.ibm.com/instances/4dd4583a-125a-47b2-bec8-4bf171ffca01");
        //Run Network API Call on separate thread
        //Use separate thread so to not block the UI thread
        Thread thread = new Thread(){
            public void run(){
                System.out.println("Thread Running");
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
                    keys.set(service1.analyze(parameters).execute().getResult());

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
                            .language("en")
                            .build();
                    AnalysisResults response = service2
                            .analyze(parameters_2)
                            .execute()
                            .getResult();
//                    score.set(service2.analyze(parameters_2).execute().getSentiment());
                    System.out.println(response);
                    score.set(response);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        thread.join();
//        System.out.println(keys);
//        System.out.println(score);

        return new AnalyzerNLP(tone, keys, score);
    }
}
