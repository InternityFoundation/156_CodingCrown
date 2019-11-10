package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private Handler mWaitHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWaitHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                } catch(Exception ignored){
                    ignored.printStackTrace();
                }
            }
        },700);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWaitHandler.removeCallbacksAndMessages(null);
    }
}
