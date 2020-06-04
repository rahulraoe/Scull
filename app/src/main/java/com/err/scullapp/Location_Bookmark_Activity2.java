package com.err.scullapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Location_Bookmark_Activity2 extends FragmentActivity implements OnMapReadyCallback, LocationListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener, View.OnClickListener,
        GoogleMap.OnInfoWindowClickListener  {

    private GoogleMap mMap;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText about_location,landmark;

    Button add_button;

    LinearLayout linearLayout;




    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;

    Double LONGITUDE=0.0;
    Double LATITUDE=0.0;
    private ProgressDialog Dialog;
    String coins;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__bookmark_2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mAuth = FirebaseAuth.getInstance();


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();


        landmark=(EditText)findViewById(R.id.landmark);

        about_location=(EditText)findViewById(R.id.about_location);

        add_button=(Button)findViewById(R.id.add_button);
        linearLayout=(LinearLayout)findViewById(R.id.add_bottom) ;

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.location.Address locationAddress;
                locationAddress=getAddress(LATITUDE,LONGITUDE);


               if(LATITUDE==0||LONGITUDE==0){
                    Snackbar snackbar = Snackbar
                            .make(linearLayout, "CANNOT DETECT ADDRESS", Snackbar.LENGTH_LONG);
                    snackbar.show();                }

                else
                {
                    Dialog = ProgressDialog.show(Location_Bookmark_Activity2.this,
                            "PLEASE WAIT...", "SAVING LOCATION BOOKMARK", true);



                    DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy hh.mm aa");
                    final String    date = dateFormat2.format(new Date()).toString();
                    final Map<String, Object> address = new HashMap<>();


                    DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
                    final String timeline = dateFormat222.format(new Date()).toString();

                    address.put("Address",locationAddress.getAddressLine(0)+"." );
                    address.put("Landmark",landmark.getText().toString());
                    address.put("About_Location",about_location.getText().toString());
                    address.put("Longitude",LONGITUDE);
                    address.put("Locality",locationAddress.getSubLocality());
                    address.put("Latitude",LATITUDE);
                    address.put("Pincode",locationAddress.getPostalCode());
                    address.put("Date",date);
                    address.put("TimeLine",timeline);
                    // Toast.makeText(MapsActivity2.this, "hello"+latitude+" " +longitude+" "+landd.getText().toString()+" "+adddddd.getText().toString()+locationAddress.getPostalCode(), Toast.LENGTH_SHORT).show();
                    String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                    StringBuilder salt = new StringBuilder();
                    Random rnd = new Random();
                    while (salt.length() < 18) { // length of the random string.
                        int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                        salt.append(SALTCHARS.charAt(index));
                    }
                    String saltStr = salt.toString();


                    SharedPreferences preferences=getApplicationContext().getSharedPreferences("User_Details",0);
                    final String DOC_ID=preferences.getString("Active_Group_Id","Empty");

                    final String User_Name=preferences.getString("User_Name","User");
                    db.collection("GROUPS").document(DOC_ID).collection("LOCATION_BOOKMARKS").document(saltStr.substring(0,5))
                            .set(address)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                                    final String date = dateFormat2.format(new Date()).toString();

                                    DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
                                    final String timeline = dateFormat222.format(new Date()).toString();

                                    final Map<String, Object> data = new HashMap<>();

                                    data.put("Date",date);
                                    data.put("Added_By",User_Name);
                                    data.put("TimeLine",timeline);
                                    data.put("text",User_Name+" added new location bookmark." );





                                    db.collection("GROUPS").document(DOC_ID).collection("GROUP_ACTIVITY")
                                            .add(data)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {








                                                    final FirebaseUser user = mAuth.getCurrentUser();
                                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
                                                    SharedPreferences.Editor editor = pref.edit();

                                                    int c=Integer.parseInt(coins)-75;
                                                    pref.edit().putString("coins",String.valueOf(c)).apply();
                                                    editor.apply();

                                                    final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid());
                                                    contact.update("Coins",String.valueOf(c))
                                                            .addOnSuccessListener(new OnSuccessListener< Void >() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {


                                                                    Toast.makeText(Location_Bookmark_Activity2.this, "Address Added Successfully", Toast.LENGTH_SHORT).show();
                                                                    Dialog.dismiss();
                                                                    finish();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Location_Bookmark_Activity2.this, "ERROR OCCURED", Toast.LENGTH_LONG).show();
                                }
                            });
                }

            }
        });


    }


    public Address getAddress(double latitude, double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(Location_Bookmark_Activity2.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);

        } catch (IOException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

        return null;

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
        mMap=googleMap;

        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);



//        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
//            @Override
//            public void onCameraIdle() {
//                // Cleaning all the markers.
//                if (mMap != null) {
//                    mMap.clear();
//                }
//
//                LatLng latLng= mMap.getCameraPosition().target;
//                mMap.addMarker(new MarkerOptions()
//                        .position(latLng)
//                        .draggable(true)//Making the marker draggable
//                        .snippet("Drag to adjust")
//                        .anchor(0.5f, 1)
//                        .title("Your Location"));
//
//                LATITUDE=latLng.latitude;
//                LONGITUDE=latLng.longitude;
//
//                Toast.makeText(Location_Bookmark_Activity2.this, "hello"+LONGITUDE, Toast.LENGTH_SHORT).show();
//
//
//
//
//
//
//
//            }
//        });


        LatLng latLng = new LatLng(LATITUDE, LONGITUDE);
        mMap.addMarker(new MarkerOptions().position(latLng)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mMap.clear();
        LATITUDE = marker.getPosition().latitude;
        LONGITUDE = marker.getPosition().longitude;
        moveMap();

    }

    private void moveMap() {
        LatLng latLng = new LatLng(LATITUDE, LONGITUDE);

        mMap.addMarker(new MarkerOptions().position(latLng)
                .title("YOUR LOCATION"));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LATITUDE, LONGITUDE), 17.0f));
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {

                                    LONGITUDE=location.getLongitude();
                                    LATITUDE=location.getLatitude();

                                    LatLng latLng = new LatLng(LATITUDE, LONGITUDE);
                                    mMap.addMarker(new MarkerOptions().position(latLng)
                                            .anchor(0.5f, 1)
                                            .title("YOUR LOCATION"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LATITUDE,LONGITUDE), 17.0f));

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }



    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }



    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            LONGITUDE=mLastLocation.getLongitude();
            LATITUDE=mLastLocation.getLatitude();

        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

}
