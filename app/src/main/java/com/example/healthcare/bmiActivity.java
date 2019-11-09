package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class bmiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
    }

    public void bmiCalculate(View view){
        EditText weight = (EditText) findViewById(R.id.editText);
        int weights = Integer.parseInt(weight.getText().toString());

        EditText height = (EditText) findViewById(R.id.editText2);
        int heights = Integer.parseInt(height.getText().toString());

        double bmi = (double) (weights*10000)/(heights*heights);
        String toastText = Double.toString(bmi);

        if(bmi<=18.5){
            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
            Intent i11 = new Intent(this, thinActivity.class);
            startActivity(i11);
        } else if(bmi>18.5 && bmi<=24.9){
            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
            Intent i12 = new Intent(this, healthyActivity.class);
            startActivity(i12);
        } else if(bmi>24.9 && bmi<=29.9){
            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
            Intent i13 = new Intent(this, overweightActivity.class);
            startActivity(i13);
        } else if(bmi>=30){
            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
            Intent i14 = new Intent(this, obeseActivity.class);
            startActivity(i14);
        }
    }
}
