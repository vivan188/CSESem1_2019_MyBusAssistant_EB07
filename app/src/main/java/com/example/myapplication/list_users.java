package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class list_users extends AppCompatActivity {
    Button button;
    Button srch;
    public EditText dest;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    DatabaseReference myRef1;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String Uid = firebaseUser.getUid();
    String Name = firebaseUser.getDisplayName();
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    LocationManager locationManager;
    LocationListener locationListener;





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                }

            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);


        dest = (EditText) findViewById(R.id.editText);

        myRef = database.getReference("Dest");
        myRef1 = database.getReference("Users");
        myRef1.child(Uid).child("UserName").setValue(Name);
        setTitle("My Bus Assistant");



       // query.addListenerForSingleValueEvent(queryValueListener);




        srch = (Button) findViewById(R.id.srch);
        srch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myRef.child(dest.getText().toString()).child(Uid).child("UserId").setValue(Uid);
                myRef1.child(Uid).child("UserName").setValue(Name);
                if(dest.getText().toString().matches("")){
                    Toast.makeText(list_users.this, "Please enter a destination", Toast.LENGTH_SHORT).show();
                }
                else if(dest.getText().toString().matches("delhi")||dest.getText().toString().matches("Delhi")) {
                    startActivity(new Intent(list_users.this, MapsActivity.class));
                }
                else{
                    startActivity(new Intent(list_users.this, MapsActivity1.class));
                }
            }
        });
        button = (Button) findViewById(R.id.logoutbttn);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(list_users.this, MainActivity.class));
                }

            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Toast.makeText(MapsActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
                // Log.i("Location",location1.toString());
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                sendLoc(location.getLatitude(), location.getLongitude());

                ;


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        }

        ;
        //if sdk < 23
        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Permission not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation((LocationManager.GPS_PROVIDER));
                //Toast.makeText(this, lastKnownLocation.toString(), Toast.LENGTH_SHORT).show();
                LatLng currentLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                sendLoc(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());


            }
        }

    }
    public void sendLoc(Double loc_lat, Double loc_long){
        myRef1.child(Uid).child("Location").child("Lat").setValue(loc_lat.toString());
        myRef1.child(Uid).child("Location").child("Long").setValue(loc_long.toString());
    }
}
