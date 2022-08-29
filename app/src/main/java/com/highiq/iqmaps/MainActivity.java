package com.highiq.iqmaps;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.maps.Style;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.Plugin;


public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private Plugin.Mapbox map;
    private PermissionsManager permissionsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);
    }
}