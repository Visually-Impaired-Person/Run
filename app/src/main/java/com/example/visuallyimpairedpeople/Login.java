package com.example.visuallyimpairedpeople;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Login extends AppCompatActivity {
    EditText ed_usereml,ed_password;
    String str_usereml,str_password;
    String username = "jobin";
    Button loginbtn;
    TextView msignup, msignuptxtvw;
    ProgressDialog progressDialog;
    public static final String MyPREFERENCES = "MyPrefs" ;
TextToSpeech t1;
    SharedPreferences sharedpreferences;
    private String URL="https://eznamalik.000webhostapp.com/db/login.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                    String toSpeak = "Now you are on Login page";
                    Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_LONG).show();
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        loginbtn = findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setTitle("Logging in");
                progressDialog.setMessage("Please wait");
                progressDialog.create();
                ed_usereml = findViewById(R.id.ed_username);
                ed_password = findViewById(R.id.ed_password);
                 str_usereml  = ed_usereml.getText().toString();
                 str_password  = ed_password.getText().toString();

                if (ed_usereml.equals("") & ed_password.equals("")) {
                    ed_usereml.setError("Please enter your email");
                    ed_password.setError("Enter your email please");
                }
                msignup = findViewById(R.id.signupbtn);
                msignup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Login.this, MainActivity.class));
                    }
                });
                msignuptxtvw = findViewById(R.id.txtvwsignup);
                msignuptxtvw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Login.this, MainActivity.class));
                    }
                });


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.cancel();
                    startActivity(new Intent(Login.this, Home.class));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.cancel();
                    Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString("Email", str_usereml);
                    editor.putString("Password", str_password);

                    editor.commit();
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                requestQueue.add(stringRequest);
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

}

