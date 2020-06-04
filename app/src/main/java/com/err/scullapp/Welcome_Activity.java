package com.err.scullapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Welcome_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Rahul);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_);

        SharedPreferences preferences=getApplicationContext().getSharedPreferences("User_Details",0);
        SharedPreferences.Editor editor = preferences.edit();
        int app_open= Integer.parseInt(preferences.getString("App_Open","0"));
           app_open=app_open+1;
        preferences.edit().putString("App_Open", String.valueOf(app_open)).apply();
        editor.apply();


        @SuppressLint("RestrictedApi") final SharedPreferences pref =getApplicationContext().getSharedPreferences("User_Details", 0);
        final String nam=pref.getString("User_Name","hello");
        if(nam.equals("hello")) {
            Toast toast = Toast.makeText(Welcome_Activity.this, "    Welcome to Stooz   ", Toast.LENGTH_LONG);

            toast.show();

        }
        else
        {

            Toast toast = Toast.makeText(Welcome_Activity.this, "    Welcome " + nam+"    ", Toast.LENGTH_LONG);

            toast.show();


        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if (auth.getCurrentUser() != null) {
                    // already signed in
                    startActivity(new Intent(Welcome_Activity.this,Home_Activity.class));
                    finish();
                    finish();
                }
                else{
                    startActivity(new Intent(Welcome_Activity.this,On_Board_Activity.class));
                    finish();
                    finish();
                }
            }
        }, 800);
    }


}
