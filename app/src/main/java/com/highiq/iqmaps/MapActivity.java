package com.highiq.iqmaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.PlaceInfo;

//Places KEy AIzaSyBiLyrgZCVOri6wN39_aiQvaiNy2-7Gf_s
//Directions key AIzaSyDp0grCRn2i366c8NhU0QmnCGcjm7J6l8Y
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private Object Places;

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is Ready");
        mMap = googleMap;

        if(mLocationPermissionGranted){
            getDeviceLocation();

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
                return;
            }
            //error from video 5 in the playlist
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));


    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageView mGps;


    //variables
    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.ic_gps);

        getLocationPermission();
    }

    private void init(){
        Log.d(TAG, "init: Initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API) //error from video 8
                .addApi(Places.PLACE_DETECTION_API) //error from video 8
                .enableAutoManage(this, this)
                .build();
        mSearchText.setOnItemClickListener(mAutocompleteClickListener);

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute method for searching
                    geoLocate();
                }
                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Clicked GPS Icon");
                getDeviceLocation();
            }
        });

        hideSoftKeyboard();
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: Geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        }
        catch(IOException e){
            Log.e(TAG, "geoLocate: IOException" + e.getMessage());
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: Found Location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: Getting Current Location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if(mLocationPermissionGranted){
                @SuppressLint("MissingPermission") Task location = mFusedLocationProviderClient.getLastLocation();//error from video 5 in the playlist
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Log.d(TAG, "onComplete: Found Location");
                                Location currentLocation = (Location) task.getResult();

                                moveCamera(new LatLng(currentLocation.getLatitude(),
                                        currentLocation.getLongitude()), DEFAULT_ZOOM,
                                        "My Location");

                            }
                            else{
                                Log.d(TAG, "onComplete: Current Location is Null");
                                Toast.makeText(MapActivity.this, "Could Not Find Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: Moving Camera to: Lat: " + latLng.latitude
                        + "Long: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);

            mMap.addMarker(options);
        }
        hideSoftKeyboard();
    }

    private void initMap(){
        Log.d(TAG, "initMap: Initializing Map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: Getting Location Permission");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                initMap();
            }
        }
        else{
            ActivityCompat.requestPermissions(this, permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: Called");
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: Permission Failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permission Granted");
                    mLocationPermissionGranted = true;
                    //initialize map

                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    //code for video 9 from playlist as well as errors
    /*
    Google Places API Autocomplete suggestions
     */

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully" + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try {
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name" + place.getName());

                mPlace.setAddress(place.getAddress().toString());
                Log.d(TAG, "onResult: address" + place.getAddress());

                //mPlace.setAttributions(place.getAttributions().toString());
                //Log.d(TAG, "onResult: attribution" + place.getAttributions());

                mPlace.setId(place.getId());
                Log.d(TAG, "onResult: Id" + place.getId());

                mPlace.setLatLng(place.getLatLng());
                Log.d(TAG, "onResult: LatLng" + place.getLatLng());

                mPlace.setRating(place.getRating());
                Log.d(TAG, "onResult: rating" + place.getRating());

                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d(TAG, "onResult: PhoneNumber" + place.getPhoneNumber());

                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: website uri" + place.getWebsiteUri());


                Log.d(TAG, "onResult: Place" + mPlace.toString());
            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException", + e.getMessage());
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace.getName());
            places.release();
        }
    };
}
