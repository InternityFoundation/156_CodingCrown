package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class EmergencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
    }

    public void uploadPicture(View view){
        Intent intent3 = new Intent(this, uploadDocActivity.class);
        startActivity(intent3);
    }

    public void clickPicture(View view){
        Intent intent4 = new Intent(this, clickPictureActivity.class);
        startActivity(intent4);
    }

    public void callNow(View view) {
        Intent intent5 = new Intent(Intent.ACTION_DIAL);
        intent5.setData(Uri.parse("tel:102"));
        startActivity(intent5);
    }

    public void predictWound(View view){
        Intent intent6 = new Intent(this, uploadToPredictActivity.class);
        startActivity(intent6);
    }

}
