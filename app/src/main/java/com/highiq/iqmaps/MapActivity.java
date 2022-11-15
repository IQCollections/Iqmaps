package com.highiq.iqmaps;
/*
    Code Attribution 1:
    Source: YouTube
    Source URL link:https://www.youtube.com/watch?v=iWYsBDCGhGw
    Title Page/Video: How to Create SearchView on Google Map in Android Studio | SearchViewOnMap | Android Coding
    Author name/tag/channel: Android Coding
    Author channel/profile url link: https://www.youtube.com/c/AndroidCoding
 */
/*
    Code Attribution 2:
    Source: YouTube
    Source URL link: https://www.youtube.com/watch?v=ifoVBdtXsv0
    Title Page/Video: Current Location and Nearby Places Suggestions in Android | Google Maps API & Places SDK | 2019
    Author name/tag/channel: Abbas Hassan
    Author channel/profile url link: https://www.youtube.com/channel/UC-bTRak4oQoHkPGL8N22tjQ
 */
/*
    Code Attribution 3:
    Source: YouTube
    Source URL link: https://www.youtube.com/watch?v=eiexkzCI8m8
    Title Page/Video: How to Implement Google Map in Android Studio | GoogleMap | Android Coding
    Author name/tag/channel: Android Coding
    Author channel/profile url link: https://www.youtube.com/c/AndroidCoding
 */
