package com.highiq.iqmaps;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.Marker;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.common.location.Location;
import com.mapbox.geojson.Point;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.Plugin;


public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private MapboxMap map;
    private Button startButton;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private Plugin.Mapbox locationLayerPlugin;
    private Location originLocation;
    private Point originPosition;
    private Point destinationPosition;
    private Marker destinationMarker;
    private Plugin.Mapbox navigationMapRoute;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    mapView.onLowMemory();
    }


}