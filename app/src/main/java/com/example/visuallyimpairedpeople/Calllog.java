package com.example.visuallyimpairedpeople;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Calllog extends AppCompatActivity {
    private static final int REQUEST_CODE = 1234;
    private static final int REQUEST_CALL = 1;
    ListView l1;
    private ListView resultList;
    private ListView listView;
    private CustomAdapter customAdapter;
    private ArrayList<ContactModel> contactModelArrayList;
TextToSpeech t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calllog);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                    String toSpeak = "you are on call page plz say any desired person nmae which you want to call";
                    Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_LONG).show();
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        listView = (ListView) findViewById(R.id.listView);

        contactModelArrayList = new ArrayList<>();

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            ContactModel contactModel = new ContactModel();
            contactModel.setName(name);
            contactModel.setNumber(phoneNumber);
            contactModelArrayList.add(contactModel);
            Log.d("name>>",name+"  "+phoneNumber);
        }

        phones.close();
        customAdapter = new CustomAdapter(this,contactModelArrayList);
        listView.setAdapter(customAdapter);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {

            Toast.makeText(getApplicationContext(), "Recognizer Not Found", Toast.LENGTH_SHORT).show();
        }

        startVoiceRecognitionActivity();
    }
    public void onBackPressed(){



        Intent intent = new Intent(Calllog.this,Home.class);
        startActivity(intent);


    }



    private void startVoiceRecognitionActivity() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                " Voice Recognition...");
        intent.putExtra(
                RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                5000000);
        startActivityForResult(intent, 1234);

    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities == null) {
            startVoiceRecognitionActivity();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            /*System.out.println("Matches list " + matches);
            resultList.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, matches));*/
            int index =0;
            final String namev = matches.get(0);
            Toast.makeText(this, ""+namev, Toast.LENGTH_SHORT).show();
            t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        t1.setLanguage(Locale.ENGLISH);
                        t1.setLanguage(Locale.UK);



                        String messagespeak="Calling"+namev;
                        t1.speak(messagespeak, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            });
            for (int i =0; i<contactModelArrayList.size(); i++) {
                if (contactModelArrayList.get(i).getName().toLowerCase().contains(namev.toLowerCase()))
                    index = i;
            }
                String number = contactModelArrayList.get(index).getNumber();
                //if (matches.contains("Iqra Nasir"))


                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));

                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);

                /*String[] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.
                        CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone._ID};

                String number=from[5].toString();
                Intent callintent= new Intent(Intent.ACTION_CALL, Uri.parse("345389573"));

                if (ActivityCompat.checkSelfPermission(Calllog.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                else {
                    startActivity(callintent);
                }*/
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