/*
    Code Attribution 4:
    Source: YouTube
    Source URL link:https://www.youtube.com/watch?v=1f4b2-Y_q2A&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi&index=4
    Title Page/Video: Google Services, GPS, and Location Permissions
    Author name/tag/channel: CodingWithMitch
    Author channel/profile url link: https://www.youtube.com/c/CodingWithMitch
 */
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.skyfishjy.library.RippleBackground;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, LocationListener, GoogleMap.OnMarkerClickListener, TaskLoadedCallback {

    private GoogleMap mMap;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser uid;
    private DatabaseReference dbRef;
    FirebaseUser firebaseUser ;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;
    private Location location;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private FloatingActionButton btnUserLocation;
    private MaterialSearchBar materialSearchBar;
    private View mapView;
    private Button btnFind;
    private Button btnGo;
    private TextView txtDistance,txtTime;

    private RippleBackground rippleBg;
    private DatabaseReference favDBRef;
    //changing types of filters
    public String LANDMARK;
    public String METRIC;
    public String MODE;
    //marker
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;


    //nearby place

    int PROXIMITY_RADIUS = 10000;
    double latitude, longitude;
    double end_latitude, end_longitude;
    public String markName,markDes;
    private Button btnaddFav;
    private final float DEFAULT_ZOOM = 15;
    DrawerLayout dl;
    NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
//        MODE = "Dark Mode";
        //firebase
        uid = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Settings").child(uid.getUid());
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadSettings values = snapshot.getValue(ReadSettings.class);
                METRIC = values.getDistanceMeasurement();
                MODE = values.getMode();

                String landmarkValue = values.getLandmark();
                if(landmarkValue.equals("Restaurants")){
                    LANDMARK = "restaurant";
                } else if(landmarkValue.equals("Schools")){
                    LANDMARK = "school";
                } else if(landmarkValue.equals("Hospitals")){
                    LANDMARK = "hospital";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dl = findViewById(R.id.mapsLayout);
        nv = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, R.string.navi_open, R.string.navi_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();
        nv.bringToFront();

        nv.setNavigationItemSelectedListener(this);
        //Setting the type of filters
        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setNavButtonEnabled(false);
        btnFind = findViewById(R.id.btn_find);
        rippleBg = findViewById(R.id.ripple_bg);
        txtDistance = findViewById(R.id.txtDistance);
        txtTime = findViewById(R.id.txtTravelTime);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        //btn go
        btnGo = findViewById(R.id.btn_go);
        btnaddFav = findViewById(R.id.btnAddFav);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);
        Places.initialize(MapActivity.this, getString(R.string.google_maps_api));
        placesClient = Places.createClient(this);
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        //setting the search bar
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString(), true, null, true);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {
                    //opening or closing a navigation drawer
                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    materialSearchBar.disableSearch();
                }
            }
        });

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();
                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if (task.isSuccessful()) {
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            if (predictionsResponse != null) {
                                predictionList = predictionsResponse.getAutocompletePredictions();
                                List<String> suggestionsList = new ArrayList<>();
                                for (int i = 0; i < predictionList.size(); i++) {
                                    AutocompletePrediction prediction = predictionList.get(i);
                                    suggestionsList.add(prediction.getFullText(null).toString());
                                }
                                materialSearchBar.updateLastSuggestions(suggestionsList);
                                if (!materialSearchBar.isSuggestionsVisible()) {
                                    materialSearchBar.showSuggestionsList();
                                }
                            }
                        } else {
                            Log.i("mytag", "prediction fetching task unsuccessful");
                        }
                    }
                });
            }
            //predictions for the searchbar
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                if (position >= predictionList.size()) {
                    return;
                }
                AutocompletePrediction selectedPrediction = predictionList.get(position);
                String suggestion = materialSearchBar.getLastSuggestions().get(position).toString();
                materialSearchBar.setText(suggestion);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialSearchBar.clearSuggestions();
                    }
                }, 1000);
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                final String placeId = selectedPrediction.getPlaceId();
                List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);

                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();
                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                        Place place = fetchPlaceResponse.getPlace();
                        Log.i("mytag", "Place found: " + place.getName());
                        LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(place.getLatLng().latitude)),Double.parseDouble(String.valueOf(place.getLatLng().longitude)));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title(place.getName());
                        markerOptions.position(latLng);
                        mMap.addMarker(markerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            apiException.printStackTrace();
                            int statusCode = apiException.getStatusCode();
                            Log.i("mytag", "place not found: " + e.getMessage());
                            Log.i("mytag", "status code: " + statusCode);
                        }
                    }
                });
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });//finding the landmarks using JSOn and api
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng currentMarkerLocation = mMap.getCameraPosition().target;
                rippleBg.startRippleAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rippleBg.stopRippleAnimation();
                        Log.i("landmark", LANDMARK);
                        StringBuilder stringMapsIcons = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                        stringMapsIcons.append("location="+currentMarkerLocation.latitude+","+currentMarkerLocation.longitude);
                        stringMapsIcons.append("&radius=10000");
                        stringMapsIcons.append("&type="+LANDMARK);
                        stringMapsIcons.append("&sensor=true");
                        stringMapsIcons.append("&key="+getResources().getString(R.string.google_maps_api));

                        String url = stringMapsIcons.toString();
                        Object datafetch[] = new Object[2];
                        datafetch[0] = mMap;
                        datafetch[1] = url;

                        FetchNearBy fetchNearBy = new FetchNearBy();
                        fetchNearBy.execute(datafetch);

                    }
                }, 3000);

            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        favDBRef = FirebaseDatabase.getInstance().getReference().child("Favourites");
        
        btnaddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(markDes.equals(null))  {
                       Toast.makeText(MapActivity.this, "Select a marker!", Toast.LENGTH_SHORT).show();
                    }else {
                  markName = favDBRef.child(firebaseUser.getUid()).push().getKey();
                  favouritesClass favClass = new favouritesClass(markName,markDes,String.valueOf(end_longitude),String.valueOf(end_latitude));
                   favDBRef.child(firebaseUser.getUid()).child(markName).setValue(favClass);
                    Toast.makeText(MapActivity.this, "Favourite Added!", Toast.LENGTH_SHORT).show(); }
                }catch (Exception err){
                    Toast.makeText(MapActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            if(MODE.equals("Dark Mode")){
                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    this, R.raw.mapstyle_dark));

                    if (!success) {
                        Log.e("MapActivity", "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e("MapActivity", "Can't find style. Error: ", e);
                }
            }

            mMap = googleMap;

            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);


            if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                layoutParams.setMargins(0, 0, 40, 180);
            }

            //check if gps is enabled or not and then request user to enable it
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

            SettingsClient settingsClient = LocationServices.getSettingsClient(MapActivity.this);
            Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

            task.addOnSuccessListener(MapActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    getDeviceLocation();
                }
            });

            task.addOnFailureListener(MapActivity.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException) {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        try {
                            resolvable.startResolutionForResult(MapActivity.this, 51);
                        } catch (IntentSender.SendIntentException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });

            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    if (materialSearchBar.isSuggestionsVisible())
                        materialSearchBar.clearSuggestions();
                    if (materialSearchBar.isSearchEnabled())
                        materialSearchBar.disableSearch();
                    return false;
                }
            });
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    Toast.makeText(MapActivity.this, "This location is: " + marker.getTitle(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            btnUserLocation = findViewById(R.id.btnUserLocation);
            //finding the user location
            btnUserLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        findUserLocation();

                    } catch (SecurityException e) {
                        Log.e("Exception: %s", e.getMessage());
                    }
                }
            });

            // directions
            mMap.setOnMarkerClickListener(this);
        }catch (Exception err){

        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
               getDeviceLocation();
            }
        }

    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(MapActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    //setting nav bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_menu,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(dl.isDrawerOpen(GravityCompat.START)){
            dl.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_map:
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_favourite:
                intent = new Intent(this, favourites.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_settings:
                intent = new Intent(this, Settings.class);
                startActivity(intent);
                finish();
                break;
        }
        dl.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    public void onClick(View v) {
        try {
        LatLng currentMarkerLocation = mMap.getCameraPosition().target;
        Object dataTransfer[] = new Object[2];
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
        String url;

        switch (v.getId()) {
            case R.id.btn_go:

                    dataTransfer = new Object[3];
                    url = getDirectionsUrl();
                    GetDirections getDirections = new GetDirections();
                    dataTransfer[0] = mMap;
                    dataTransfer[1] = url;
                    dataTransfer[2] = new LatLng(end_latitude, end_longitude);
                    findUserLocation();
                    place1 = new MarkerOptions().position(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude())).title("location 1");
                    place2 = new MarkerOptions().position(new LatLng(end_latitude, end_longitude)).title("position 2");

                    new FetchURL(MapActivity.this).execute(getUrlDirections(place1.getPosition(), place2.getPosition(), "driving"), "driving");

                    float distance[] = new float[1];
                    Location.distanceBetween(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude(), end_latitude, end_longitude, distance);
                    double finalDistance,traveltime;
                    DecimalFormat df2 = new DecimalFormat("#.##");
                    //Displaying in km
                    if(METRIC.equals("Kms")){
                         finalDistance = distance[0] / 1000;
                        txtDistance.setText(df2.format(finalDistance)+ " KM");
                        //Time = distance/speed
                         traveltime = (finalDistance/45 )*60;

                    }else{
                        finalDistance = (distance[0]/1000) *0.621371;
                        txtDistance.setText(df2.format(finalDistance)+" Miles");
                        traveltime = (finalDistance/30)*60;

                    }
                String s = String.format("%.0f", traveltime);
                txtTime.setText("Time: "+s+" Min");

                    Toast.makeText(this, df2.format(finalDistance), Toast.LENGTH_SHORT).show();

                    getDirections.execute(dataTransfer);


                break;
        }
        }catch (Exception err){
        Toast.makeText(this, "Please select a marker", Toast.LENGTH_SHORT).show();
    }
    }   //setting google direction api and drawing using polyline

    private String getUrlDirections(LatLng origin, LatLng dest, String directionMode) {
        try {
            findUserLocation();
            LatLng currentMarkerLocation = mMap.getCameraPosition().target;
            // origin of route
            String str_origin = "origin=" + mMap.getMyLocation().getLatitude() + "," + mMap.getMyLocation().getLongitude();
            // destination of route
            String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
            // mode
            String mode = "mode=" + directionMode;
            // building the parameters of the web service
            String parameters = str_origin + "&" + str_dest + "&" + mode;
            // output
            String output = "json";
            // building the url
            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getResources().getString(R.string.google_maps_api);
            return url;
        }catch(Exception err){
            return null;
        }
    }

        private void findUserLocation(){

            @SuppressLint("MissingPermission") Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        location = task.getResult();
                        LatLng currentLatLng = new LatLng(location.getLatitude(),
                                location.getLongitude());
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentLatLng,
                                DEFAULT_ZOOM);
                        mMap.moveCamera(update);
                    }
                }
            });
        }

    private String getDirectionsUrl(){
        LatLng currentMarkerLocation = mMap.getCameraPosition().target;
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+currentMarkerLocation.latitude+","+currentMarkerLocation.longitude);
        googleDirectionsUrl.append("&destination="+end_latitude+","+end_longitude);
        googleDirectionsUrl.append("&key="+getResources().getString(R.string.google_maps_api));
        return googleDirectionsUrl.toString();
    }

    private String getUrl(double latitude, double longitude, String nearbyPlaces){
        LatLng currentMarkerLocation = mMap.getCameraPosition().target;
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+currentMarkerLocation.latitude+","+currentMarkerLocation.longitude);
        googlePlaceUrl.append("&radius=10000");
        googlePlaceUrl.append("&type="+nearbyPlaces);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+getResources().getString(R.string.google_maps_api));

        return googlePlaceUrl.toString();
    }

    @Override//setting marker details
    public boolean onMarkerClick(@NonNull Marker marker) {
        end_latitude = marker.getPosition().latitude;
        end_longitude = marker.getPosition().longitude;
        markName = marker.getId();
        markDes = marker.getTitle();

        return false;
    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null){
            currentPolyline.remove();

        }
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}

