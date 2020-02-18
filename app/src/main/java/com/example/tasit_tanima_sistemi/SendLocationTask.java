package com.example.tasit_tanima_sistemi;

import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendLocationTask extends AsyncTask<String, String, String> {
    private HttpURLConnection urlConnection;
    private String urlString;
    private String data;
    private String result;
    private int responseCode;
    String bearer;
    public SendLocationTask(String response) {
        int endpoint=response.indexOf(",");
        bearer=response.substring(25,endpoint-1);
        //bearer=response;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            urlString = params[0]; // URL to call
            data = params[1]; //data to post
            OutputStream out = null;
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Authorization"," Bearer " +bearer);
            urlConnection.setDoOutput(true);
            out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            out.close();
            urlConnection.connect();

            responseCode = urlConnection.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + urlString);
            System.out.println("Post parameters : " + data);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            //print result
            result=response.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public String getResult() {
        return result;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
