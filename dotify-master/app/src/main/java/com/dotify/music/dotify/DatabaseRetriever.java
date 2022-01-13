package com.dotify.music.dotify;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DatabaseRetriever extends AsyncTask<String, JSONArray, JSONArray> {

    public static final String SERVER_IP = "192.168.1.160:8080";
    private static final String TAG = "DBRetrieve";

    @Override
    protected JSONArray doInBackground(String... strings) {
        String httpMethod = strings[0]; // GET, POST, DELETE
        String urlString = "http://" + SERVER_IP + "/" + strings[1]; // actual url to send
        String result = "";
        InputStream is = null;

        try {
            Log.i(TAG, "Connecting to " + urlString);
            URL url = new URL(urlString);
            HttpURLConnection urlConnection;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(httpMethod);
            is = urlConnection.getInputStream();
            Log.i(TAG, "CONNECTION FINISHED");
        } catch(Exception e){
            Log.e(TAG, "Error in http connection "+e.toString());
        }

        //convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            result=sb.toString();
            Log.i(TAG, "READ FINISHED");
        }catch(Exception e){
            Log.e(TAG, "Error converting result "+e.toString());
        }

        //parse json data
        try{
            JSONArray jsonArray = new JSONArray(result);
            Log.i(TAG, "JSON BUILD FINISHED");
            return jsonArray;
        } catch(JSONException e){
            Log.e(TAG, "Error parsing data "+e.toString());
        }

        return null;
    }
}
