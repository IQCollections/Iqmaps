package com.highiq.iqmaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;

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
    private Switch swtRestaurant;
    private Switch swtBars;
    private Switch swtSports;
    private Switch swtGroceries;
    private Switch swtHistory;
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
        swtRestaurant = findViewById(R.id.switchRestaurants);
        swtBars = findViewById(R.id.switchBars);
        swtSports = findViewById(R.id.switchSports);
        swtGroceries = findViewById(R.id.switchGroceries);
        swtHistory = findViewById(R.id.switchHistory);
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
                boolean restaurant = values.isRestaurants();
                boolean bars = values.isBars();
                boolean sports = values.isSports();
                boolean groceries = values.isGroceries();
                boolean history = values.isHistory();
                editPhone.setText(phone);
                editAddress.setText(address);
                if(metrics.equals("Kms")){
                    rbnKms.setChecked(true);
                } else if(metrics.equals("Miles")){
                    rbnMiles.setChecked(true);
                }
                swtRestaurant.setChecked(restaurant);
                swtBars.setChecked(bars);
                swtSports.setChecked(sports);
                swtGroceries.setChecked(groceries);
                swtHistory.setChecked(history);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dbref2.addValueEventListener(postListener);

        onRadioButtonClicked(rbnKms, rbnMiles);
        onRadioButtonClicked(rbnMiles, rbnKms);
        switchListener(swtRestaurant);
        switchListener(swtBars);
        switchListener(swtSports);
        switchListener(swtGroceries);
        switchListener(swtHistory);

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

    public void switchListener(Switch swt){
        swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                String input = swt.getText().toString();
                dbRef = FirebaseDatabase.getInstance().getReference().child("Settings");
                uid = FirebaseAuth.getInstance().getCurrentUser();
                if(isChecked) {
                    switch (input) {
                        case "Restaurants":
                            dbRef.child(uid.getUid()).child("restaurants").setValue(true);
                            break;
                        case "Bars":
                            dbRef.child(uid.getUid()).child("bars").setValue(true);
                            break;
                        case "Sports":
                            dbRef.child(uid.getUid()).child("sports").setValue(true);
                            break;
                        case "Groceries":
                            dbRef.child(uid.getUid()).child("groceries").setValue(true);
                            break;
                        case "Historical Sites":
                            dbRef.child(uid.getUid()).child("history").setValue(true);
                            break;
                    }
                    //add true to database
                } else {
                    switch (input) {
                        case "Restaurants":
                            dbRef.child(uid.getUid()).child("restaurants").setValue(false);
                            break;
                        case "Bars":
                            dbRef.child(uid.getUid()).child("bars").setValue(false);
                            break;
                        case "Sports":
                            dbRef.child(uid.getUid()).child("sports").setValue(false);
                            break;
                        case "Groceries":
                            dbRef.child(uid.getUid()).child("groceries").setValue(false);
                            break;
                        case "Historical Sites":
                            dbRef.child(uid.getUid()).child("history").setValue(false);
                            break;
                    //add false to database
                    }
                }
            }
        });
    }

    public void onRadioButtonClicked(RadioButton rbnCheck, RadioButton rbnAlternate) {
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