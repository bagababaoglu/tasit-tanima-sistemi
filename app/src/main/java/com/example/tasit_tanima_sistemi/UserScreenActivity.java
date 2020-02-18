package com.example.tasit_tanima_sistemi;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class UserScreenActivity extends AppCompatActivity implements LocationListener {
    EditText plate;
    Button send_button;
    String response;
    String authresponse;
    int responseCode;
    protected LocationManager locationManager;
    protected double latitude=0,longitude=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        authresponse=(String)intent.getStringExtra("response");
        setContentView(R.layout.activity_user_screen);
        plate=(EditText) findViewById(R.id.editText3);
        send_button=(Button)findViewById(R.id.button4);


        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "Problem with GPS",Toast.LENGTH_SHORT).show();
        }
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latitude!=0){
                    responseCode=sendLocation();
                    if(responseCode==200){
                        Toast.makeText(getApplicationContext(), "Successful request.",Toast.LENGTH_SHORT).show();
                    }else if (responseCode==400){
                        Toast.makeText(getApplicationContext(), "Unsuccessful request.",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Problem with GPS",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Problem with GPS",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public int sendLocation(){
        JSONObject location = new JSONObject();
        try{
            location.put("vendorLat", latitude);
            location.put("vendorLong", longitude);

        }catch (JSONException e){
            Log.e("error","JSON");
        }
        String [] params =new String[2];
        params[0]="https://berkay-project-backend.herokuapp.com/vehicles/"+plate.getText()+"/requests";
        params[1]=location.toString();
        SendLocationTask callapi =new SendLocationTask(authresponse);
        callapi.execute(params);
        try{
            callapi.get();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Cannot connect to server. "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        response=callapi.getResult();
        return callapi.getResponseCode();
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
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

    boolean isBackPressed = false;
    @Override
    public void onBackPressed() {
        if (!isBackPressed) {
            Toast.makeText(this, "Press Back Again to exit.", Toast.LENGTH_SHORT).show();
            isBackPressed = true;
        } else finish();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(UserScreenActivity.this,MainActivity.class);
        finish();
        locationManager.removeUpdates(this);
        locationManager=null;
        startActivity(intent);
        return true;
    }
}
