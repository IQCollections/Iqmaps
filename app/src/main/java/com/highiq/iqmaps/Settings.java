package com.highiq.iqmaps;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseUser uid;
    private DatabaseReference dbRef;
    private DatabaseReference dbref2;
    private Button btnBack;
    private Button btnSubmit;
    private EditText editPhone;
    private EditText editAddress;
    private RadioButton rbnKms;
    private RadioButton rbnMiles;
    private RadioButton rbnLight;
    private RadioButton rbnDark;
    private Switch swtRestaurant;
    private Switch swtSchools;
    private Switch swtHospitals;
    DrawerLayout dl;
    NavigationView nv;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();
        btnSubmit = findViewById(R.id.btnApply);
        editPhone = findViewById(R.id.editTextPhone);
        editAddress = findViewById(R.id.editTextAddress);
        rbnKms = findViewById(R.id.radioButtonKms);
        rbnMiles = findViewById(R.id.radioButtonMiles);
        rbnLight = findViewById(R.id.radioButtonLightMode);
        rbnDark = findViewById(R.id.radioButtonDarkMode);
        swtRestaurant = findViewById(R.id.switchRestaurants);
        swtSchools = findViewById(R.id.switchSchools);
        swtHospitals = findViewById(R.id.switchHospitals);
        dl = findViewById(R.id.settings_layout);
        nv = findViewById(R.id.nav_view);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Settings");
        uid = FirebaseAuth.getInstance().getCurrentUser();

        dbref2 = FirebaseDatabase.getInstance().getReference().child("Settings").child(uid.getUid());
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadSettings values = snapshot.getValue(ReadSettings.class);
                String phone = values.getPhone();
                String address = values.getAddress();
                String metrics = values.getDistanceMeasurement();
                String mode = values.getMode();
                String landmark = values.getLandmark();
                editPhone.setText(phone);
                editAddress.setText(address);

                if(metrics.equals("Kms")){
                    rbnKms.setChecked(true);
                } else if(metrics.equals("Miles")){
                    rbnMiles.setChecked(true);
                }

                if(mode.equals("Light Mode")){
                    rbnLight.setChecked(true);
                } else if(mode.equals("Dark Mode")){
                    rbnDark.setChecked(true);
                }

                if(landmark.equals("Restaurants")){
                    swtRestaurant.setChecked(true);
                } else if(landmark.equals("Schools")){
                    swtSchools.setChecked(true);
                } else if(landmark.equals("Hospitals")){
                    swtHospitals.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dbref2.addValueEventListener(postListener);

        onRadioButtonMetricsClicked(rbnKms, rbnMiles);
        onRadioButtonMetricsClicked(rbnMiles, rbnKms);

        onRadioButtonModeClicked(rbnLight, rbnDark);
        onRadioButtonModeClicked(rbnDark, rbnLight);

        switchListener(swtRestaurant, swtSchools, swtHospitals);
        switchListener(swtSchools, swtRestaurant, swtHospitals);
        switchListener(swtHospitals, swtRestaurant, swtSchools);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userPhone = editPhone.getText().toString();
                String userAddress = editAddress.getText().toString();
                //add to database
                dbRef.child(uid.getUid()).child("phone").setValue(userPhone);
                dbRef.child(uid.getUid()).child("address").setValue(userAddress);
                Toast.makeText(Settings.this, "Your info has been updated", Toast.LENGTH_SHORT).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, R.string.navi_open, R.string.navi_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();
        nv.bringToFront();
        nv.setNavigationItemSelectedListener(this);
    }

    public void switchListener(Switch swt, Switch swt2, Switch swt3){
        swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    String input = swt.getText().toString();
                    dbRef = FirebaseDatabase.getInstance().getReference().child("Settings");
                    uid = FirebaseAuth.getInstance().getCurrentUser();
                    dbRef.child(uid.getUid()).child("landmark").setValue(input);
                    swt2.setChecked(false);
                    swt3.setChecked(false);

                    //add true to database
                }
            }
        });
    }

    public void onRadioButtonMetricsClicked(RadioButton rbnCheck, RadioButton rbnAlternate) {
        rbnCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    //add true to database
                    String input = rbnCheck.getText().toString();
                    rbnAlternate.setChecked(false);
                    rbnAlternate.setSelected(false);
                    dbRef = FirebaseDatabase.getInstance().getReference().child("Settings");
                    uid = FirebaseAuth.getInstance().getCurrentUser();
                    dbRef.child(uid.getUid()).child("distanceMeasurement").setValue(input);
                }
            }
        });
    }

    public void onRadioButtonModeClicked(RadioButton rbnCheck, RadioButton rbnAlternate) {
        rbnCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    //add true to database
                    String input = rbnCheck.getText().toString();
                    rbnAlternate.setChecked(false);
                    rbnAlternate.setSelected(false);
                    dbRef = FirebaseDatabase.getInstance().getReference().child("Settings");
                    uid = FirebaseAuth.getInstance().getCurrentUser();
                    dbRef.child(uid.getUid()).child("mode").setValue(input);
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_map:
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_favourite:
                intent = new Intent(this, favourites.class);
                startActivity(intent);
                break;
            case R.id.nav_settings:
                intent = new Intent(this, Settings.class);
                startActivity(intent);
                break;

        }
        dl.closeDrawer(GravityCompat.START);
        return true;
    }
}