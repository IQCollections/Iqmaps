package com.highiq.iqmaps;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchURL extends AsyncTask<String, Void, String> {
    Context mContext;
    String directionMode = "driving";

    public FetchURL(Context mContext){
        this.mContext = mContext;
    }


    @Override
    protected String doInBackground(String... strings) {
        // For storing data from web service
        String data = "";
        directionMode = strings[1];
        try{
            //Fetching data from web service
            data = downloadURL(strings[0]);
            Log.d("myLog", "Background tasks data" + data.toString());
        } catch (Exception e){
            Log.d("Background Tasks", e.toString());
        }
        return  data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        PointsParser parserTask = new PointsParser(mContext, directionMode);
        // Invokes the thread for parsing the JSON data
        parserTask.execute(s);
    }

    private String downloadURL(String strUrl) throws IOException {
        String data = "";
        InputStream is = null;
        HttpURLConnection urlCon = null;
        try{
            URL url = new URL(strUrl);
            // creating an http connection to communicate with the url
            urlCon = (HttpURLConnection) url.openConnection();
            // connecting to the url
            urlCon.connect();
            // reading data from the url
            is = urlCon.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null){
                sb.append(line);
            }
            data = sb.toString();
            Log.d("myLogs", "Download URL: " + data.toString());
            br.close();

        } catch (Exception e){

        } finally {
            is.close();
            urlCon.disconnect();
        }
        return  data;
    }
}
