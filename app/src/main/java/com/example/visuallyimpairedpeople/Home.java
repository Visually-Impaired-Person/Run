package com.example.visuallyimpairedpeople;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {
    private static final int REQUEST_CODE = 1234;
    private ListView resultList;
    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                    String toSpeak = "Now you are on Home page";
                    Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_LONG).show();
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        home();



    }

    public void onBackPressed(){



        Intent intent = new Intent(Home.this,Login.class);
        startActivity(intent);


    }


    private void home() {
        resultList = (ListView) findViewById(R.id.list);
        // to check if recognizer available or not
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {

            Toast.makeText(getApplicationContext(), "Recognizer Not Found", 1000).show();
        }

        startVoiceRecognitionActivity();
    }

    private void startVoiceRecognitionActivity() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                " Voice Recognition...");
        intent.putExtra(
                RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                50000000);
        startActivityForResult(intent, 1234);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            System.out.println("Matches list " + matches);
            resultList.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, matches));
            if (matches.contains("call")) {
                startActivity(new Intent(Home.this, Calllog.class));


            } else if (matches.contains("message")) {
                startActivity(new Intent(Home.this, Mess.class));
            } else if (matches.contains("camera")) {
                startActivity(new Intent(Home.this, Lookout.class));
            } else if (matches.contains("map")) {
                startActivity(new Intent(Home.this, Location.class));
            } else if (matches.contains("help")) {
                startActivity(new Intent(Home.this, Guide.class));
            } else if (matches.contains("pop up")) {
                startVoiceRecognitionActivity();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    public void onPause()
    {
        if (t1 !=null)
        {
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}
