package com.example.visuallyimpairedpeople;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Locale;

public class Splashh extends AppCompatActivity {
    ImageView imageView;
    TextToSpeech t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashh);
        imageView=findViewById(R.id.imageview);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.animation);
        imageView.setAnimation(animation);
        Splashit();
        t1 = new TextToSpeech(getApplicationContext(),new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status){
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                    String toSpeak = "Welcome to application";
                    Toast.makeText(getApplicationContext(),toSpeak,Toast.LENGTH_LONG).show();
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

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



    private void Splashit() {
        Thread thread=new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(4000);
                    startActivity(new Intent(Splashh.this,MainActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }

}
