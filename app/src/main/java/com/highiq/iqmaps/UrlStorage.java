package com.highiq.iqmaps;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlStorage {
    public String retrieveURL(String url) throws IOException{
        String urlData ="";
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;


        try {
            URL getURl = new URL(url);
            httpURLConnection = (HttpURLConnection) getURl.openConnection();
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();
            String line ="";
            while((line=bufferedReader.readLine())!=null){
                sb.append(line);
            }
            urlData =sb.toString();
            bufferedReader.close();

        }catch (IOException e){
            Log.e("ERROR!",e.toString());
    }finally {
            inputStream.close();
            httpURLConnection.disconnect();

        }
        return urlData;
}}

