package com.example.visuallyimpairedpeople;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Locale;

public class SimpleSmsReciever extends BroadcastReceiver {
    TextToSpeech t1;
    String toSpeak;
    String str = "";
    private static final String TAG = "Message recieved";
String no="";
    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);

                str += msgs[i].getMessageBody().toString();
                str += "\n";
                no = msgs[i].getOriginatingAddress();

                Intent smsIntent=new Intent(context,Recieve.class);

                smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



                smsIntent.putExtra("Message", msgs[i].getMessageBody());


                Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(no));
                Cursor c = context.getContentResolver().query(lookupUri, new String[]{ContactsContract.Data.DISPLAY_NAME},null,null,null);
                try {
                    c.moveToFirst();
                    String  displayName = c.getString(0);
                    String ContactName = displayName;
                    smsIntent.putExtra("MessageNumber",ContactName);
                    final String   strmsg=  "SMS Received From :" + ContactName + "\n" + msgs[i].getMessageBody();
                    context.startActivity(smsIntent);
                    t1 = new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status != TextToSpeech.ERROR) {
                                t1.setLanguage(Locale.ENGLISH);
                                t1.setLanguage(Locale.UK);


                                Toast.makeText(context, strmsg, Toast.LENGTH_LONG).show();
                                String messagespeak = strmsg;
                                t1.speak(messagespeak, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                    });
                } catch (Exception e) {
                    // TODO: handle exception
                }finally{
                    c.close();
                }

                // Get the Sender Message : messages.getMessageBody()
                // Get the SenderNumber : messages.getOriginatingAddress()



            }
        }
    }
}