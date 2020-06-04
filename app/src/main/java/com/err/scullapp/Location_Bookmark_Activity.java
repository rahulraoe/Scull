package com.err.scullapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Location_Bookmark_Activity extends AppCompatActivity {

    RecyclerView padrecyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<LocationModel> dataaaaa;
    LocationAdapter padapter;
    Button tt;
    RelativeLayout ttt;
    private ProgressDialog Dialog;
    ImageView imgg;
    TextView txt;



    CircleImageView Location_Bookmark;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;

    private ArrayList<LocationModel> addressdata;

    LocationAdapter recycler_adapter;

    RelativeLayout Rel_Location;
    RecyclerView Location_Recycler;
    String coins;

    RelativeLayout Rel_Group_Gallery_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__bookmark_);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Location Bookmarks");
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
        coins=pref.getString("coins","0");

        Rel_Group_Gallery_no=(RelativeLayout)findViewById(R.id.Rel_Group_Gallery_no);
        Rel_Group_Gallery_no.setVisibility(View.GONE);

        Location_Recycler=(RecyclerView)findViewById(R.id.Location_Recycler);

        Rel_Location=(RelativeLayout)findViewById(R.id.Rel_Location);

        Location_Bookmark=(CircleImageView)findViewById(R.id.Location_Bookmark);
        Glide.with(Location_Bookmark_Activity.this)
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/record%20%20icon.png?alt=media&token=21e1b176-fbfb-423d-9c7d-fac4f155f240")
                .into(Location_Bookmark)
        ;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        addressdata=new ArrayList<>();
        LoadRecyclerView();
        LoadDataRecycler();
        Location_Bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coins=pref.getString("coins","0");

                if(Integer.parseInt(coins)<75) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(Location_Bookmark_Activity.this);
                    builder.setMessage("You dont have 75 coins to create group.Earn coins.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent in =new Intent(Location_Bookmark_Activity.this,Coins_Activity.class);
                                    in.putExtra("important","important");
                                    startActivity(in);
                                }
                            });

                    AlertDialog alertDialog=builder.create();
                    alertDialog.setTitle("ERROR");
                    alertDialog.show();

                }

                else {
                    startActivity(new Intent(Location_Bookmark_Activity.this, Location_Bookmark_Activity2.class));
                }

            }
        });



    }
    private void LoadRecyclerView() {

        Location_Recycler.setLayoutManager(new LinearLayoutManager(Location_Bookmark_Activity.this));
       Location_Recycler.setAdapter(recycler_adapter);
    }

    private void LoadDataRecycler() {

        SharedPreferences preferences=getApplicationContext().getSharedPreferences("User_Details",0);
        final String DOC_ID=preferences.getString("Active_Group_Id","Empty");

        db.collection("GROUPS").document(DOC_ID).collection("LOCATION_BOOKMARKS").orderBy("TimeLine", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot querySnapshot:task.getResult()){

                            LocationModel modell=new LocationModel(querySnapshot.getString("Address"),querySnapshot.getDouble("Longitude"),querySnapshot.getDouble("Latitude"),querySnapshot.getString("Landmark"),querySnapshot.getString("About_Location"),querySnapshot.getString("Pincode"),querySnapshot.getString("Locality"),querySnapshot.getString("Date"));
                            addressdata.add(modell);

                        }
                        if(addressdata.size()==0){
                            Rel_Group_Gallery_no.setVisibility(View.VISIBLE);
                        }
                        else
                            Rel_Group_Gallery_no.setVisibility(View.GONE);



                        recycler_adapter=new LocationAdapter(addressdata,Location_Bookmark_Activity.this);
                        Location_Recycler.setAdapter(recycler_adapter);
                        Rel_Location.setVisibility(View.GONE);
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Location_Bookmark_Activity.this, "ERROR OCCURED TRY AGAIN", Toast.LENGTH_LONG).show();
                Rel_Location.setVisibility(View.GONE);
            }
        });


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

                                   // Toast.makeText(Location_Bookmark_Activity.this, "hello"+location.getLatitude(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(Location_Bookmark_Activity.this, "last"+mLastLocation.getLatitude(), Toast.LENGTH_SHORT).show();

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
