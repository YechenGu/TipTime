package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;


import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       /* if (isNetWorkConnected()){
            //splash
            showSetNetworkDialog();
        }else{

        }*/

       Timer timer = new Timer();
       timer.schedule(timeTask,2000);
    }
       TimerTask timeTask = new TimerTask(){
           @Override
           public void run() {
                startActivity(new Intent(SplashActivity.this,
                        LoginActivity.class));
           }

        };

    private boolean isNetWorkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return  (info!=null&&info.isConnected());
    }
}