package com.err.scullapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private View navHeader;

    FrameLayout frameLayout;

    String user_name_str;
    FirebaseUser user;
    String active_group;

    private AdView mAdView;

    String coins;
    int app_open;
    TextView User_coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //setting main fragment
        frameLayout = (FrameLayout) findViewById(R.id.fragment_area);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        coins=pref.getString("coins","0");
        final SharedPreferences preferences=getApplicationContext().getSharedPreferences("User_Details",0);
app_open= Integer.parseInt(preferences.getString("App_Open","0"));





        if(Integer.parseInt(coins)<120) {
            androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(Home_Activity.this);
            builder.setMessage("You dont have enough coins.Earn coins?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent in =new Intent(Home_Activity.this,Coins_Activity.class);
                            in.putExtra("not_important","important");
                            startActivity(in);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });


            androidx.appcompat.app.AlertDialog alertDialog=builder.create();
            alertDialog.setTitle("ERROR");
            alertDialog.show();

        }
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        active_group=pref.getString("Active_Group","No_Active_Group");
        user_name_str=pref.getString("User_Name","User");



        if(user_name_str.equalsIgnoreCase("User")) {


            final DocumentReference docRef = db.collection("USERS_DATA").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            pref.edit().putString("User_Name", document.getString("Name")).apply();
                            pref.edit().putString("User_Email", document.getString("Email")).apply();
                            pref.edit().putString("User_Profile_Url",document.getString("Group_Pic_Url")).apply();
                            editor.apply();
                        }
                    }
                }
            });

        }

        if(active_group.equalsIgnoreCase("No_Active_Group")) {
            SetGoodMorningMessage();
            Home_Fragment fragment1 = new Home_Fragment();
            setFragment(fragment1);

        }
        else
        {
Active_Group_Home_Fragment active_group_home_fragment=new Active_Group_Home_Fragment();
setFragment(active_group_home_fragment);

SetGroupName();
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


//        Toast.makeText(Home_Activity.this, "Successfull"+user.getUid(), Toast.LENGTH_SHORT).show();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        final LayoutInflater factory = getLayoutInflater();










        final View hamsburgerview = factory.inflate(R.layout.nav_header_home_, null);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        TextView user_name=(TextView)navHeader.findViewById(R.id.user_name);
        TextView user_email=(TextView)navHeader.findViewById(R.id.User_Email);

        User_coins=(TextView)navHeader.findViewById(R.id.User_coins);
        User_coins.setText(coins+" coins");

        user_name.setText(pref.getString("User_Name","User"));
        user_email.setText(pref.getString("User_Email","email@gmail.com"));


        CircleImageView user_image=(CircleImageView)navHeader.findViewById(R.id.User_Image);

        String url=pref.getString("User_Profile_Url","https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/male%20icon.png?alt=media&token=df78a7ee-78d9-46d7-a88d-0a7b7c4d0e8c");

        Glide
                .with(Home_Activity.this)
                .load(url)

                .into(user_image);


        if(app_open>5){
            androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(Home_Activity.this);
            builder.setMessage("Love using scull app?Please rate us.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                preferences.edit().putString("App_Open", "0").apply();
                                editor.apply();
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                preferences.edit().putString("App_Open", "0").apply();
                                editor.apply();
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    preferences.edit().putString("App_Open", "2").apply();
                    editor.apply();
                    dialogInterface.cancel();
                }
            });


            androidx.appcompat.app.AlertDialog alertDialog=builder.create();
            alertDialog.setTitle("Rate Us");
            alertDialog.show();
        }

    }

    private void SetGroupName() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
        getSupportActionBar().setTitle(pref.getString("Active_Group","User"));

    }

    private void SetGoodMorningMessage() {
        if(user_name_str.equalsIgnoreCase("User")){
            user_name_str=user.getDisplayName();
        }
        String o = user_name_str.substring(0, 1).toUpperCase() + user_name_str.substring(1);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {

            getSupportActionBar().setTitle("Morning , " + o);


        } else if (timeOfDay >= 12 && timeOfDay < 16) {


            getSupportActionBar().setTitle("Afternoon , " + o);
        } else if (timeOfDay >= 16 && timeOfDay < 21) {

            getSupportActionBar().setTitle("Evening , " + o);

        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            getSupportActionBar().setTitle("Evening , " + o);

        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogLayout = inflater.inflate(R.layout.ad_banner_layout, null);


            mAdView = dialogLayout.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            builder.setView(dialogLayout);
                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit App")
                    .setMessage("Are you sure you want to exit app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();



        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if(!active_group.equalsIgnoreCase("No_Active_Group")) {
            getMenuInflater().inflate(R.menu.home_, menu);

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement




        if(!active_group.equalsIgnoreCase("No_Active_Group")) {

            if (id == R.id.expense) {
                Intent in=new Intent(Home_Activity.this,Group_Expense_Activity.class);
                startActivity(in);
                return true;
            }
            else if (id == R.id.add) {
                Intent in=new Intent(Home_Activity.this,Add_Participant_Activity.class);
                startActivity(in);
                return true;
            }



            else if (id == R.id.change) {
                if(Integer.parseInt(coins)<75) {
                    androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(Home_Activity.this);
                    builder.setMessage("You dont have 75 coins to change profile pic.Earn coins.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent in =new Intent(Home_Activity.this,Coins_Activity.class);
                                    in.putExtra("important","important");
                                    startActivity(in);
                                }
                            });

                    androidx.appcompat.app.AlertDialog alertDialog=builder.create();
                    alertDialog.setTitle("ERROR");
                    alertDialog.show();

                }

                else {
                    Intent in = new Intent(Home_Activity.this, Change_Profile_Activity.class);
                    startActivity(in);

                }

                return true;
            }


        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            // Handle the camera action

                SetGoodMorningMessage();
                Home_Fragment fragment1=new Home_Fragment();
            setFragment(fragment1);

        } else if (id == R.id.nav_gallery) {
//            BlankFragment frag=new BlankFragment();
//            setFragment(frag);



            if(active_group.equalsIgnoreCase("No_Active_Group")) {
                SetGoodMorningMessage();


            }
            else
            {


                SetGroupName();

            }


            Active_Group_Home_Fragment active_group_home_fragment=new Active_Group_Home_Fragment();
            setFragment(active_group_home_fragment);




        } else if (id == R.id.logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogLayout = inflater.inflate(R.layout.ad_banner_layout, null);


            mAdView = dialogLayout.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            builder.setView(dialogLayout);
            builder.setIcon(R.drawable.logout)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                final SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0);
                                pref.edit().clear().apply();
                                pref.edit().clear().commit();

                            }
                            catch (Exception e){

                            }
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(Home_Activity.this,RegisterActivity.class));
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

        }
        else if (id == R.id.rate) {

            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }

        }  else if (id == R.id.nav_share) {

            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String text="Hey install scull app it is very productive"+"Install app from playstore\n"+
                        "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(shareIntent, "Choose One"));
            } catch(Exception e) {
                Toast.makeText(Home_Activity.this, "Error", Toast.LENGTH_SHORT).show();
            }

        }

        else if (id == R.id.word) {
            Intent in=new Intent(Home_Activity.this,Word_From_Developer_Activity.class);
            startActivity(in);
            return true;
        }
        else if (id == R.id.privacy) {
            Intent in=new Intent(Home_Activity.this,Privacy_Policy_Activity.class);
            startActivity(in);
            return true;
        }
        else if (id == R.id.nav_send) {

        }

//        if(fragment==null){
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.fragment_area, fragment);
//            ft.commit();
//        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment home_fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_area,home_fragment);
        fragmentTransaction.commit();

    }




//    @Override
//    protected void onRestart() {
//
//        if(active_group.equalsIgnoreCase("No_Active_Group")) {
//            SetGoodMorningMessage();
//            Home_Fragment fragment1 = new Home_Fragment();
//            setFragment(fragment1);
//
//        }
//        else
//        {
//            SetGroupName();
//            Active_Group_Home_Fragment active_group_home_fragment=new Active_Group_Home_Fragment();
//            setFragment(active_group_home_fragment);
//
//        }
//
//        super.onRestart();
//    }


    @Override
    protected void onRestart() {
          SharedPreferences preferences=getApplicationContext().getSharedPreferences("User_Details",0);
          User_coins.setText(preferences.getString("coins","0"));
          super.onRestart();
    }
}
