package com.err.scullapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Coins_Activity extends AppCompatActivity implements RewardedVideoAdListener {

    Button Reward_Button;
    String coins;
    private RewardedVideoAd mRewardedVideoAd;
    private ProgressDialog myProgressDialog;
    private FirebaseAuth mAuth;

    int updated_coins;

    String important="not_important";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coins_);

        important=getIntent().getStringExtra("important");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Claim Rewards");

        Reward_Button=(Button)findViewById(R.id.Reward_Button);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
        coins=pref.getString("coins","0");
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);


//native ad
        AdLoader.Builder builderr= new AdLoader.Builder(
                this, "ca-app-pub-3940256099942544/2247696110");

        builderr.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                TemplateView template = findViewById(R.id.my_template);
                template.setNativeAd(unifiedNativeAd);
            }
        });

        AdLoader adLoader = builderr.build();
        adLoader.loadAd(new AdRequest.Builder().build());




        if(Integer.parseInt(coins)<5000){
            LoadRewardAd();
        }
        else
        {
            final AlertDialog.Builder builder=new AlertDialog.Builder(Coins_Activity.this);
            builder.setMessage("You have more than 5000 coins.Can't claim more coins.")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alertDialog=builder.create();
            alertDialog.setTitle("ERROR");
            alertDialog.show();
        }


        Reward_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myProgressDialog = ProgressDialog.show(Coins_Activity.this,
                        "HOLD ON...", "Loading Ad", true);

if(!isOnline()){
                    Toast.makeText(Coins_Activity.this, "No Internet", Toast.LENGTH_LONG).show();
                    myProgressDialog.dismiss();
                }
else if(Integer.parseInt(coins)>5000)
{
    myProgressDialog.dismiss();
    final AlertDialog.Builder builder=new AlertDialog.Builder(Coins_Activity.this);
    builder.setMessage("You have more than 5000 coins.Can't claim more coins.")
            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

    AlertDialog alertDialog=builder.create();
    alertDialog.setTitle("ERROR");
    alertDialog.show();

}
                else if (mRewardedVideoAd.isLoaded()) {
                    myProgressDialog.dismiss();
                    mRewardedVideoAd.show();
                }

                else
                {

                    Toast.makeText(Coins_Activity.this, "Loading Please Wait", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void LoadRewardAd() {

        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {

        myProgressDialog.dismiss();
        mRewardedVideoAd.show();


    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        if(important.equalsIgnoreCase("important"))
            updated_coins=Integer.parseInt(coins)+150;
        else
            updated_coins=Integer.parseInt(coins)+50;

        pref.edit().putString("coins",String.valueOf(updated_coins)).apply();
        editor.apply();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid());
        contact.update("Coins",String.valueOf(updated_coins))
                .addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Coins_Activity.this, "Reward Added", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

        AlertDialog.Builder builder=new AlertDialog.Builder(Coins_Activity.this);
        builder.setMessage("Loading Ad failed please try again later.")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog=builder.create();
        alertDialog.setTitle("ERROR");
        alertDialog.show();

    }

    @Override
    public void onRewardedVideoCompleted() {

    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
