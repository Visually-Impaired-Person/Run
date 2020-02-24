package com.example.visuallyimpairedpeople;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    EditText ed_username,ed_email,ed_password;
    String str_username,str_email,str_password;
    TextToSpeech t1;
    private String URL="https://eznamalik.000webhostapp.com/db/signup.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                    String toSpeak = "you are on Signup page";
                    Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_LONG).show();
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });


                ed_email = findViewById(R.id.ed_email);
                ed_username = findViewById(R.id.ed_username);
                ed_password = findViewById(R.id.ed_password);

            }

            public void Register(View view) {
                str_email = ed_email.getText().toString();
                str_username = ed_username.getText().toString();
                str_password = ed_password.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("Name",str_username);
                        params.put("Email",str_email);
                        params.put("Password",str_password);

                        return params;
                    }
                };
                RequestQueue requestQueue= Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }

            public void navigatetologin(View view) {
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
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

