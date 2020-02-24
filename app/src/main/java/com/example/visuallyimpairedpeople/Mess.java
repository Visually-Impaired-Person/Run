package com.example.visuallyimpairedpeople;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Mess extends AppCompatActivity {
    private static final int REQUEST_CODE = 1234;
    private String number;
    private static final int REQUEST_CODE1 = 12345;

    private static final int REQUEST_CALL = 1;
    String namev;
    private ListView listView;
    private CustomAdapter customAdapter;
    private ArrayList<ContactModel> contactModelArrayList;
    TextToSpeech t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);String toSpeak = "You are in message page. please first say a contact name and then any message which we want to send";
                    Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_LONG).show();
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        listView = (ListView) findViewById(R.id.listView);

        contactModelArrayList = new ArrayList<>();

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            ContactModel contactModel = new ContactModel();
            contactModel.setName(name);
            contactModel.setNumber(phoneNumber);
            contactModelArrayList.add(contactModel);
            Log.d("name>>", name + "  " + phoneNumber);
        }

        phones.close();

        customAdapter = new CustomAdapter(this, contactModelArrayList);
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



        Intent intent = new Intent(Mess.this,Home.class);
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
            int index = 0;
            final String namev = matches.get(0);
            Toast.makeText(this, "" + namev, Toast.LENGTH_SHORT).show();
            t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        t1.setLanguage(Locale.ENGLISH);
                        t1.setLanguage(Locale.UK);



                        String messagespeak="Message sent to"+namev;
                        t1.speak(messagespeak, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            });
            for (int i = 0; i < contactModelArrayList.size(); i++) {
                if (contactModelArrayList.get(i).getName().toLowerCase().contains(namev.toLowerCase()))
                    index = i;
            }
            number = contactModelArrayList.get(index).getNumber();
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    " Voice Recognition...");
            intent.putExtra(
                    RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                    5000000);
            startActivityForResult(intent, 12345);

            //if (matches.contains("Iqra Nasir"))
        }

//////////////////////popup again/////////////////////////////
        if (requestCode == REQUEST_CODE1 && resultCode == RESULT_OK) {
            final ArrayList<String> matches1 = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

//            startActivity(intent);

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

            // if (mat)/*match new request code*/ {
            //matches your mmessage

            sendSMS(number, matches1.get(0));
            t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        t1.setLanguage(Locale.ENGLISH);
                        t1.setLanguage(Locale.UK);



                        String messagespeak="This message"+matches1+"Sent to"+namev;
                        t1.speak(messagespeak, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            });

        }
    }
    private void sendSMS(String number, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = (PendingIntent) PendingIntent.getBroadcast(Mess.this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = (PendingIntent) PendingIntent.getBroadcast(Mess.this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:

                        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status != TextToSpeech.ERROR) {
                                    t1.setLanguage(Locale.ENGLISH);
                                    t1.setLanguage(Locale.UK);


                                    Toast.makeText(getBaseContext(), "SMS sent",
                                            Toast.LENGTH_SHORT).show();
                                    String messagespeak="SMS Sent successfully";
                                    t1.speak(messagespeak, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            }
                        });
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if (status != TextToSpeech.ERROR) {
                                    t1.setLanguage(Locale.ENGLISH);
                                    t1.setLanguage(Locale.UK);


                                    Toast.makeText(getBaseContext(), "Failed to sent",
                                            Toast.LENGTH_SHORT).show();
                                    String messagespeak="Failed to sent";
                                    t1.speak(messagespeak, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            }
                        });
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, message, sentPI, deliveredPI);
    }
    public class SimpleSmsReciever extends BroadcastReceiver {

        private static final String TAG = "Message recieved";

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle pudsBundle = intent.getExtras();
            Object[] pdus = (Object[]) pudsBundle.get("pdus");
            SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);

            // Start Application's  MainActivty activity

            Intent smsIntent=new Intent(context,MainActivity.class);

            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            smsIntent.putExtra("MessageNumber", messages.getOriginatingAddress());

            smsIntent.putExtra("Message", messages.getMessageBody());

            context.startActivity(smsIntent);

            // Get the Sender Message : messages.getMessageBody()
            // Get the SenderNumber : messages.getOriginatingAddress()

            Toast.makeText(context, "SMS Received From :"+messages.getOriginatingAddress()+"\n"+ messages.getMessageBody(), Toast.LENGTH_LONG).show();
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



