package com.highiq.iqmaps;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.List;

public class favourites extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout dl;
    NavigationView nv;
    ListView listView;
    private DatabaseReference dbref;
    private FirebaseUser uid;
    List<favouritesClass> favouritesClassList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        dl = findViewById(R.id.favouriteslayout);
        nv = findViewById(R.id.nav_view);
        listView = findViewById(R.id.favouritelistview);
        uid = FirebaseAuth.getInstance().getCurrentUser();
        dbref = FirebaseDatabase.getInstance().getReference("Favourites").child(uid.getUid());
        favouritesClassList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Favourites").child(uid.getUid());


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, R.string.navi_open, R.string.navi_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();
        nv.bringToFront();
        nv.setNavigationItemSelectedListener(this);



    }

    @Override
    protected void onStart() {
        super.onStart();
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            favouritesClassList.clear();
            for(DataSnapshot favouritesSnapshot : snapshot.getChildren()){
                    favouritesClass favClass = favouritesSnapshot.getValue(favouritesClass.class);
                    favouritesClassList.add(favClass);
            }
                favourites_adapter adapter = new favourites_adapter(favourites.this, favouritesClassList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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