package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences splash = getSharedPreferences(MainActivity.SPLASH_PREF, 0);
        SharedPreferences.Editor editor = splash.edit();
        editor.putInt("splash", 1); // 1 mean it already splash 1 times.
        editor.apply();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 5000); // 4 sec
    }
}
