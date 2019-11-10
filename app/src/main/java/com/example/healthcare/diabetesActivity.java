package com.example.healthcare;
import org.tensorflow.lite.Interpreter;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.widget.EditText;
import android.view.View;
import android.content.res.AssetFileDescriptor;


import android.os.Bundle;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import android.content.Intent;

public class diabetesActivity extends AppCompatActivity {
    Interpreter tflite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diabetes);
    }

    public void predict(View view){
        EditText preg = (EditText) findViewById(R.id.editText3);
        EditText glucose = (EditText) findViewById(R.id.editText4);
        EditText insulin = (EditText) findViewById(R.id.editText5);
        EditText bmi = (EditText) findViewById(R.id.editText6);
        EditText age = (EditText) findViewById(R.id.editText7);

        String p = preg.getText().toString();
        String g = glucose.getText().toString();
        String i = insulin.getText().toString();
        String b = bmi.getText().toString();
        String a = age.getText().toString();


        if(TextUtils.isEmpty(p)){
            preg.setError("Please enter no. of pregnancies!");
            preg.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(g)){
            glucose.setError("Please enter glucose level!");
            glucose.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(i)){
            insulin.setError("Please enter insulin level!");
            insulin.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(b)){
            bmi.setError("Please enter BMI!");
            bmi.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(a)){
            age.setError("Please enter Age!");
            age.requestFocus();
            return;
        }

        try{
            tflite = new Interpreter(loadModelFile());
        } catch(Exception ex){
            ex.printStackTrace();
        }
        float prediction = doInference(p,g,i,b,a);
        if(prediction>0.5){
            Intent i21 = new Intent(this, nodiabetesActivity.class);
            startActivity(i21);
        } else {
            Intent i22 = new Intent(this, yesdiabetesActivity.class);
            startActivity(i22);
        }
    }

    public float doInference(String i1, String i2, String i3, String i4, String i5){
        float[] inputVal = new float[5];
        inputVal[0] = Float.valueOf(i1);
        inputVal[1] = Float.valueOf(i2);
        inputVal[2] = Float.valueOf(i3);
        inputVal[3] = Float.valueOf(i4);
        inputVal[4] = Float.valueOf(i5);

        float[][] outputval = new float[1][2];

        tflite.run(inputVal, outputval);
        float inferredValue = outputval[0][1];
        return inferredValue;
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("diet.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


}
