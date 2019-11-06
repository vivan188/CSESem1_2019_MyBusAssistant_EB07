package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String Uid = firebaseUser.getUid();
    LatLng loc1 = new LatLng(28.0229, 73.3119);
    LatLng loc2 = new LatLng(31.1471, 75.3412);
    LatLng loc3 = new LatLng(23.0225, 72.5714);
    LatLng loc4 = new LatLng(26.9124, 75.7873);









   // Button busbttn = (Button) findViewById(R.id.busbttn);

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

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        myRef = database.getReference("Users");
        setTitle("My Bus Assistant");
    }
    @Override
    protected void onDestroy(){

        super.onDestroy();

    }
    /*public void senddata(){
        myRef.child("Bus/no").setValue("yes");
    }*/




    @Override
    public void onMapReady(GoogleMap googleMap){
            mMap = googleMap;
            mMap.addMarker(new MarkerOptions().position(loc1).title("Bus1"));
            mMap.addMarker(new MarkerOptions().position(loc2).title("Bus2"));
            mMap.addMarker(new MarkerOptions().position(loc3).title("Bus3"));
            mMap.addMarker(new MarkerOptions().position(loc4).title("Bus4"));
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //Toast.makeText(MapsActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
                    // Log.i("Location",location1.toString());
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    sendLoc(location.getLatitude(), location.getLongitude());

                    ;
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Current Location"));
                    mMap.addMarker(new MarkerOptions().position(loc1).title("Bus1"));
                    mMap.addMarker(new MarkerOptions().position(loc2).title("Bus2"));
                    mMap.addMarker(new MarkerOptions().position(loc3).title("Bus3"));
                    mMap.addMarker(new MarkerOptions().position(loc4).title("Bus4"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 5f));


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
            };
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


                    //Log.i("Location",lastKnownLocation.toString());
                    //mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Current Location"));

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 5f));


                }
            }

        }


    public void sendLoc(Double loc_lat, Double loc_long){
        myRef.child(Uid).child("Location").child("Lat").setValue(loc_lat.toString());
        myRef.child(Uid).child("Location").child("Long").setValue(loc_long.toString());
    }


}

