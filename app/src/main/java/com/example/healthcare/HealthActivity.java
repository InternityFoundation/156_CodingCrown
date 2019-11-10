package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HealthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
    }

    public void bmi(View view){
        Intent i1 = new Intent(this, bmiActivity.class);
        startActivity(i1);
    }

    public void diabetes(View view){
        Intent i2 = new Intent(this, diabetesActivity.class);
        startActivity(i2);
    }

    public void mentaltest(View view){
        Intent i3 = new Intent(this, mentaltestActivity.class);
        startActivity(i3);
    }

    public void heartdisease(View view){
        Intent i4 = new Intent(this, heartdiseaseActivity.class);
        startActivity(i4);
    }
}
