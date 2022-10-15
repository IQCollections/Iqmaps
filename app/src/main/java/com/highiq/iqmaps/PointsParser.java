package com.highiq.iqmaps;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PointsParser extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    TaskLoadedCallback taskCallback;
    String directionMode = "driving";

    public PointsParser(Context mContext, String directionMode){
        this.taskCallback = (TaskLoadedCallback) mContext;
        this.directionMode = directionMode;
    }

    // Parsing the data in non-ui thread form

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
        JSONObject jObj;
        List<List<HashMap<String, String>>> routes = null;

        try{
            jObj = new JSONObject(jsonData[0]);
            Log.d("myLogs", jsonData[0].toString());
            DirectionsDataParser pParser = new DirectionsDataParser();
            Log.d("myLogs", pParser.toString());

            // start parsing the data
            routes = pParser.parse(jObj);
            Log.d("myLogs", "Executing routes");
            Log.d("myLogs", routes.toString());

        } catch (Exception e){
            Log.d("myLogs", e.toString());
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points;
        PolylineOptions pOptions = null;
        // traversing all the routes
        for (int i = 0; i < result.size(); i++){
            points = new ArrayList<>();
            pOptions = new PolylineOptions();
            //fetching the routes
            List<HashMap<String, String>> path = result.get(i);
            // fetching all the points in the route
            for(int j =0; j <path.size(); j++){
                HashMap<String, String> point = path.get(j);
                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                com.google.android.gms.maps.model.LatLng position = new com.google.android.gms.maps.model.LatLng(lat, lng);
                points.add(position);
            }
            //add all the points in the route to the pOptions
            pOptions.addAll(points);
            if(directionMode.equalsIgnoreCase("walking")){
                pOptions.width(10);
                pOptions.color(Color.GRAY);
            } else {
                pOptions.width(20);
                pOptions.color(Color.CYAN);
            }
            Log.d("myLogs", "onPostExecute polyline options decoded");

        }
        // drawing the polyline in the google map for the route
        if (pOptions != null) {
            // mMap.addPolyline(pOptions);
            taskCallback.onTaskDone(pOptions);
        } else {
            Log.d("myLogs", "without Polylines drawn");
        }
    }



}
