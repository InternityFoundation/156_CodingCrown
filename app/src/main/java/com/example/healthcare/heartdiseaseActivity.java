package com.example.healthcare;

import org.tensorflow.lite.Interpreter;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;

public class heartdiseaseActivity extends AppCompatActivity {
    Interpreter tflite2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartdisease);
    }

    public void predictHeart(View view){
        EditText age = (EditText) findViewById(R.id.editText8);
        EditText gender = (EditText) findViewById(R.id.editText9);
        EditText chestPain = (EditText) findViewById(R.id.editText10);
        EditText bloodPressure = (EditText) findViewById(R.id.editText11);
        EditText cholesterol = (EditText) findViewById(R.id.editText12);
        EditText sugar = (EditText) findViewById(R.id.editText13);
        EditText maxHeartRate = (EditText) findViewById(R.id.editText14);
        EditText exercise = (EditText) findViewById(R.id.editText15);

        String a1 = age.getText().toString();
        String g1 = gender.getText().toString();
        String cp1 = chestPain.getText().toString();
        String bp1 = bloodPressure.getText().toString();
        String ch1 = cholesterol.getText().toString();
        String s1 = sugar.getText().toString();
        String max1 = maxHeartRate.getText().toString();
        String exer = exercise.getText().toString();

        if(TextUtils.isEmpty(a1)){
            age.setError("Please enter your age!");
            age.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(g1)){
            gender.setError("Please enter gender, Male:1 Female:0!");
            gender.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(cp1)){
            chestPain.setError("Please scale your chest pain from 0-3!");
            chestPain.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(bp1)){
            bloodPressure.setError("Please enter your BP!");
            bloodPressure.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(ch1)){
            cholesterol.setError("Please enter your cholesterol level!");
            cholesterol.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(s1)){
            sugar.setError("Please enter your sugar level!");
            sugar.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(max1)){
            maxHeartRate.setError("Please enter maximum heart rate!");
            maxHeartRate.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(exer)){
            exercise.setError("Please enter exercise induced angina!");
            exercise.requestFocus();
            return;
        }

        float a = Float.parseFloat(age.getText().toString());
        float g = Float.parseFloat(gender.getText().toString());
        float cp = Float.parseFloat(chestPain.getText().toString());
        float bp = Float.parseFloat(bloodPressure.getText().toString());
        float ch = Float.parseFloat(cholesterol.getText().toString());
        float s = Float.parseFloat(sugar.getText().toString());
        float max = Float.parseFloat(maxHeartRate.getText().toString());
        float ex = Float.parseFloat(exercise.getText().toString());

        try{
            tflite2 = new Interpreter(loadModelFile());
        } catch(Exception ex1){
            ex1.printStackTrace();
        }

        float[][] prediction = doInference(a, g, cp, bp, ch, s, max, ex);
        float first = prediction[0][0];
        float second = prediction[0][1];
        if(first>second){
            Intent i41 = new Intent(this, noheartActivity.class);
            startActivity(i41);
        } else{
            Intent i42 = new Intent(this, yesheartActivity.class);
            startActivity(i42);
        }
    }

    public float[][] doInference(float a, float g, float cp, float bp, float ch, float s, float max, float ex){
        float[] inputVal = new float[13];
        inputVal[0] = a/77;
        inputVal[1] = g;
        inputVal[2] = cp/3;
        inputVal[3] = bp/200;
        inputVal[4] = ch/564;
        inputVal[5] = s;
        inputVal[6] = (float) 0.528053;
        inputVal[7] = max/202;
        inputVal[8] = ex;
        inputVal[9] = (float) 0.167678;
        inputVal[10] = (float) 1.399340;
        inputVal[11] = (float) 0.729373;
        inputVal[12] = (float) 2.313531;

        float[][] outputVal = new float[1][2];

        tflite2.run(inputVal, outputVal);

        return outputVal;
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("heart.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

}
