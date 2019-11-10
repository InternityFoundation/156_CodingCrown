package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class lacerationActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laceration);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    public void callAmb(View view){
        Intent intent1 = new Intent(Intent.ACTION_DIAL);
        intent1.setData(Uri.parse("tel:102"));
        startActivity(intent1);
    }

    public void callFam(View view){
        Intent intent2 = new Intent(Intent.ACTION_DIAL);
        intent2.setData(Uri.parse("tel:"));
        startActivity(intent2);
    }

    public void upload(View view){
        Intent intent3 = new Intent(this, uploadDocActivity.class);
        startActivity(intent3);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sendLocation(View view){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        onLocationChanged(location);
    }

    @Override
    public void onLocationChanged(Location location) {
        double longtitude = location.getLongitude();
        double latitude = location.getLatitude();

        Date currentTime = Calendar.getInstance().getTime();

        LocationHelper helper = new LocationHelper(longtitude, latitude);
        FirebaseDatabase.getInstance().getReference("Current Location"+currentTime).setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(lacerationActivity.this, "Location Saved", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(lacerationActivity.this, "Location Not Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
