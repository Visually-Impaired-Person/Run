package com.example.visuallyimpairedpeople;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

public class Lookout extends AppCompatActivity {

    private static final int CAMERA_REQUEST =12 ;
TextToSpeech t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookout);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                    String toSpeak = "Camera is open";
                    Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_LONG).show();
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        Intent camintent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camintent,CAMERA_REQUEST);
    }
    public void onBackPressed(){



        Intent intent = new Intent(Lookout.this,Home.class);
        startActivity(intent);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }

        }
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
