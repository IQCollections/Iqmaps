package com.highiq.iqmaps;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetDirections extends AsyncTask<Object, String, String> {

    GoogleMap mMap;
    String url;
    String googleDirections;
    String duration, distance;
    LatLng latLng;

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];

        url = (String) objects[1];
        latLng = (LatLng) objects[2];
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirections = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleDirections;
    }

    @Override
    protected void onPostExecute(String s) {
       HashMap<String, String> directionsList = null;
        //String[] directionsList;
        DataParser parser = new DataParser();
        //directionsList = parser.parseDirections(s);
        //displayDirection(directionsList);


        directionsList = parser.parseDirections(s);
        duration = directionsList.get("duration");
        distance = directionsList.get("distance");

        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Duration = "+ duration);
        markerOptions.snippet("Distance = "+ distance);

        mMap.addMarker(markerOptions);

    }

    public void  displayDirection(String[] directionsList){
        int count = directionsList.length;

        for (int i = 0; i<count; i++){
            PolylineOptions options = new PolylineOptions();
            options.color(Color.CYAN);
            options.width(10);
            options.addAll(PolyUtil.decode(directionsList[i]));

            mMap.addPolyline(options);
        }
    }
}
