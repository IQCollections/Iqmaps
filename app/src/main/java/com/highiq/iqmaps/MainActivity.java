package com.highiq.iqmaps;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.Marker;
//import com.mapbox.android.core.location.LocationEngine;
//import com.mapbox.common.location.Location;
//import com.mapbox.geojson.Point;
//import com.mapbox.maps.MapboxMap;
//import com.mapbox.maps.Style;
//import com.mapbox.android.core.permissions.PermissionsManager;
//import com.mapbox.maps.MapView;
//import com.mapbox.maps.Style;
//import com.mapbox.maps.plugin.Plugin;

// API AIzaSyC8cLuPcApXvOHR_rcOvYuJE7YIjNoqZG4
public class MainActivity extends AppCompatActivity {
    //private MapView mapView;
    //private MapboxMap map;
    //private Button startButton;
    //private PermissionsManager permissionsManager;
    //private LocationEngine locationEngine;
    //private Plugin.Mapbox locationLayerPlugin;
    //private Location originLocation;
    //private Point originPosition;
    //private Point destinationPosition;
    //private Marker destinationMarker;
    //private Plugin.Mapbox navigationMapRoute;
    //private static final String TAG = "MainActivity";

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mapView = findViewById(R.id.mapView);
        //mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);

        if(isServicesOK()){
            init();
        }
    }
    private void init(){
        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
           //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can resolve it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this, "Make request not working", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    //@Override
    //protected void onStart() {
        //super.onStart();
        //mapView.onStart();
    //}


    //@Override
    //protected void onStop() {
        //super.onStop();
        //mapView.onStop();
    //}

    //@Override
    //public void onLowMemory() {
        //super.onLowMemory();
    //mapView.onLowMemory();
    //}


}