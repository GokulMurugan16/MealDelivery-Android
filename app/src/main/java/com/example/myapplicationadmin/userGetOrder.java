package com.example.myapplicationadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class userGetOrder extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean locationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final LatLng defaultLocation = new LatLng(43.6761, -79.4105);
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    float distance = 0;
    FirebaseFirestore db;

    String details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_get_order);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        db = FirebaseFirestore.getInstance();
        Intent i = getIntent();
        details = i.getStringExtra("details");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(userGetOrder.this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
        // Add a marker in Sydney and move the camera
        LatLng GeorgeBrown = defaultLocation;
        mMap.addMarker(new MarkerOptions().position(GeorgeBrown).title("Marker in George Brown"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GeorgeBrown,20));
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }



    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {

                                LatLng loc = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(loc).title(" This was your Previous Location"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), 25));
                                calc(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());

                            }
                        } else {
                            Log.d("TAG", "Current location is null. Using defaults.");
                            Log.e("TAG", "Exception: %s", task.getException());

                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, 25));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }


    public void calc(double lat, double lon)
    {
        Location locationA = new Location("point A");

        locationA.setLatitude(lat);
        locationA.setLongitude(lon);

        Location locationB = new Location("point B");

        locationB.setLatitude(43.6761);
        locationB.setLongitude(-79.4105);

        distance = locationA.distanceTo(locationB);

        if(distance<100)
        {
            int[] a = {1,2,3};
            Random r = new Random();
            int b = r.nextInt(a.length);
            final int slot = a[b];

            new AlertDialog.Builder(userGetOrder.this)
                    .setTitle("Alert Them?")
                    .setMessage("Check in at Parking Slot :" + a[b])
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                            String currentDate = sdf.format(new Date());
                            Map<String, Object> user = new HashMap<>();
                            user.put("Details", details);
                            user.put("Date", currentDate);
                            user.put("Parking",slot);

                            db.collection("OrderPickup")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG", "Error adding document", e);
                                        }
                                    });

                        }
                    }).show();



        }
        else
        {

            new AlertDialog.Builder(userGetOrder.this)
                    .setTitle("Too Far")
                    .setMessage("You aren’t at the store yet! Please retry when you are at the store")
                    .setPositiveButton("ok", null).show();

        }


    }

}