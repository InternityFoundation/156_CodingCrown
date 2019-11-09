package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
//this is a test comment for commit and push demonstration
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // for new activity page for health care tab
    public void healthCare(View view){
        Intent intent1 = new Intent(this, HealthActivity.class);
        startActivity(intent1);
    }

    // for new activity page for emergency tab
    public void emergency(View view){
        Intent intent2 = new Intent(this, EmergencyActivity.class);
        startActivity(intent2);
    }
}
