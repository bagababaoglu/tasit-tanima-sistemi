package com.example.tasit_tanima_sistemi;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserScreenActivity extends AppCompatActivity implements LocationListener {
    EditText plate;
    Button send_button;
    protected LocationManager locationManager;
    protected LocationListener ocationListener;
    protected Location location;
    protected Context context;
    protected double latitude,longitude;
    protected boolean gps_enabled,network_enabled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);
        plate=(EditText) findViewById(R.id.editText3);
        send_button=(Button)findViewById(R.id.button4);
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "Problem with GPS",Toast.LENGTH_SHORT).show();
        }
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Latitude "+latitude+" Longitude "+longitude,Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        Toast.makeText(getApplicationContext(), "Latitude "+latitude+" Longitude "+longitude,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}
