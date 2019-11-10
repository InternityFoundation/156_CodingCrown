package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class bmiActivity extends AppCompatActivity {

    EditText weight;
    EditText height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        weight = (EditText) findViewById(R.id.editText);
        height = (EditText) findViewById(R.id.editText2);

    }

    public void bmiCalculate(View view){

        String weight1 = weight.getText().toString();
        String height1 = height.getText().toString();

        if(TextUtils.isEmpty(weight1)){
            weight.setError("Weight is required!");
            weight.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(height1)){
            height.setError("Height is required!");
            height.requestFocus();
            return;
        }

        int weights = Integer.parseInt(weight.getText().toString());
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
