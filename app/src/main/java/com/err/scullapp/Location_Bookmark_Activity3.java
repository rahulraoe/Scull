package com.err.scullapp;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Location_Bookmark_Activity3 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    TextView about_location,landmark;

    Double LATITUDE,LONGITUDE=0.0;
    String memory,landmark_str,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__bookmark_3);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        about_location=(TextView)findViewById(R.id.about_location);
        landmark=(TextView)findViewById(R.id.landmark);




        LATITUDE=getIntent().getDoubleExtra("latitude",0.0);
        LONGITUDE=getIntent().getDoubleExtra("longitude",0.0);
        address= (String) getIntent().getStringExtra("address");
        memory=getIntent().getStringExtra("memory");

        about_location.setText(memory);

        landmark.setText(address);




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

        // Add a marker in Sydney and move the camera
        LatLng latlng = new LatLng(LATITUDE, LONGITUDE);
        mMap.addMarker(new MarkerOptions().position(latlng).title("Location Bookmark"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LATITUDE,LONGITUDE), 17.0f));

    }
}
