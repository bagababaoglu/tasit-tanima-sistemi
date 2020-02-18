package com.example.tasit_tanima_sistemi;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    Button login_button, cancel_button;
    EditText username, password;
    String usrname,pssword;
    String response;
    ProgressBar progBar;
    private String[] permissions= {Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET};

    TextView tx1;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setContentView(R.layout.activity_main);
        init();
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progBar.setVisibility(View.VISIBLE);
                int responseCode=authenticateUser();
                if(responseCode==200){
                    Intent intent = new Intent(getApplicationContext(), UserScreenActivity.class);
                    intent.putExtra("response",response);
                    finish();
                    startActivity(intent);
                }else{
                    progBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
                    tx1.setVisibility(View.VISIBLE);
                    tx1.setBackgroundColor(Color.RED);
                    counter--;
                    tx1.setText(Integer.toString(counter));

                    if (counter == 0) {
                        login_button.setEnabled(false);
                    }
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    boolean isBackPressed = false;
    @Override
    public void onBackPressed() {
        if (!isBackPressed) {
            Toast.makeText(this, "Press Back Again to exit.", Toast.LENGTH_SHORT).show();
            isBackPressed = true;
        } else finish();

    }

    public int authenticateUser(){
        usrname=username.getText().toString();
        pssword=password.getText().toString();
        //create json object
        JSONObject person = new JSONObject();
        try{
            person.put("username", usrname);
            person.put("password", pssword);
        }catch (JSONException e){
            Log.e("error","JSON");
        }
        String [] params =new String[2];
        params[0]="https://berkay-project-backend.herokuapp.com/auth/workerLogin";
        params[1]=person.toString();
        AuthorizeUserTask callapi =new AuthorizeUserTask();
        callapi.execute(params);
        try{
            callapi.get();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Cannot connect to server. "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        response=callapi.getResult();
        return callapi.getResponseCode();

    }

    public void checkPermission (){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, permissions, 0);

    }
    public void init(){
        login_button = (Button)findViewById(R.id.button);
        username = (EditText)findViewById(R.id.editText);
        password = (EditText)findViewById(R.id.editText2);
        cancel_button = (Button)findViewById(R.id.button2);
        progBar=findViewById(R.id.simpleProgressBar);
        progBar.setVisibility(View.INVISIBLE);
        tx1 = (TextView)findViewById(R.id.textView3);
        tx1.setVisibility(View.GONE);

    }



    }












