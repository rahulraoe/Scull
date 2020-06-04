package com.err.scullapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home_Fragment extends Fragment {

    CardView My_Groups_Card,Create_Group_Card,Join_Group_Card;

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private InterstitialAd mInterstitialAd;

    ImageView firstimg,secondimg,thirdimg,fourthimg;
    public Home_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View view=inflater.inflate(R.layout.fragment_home_, container, false);


       My_Groups_Card=(CardView)view.findViewById(R.id.My_Groups_Card);
       My_Groups_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),My_Groups_Activity.class));
            }
        });
        My_Groups_Card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getContext(),"Click to view your groups",Toast.LENGTH_LONG).show();
                return false;
            }
        });
        MobileAds.initialize(getContext(),
                "ca-app-pub-3940256099942544~3347511713");
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        firstimg=(ImageView)view.findViewById(R.id. firstimg);
        secondimg=(ImageView)view.findViewById(R.id.secondimg);
        thirdimg=(ImageView)view.findViewById(R.id.thirdimg);
        fourthimg=(ImageView)view.findViewById(R.id.fourthimg);


        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/scull_my_groups.png?alt=media&token=34fbc5c9-e6a7-4928-8166-f409456d15b2")
                .into(firstimg);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/scull_create_group.png?alt=media&token=2a6741ce-92a2-4ddc-b0b9-b959a159532d")
                .into(secondimg);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/scull_join_groups.png?alt=media&token=d5cb2673-c7f4-4edd-abba-acd9b472651c")
                .into(thirdimg);

        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/scull_feedback.png?alt=media&token=09b527a6-8f39-4c07-80f9-0b1bce99364b")
                .into(fourthimg);


        AdLoader.Builder builder = new AdLoader.Builder(
                getContext(), "ca-app-pub-3940256099942544/2247696110");

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                TemplateView template = view.findViewById(R.id.my_template);
                template.setNativeAd(unifiedNativeAd);
            }
        });

        AdLoader adLoader = builder.build();
        adLoader.loadAd(new AdRequest.Builder().build());




        Create_Group_Card=(CardView)view.findViewById(R.id.Create_Group_Card);
        Create_Group_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    startActivity(new Intent(getContext(),NewTrip_Activity.class));
                }


            }
        });
        Create_Group_Card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(getContext(), "Click to create new group", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        Join_Group_Card=(CardView)view.findViewById(R.id.Join_Group_Card);
        Join_Group_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),Join_Group_Activity.class));
            }
        });

        Join_Group_Card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getContext(),"Click to join new group",Toast.LENGTH_LONG).show();
                return false;
            }
        });




       return  view;


    }

}
