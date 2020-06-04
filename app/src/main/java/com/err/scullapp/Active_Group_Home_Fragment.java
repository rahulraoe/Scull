package com.err.scullapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Active_Group_Home_Fragment extends Fragment {
   String Group_Name, DOC_ID, Creator_Name, Group_Date;

    CircleImageView Group_Image;
    RelativeLayout No_Active_Groups_Rel, Main_Rel;

    TextView Created_By;

    CardView Split_Expense_Card,Group_Activity_Card,Gallery_Card,Tickets_Card,Location_Card,Earn_Coins,Todo_Card;
    private CollectionReference notebookRef;




    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    CardView share;

    public Active_Group_Home_Fragment() {
        // Required empty public constructor
    }


    ImageView fourthimg,fifthimg;

    ImageView firstimg,secondimg,thirdimg,sixthimg,seventhimg,eigthimg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_active__group__home_, container, false);


        Group_Image = (CircleImageView) view.findViewById(R.id.Group_Image);
        No_Active_Groups_Rel = (RelativeLayout) view.findViewById(R.id.No_Active_Groups_Rel);
        Main_Rel = (RelativeLayout) view.findViewById(R.id.Main_Rel);
        Created_By = (TextView) view.findViewById(R.id.Created_By);
        SharedPreferences preferences = getContext().getSharedPreferences("User_Details", 0);
        Group_Name = preferences.getString("Active_Group", "No_Active_Group");
        Creator_Name = preferences.getString("Active_Group_Creator", "User");



        Group_Date = preferences.getString("Active_group_Date", "Date");


        DOC_ID = preferences.getString("Active_Group_Id", "Empty");
        notebookRef = db.collection("GROUPS").document(DOC_ID).collection("GROUP_MEMBERS");





        fifthimg=(ImageView)view.findViewById(R.id.fifthimg);




        firstimg=(ImageView)view.findViewById(R.id. firstimg);
        secondimg=(ImageView)view.findViewById(R.id.secondimg);
        thirdimg=(ImageView)view.findViewById(R.id.thirdimg);
        fourthimg=(ImageView)view.findViewById(R.id.fourthimg);

        sixthimg=(ImageView)view.findViewById(R.id.sixthimg);
        seventhimg=(ImageView)view.findViewById(R.id.seventhimg);
        eigthimg=(ImageView)view.findViewById(R.id.eigthimg);



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






        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/split%20icon.png?alt=media&token=1846ed05-19f9-4a31-ae35-d2986717a3f0")
                .into(firstimg);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/tickets%20%20icon.png?alt=media&token=5b016276-b8a6-49b6-8e52-08a769f08685")
                .into(secondimg);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/Itinerary.png?alt=media&token=2bde12ee-0280-4e2d-97b8-42f8821d3116")
                .into(thirdimg);

        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/gallery%20icon%20(1).png?alt=media&token=bccf08ee-b53a-4436-9328-89abfe03fd7a")
                .into(fourthimg);

        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/location%20icon.png?alt=media&token=c48b0b60-b6a3-4792-a5fd-d4d9bc34aa23")
                .into(fifthimg);


        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/contacts%20icon%20(1).png?alt=media&token=615da667-31f8-4381-a1dc-a37d2da44201")
                .into(sixthimg);
        Glide.with(getContext())
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/Group_Activity.png?alt=media&token=d85ab81a-19f1-4e82-9f4b-00b86841dc06")
                .into(seventhimg);
        Glide.with(getContext())
                //
                // .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/group_feedback.png?alt=media&token=1336f38b-dfae-47c6-b188-91f7a55d0293")
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/earn%20a%20%20icon.png?alt=media&token=d45d2ead-a4f1-4515-921f-2b8c2edf81e3")
                .into(eigthimg);





//        Glide.with(getContext())
//                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/pencil%20icon.png?alt=media&token=a5349bab-d651-470c-88b7-c401d06d0335")
//                .into(Profile_Edit);

        if (Group_Name.equalsIgnoreCase("No_Active_Group")) {
            No_Active_Groups_Rel.setVisibility(View.VISIBLE);
            Main_Rel.setVisibility(View.INVISIBLE);

        } else {
            No_Active_Groups_Rel.setVisibility(View.INVISIBLE);
            Main_Rel.setVisibility(View.VISIBLE);
            Created_By.setText("Created by " + Creator_Name + " on " + Group_Date + ".");
        }


        String url = preferences.getString("Active_group_Pic_Url", "https://firebasestorage.googleapis.com/v0/b/scullapp-1cb15.appspot.com/o/iconfinder_users_1902261.png?alt=media&token=fddfa706-6881-4609-b7f9-b7f276de7ced");
        Glide.with(getContext())
                .load(url)
                .into(Group_Image);


String active_group=preferences.getString("Active_Group","No_Active_Group");
        if(!active_group.equalsIgnoreCase("No_Active_Group")) {
            final DocumentReference docRef = db.collection("GROUPS").document(preferences.getString("Active_Group_Id", "Empty"));
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (snapshot != null && snapshot.exists()) {
                        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("User_Details", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        pref.edit().putString("Active_Group", snapshot.getString("Group_name")).apply();

                        String url = snapshot.getString("Group_Pic_Url");
                        pref.edit().putString("Active_group_Pic_Url",url).apply();
                        Glide.with(getContext())
                                .load(url)
                                .into(Group_Image);

                        editor.apply();
                    } else {

                    }
                }
            });

        }

        Split_Expense_Card = (CardView) view.findViewById(R.id.Split_Expense_Card);
        Split_Expense_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(getContext(),Share_Expense_Activity.class);

                startActivity(in);
            }
        });


        Group_Activity_Card=(CardView)view.findViewById(R.id.Group_Activity_Card);
        Group_Activity_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(getContext(),Group_Activity_Activity.class));


            }
        });

        // LoadDataForNextActivity(DOC_ID);

        Gallery_Card=(CardView)view.findViewById(R.id.Gallery_Card);
        Gallery_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),Group_Gallery_Activity.class));





            }
        });


        Tickets_Card=(CardView)view.findViewById(R.id.Tickets_Card);
        Tickets_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),Bills_Tickets_Activity.class));
            }
        });


        Location_Card=(CardView)view.findViewById(R.id.Location_Card);
        Location_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),Location_Bookmark_Activity.class));
            }
        });



        Earn_Coins=(CardView)view.findViewById(R.id.Earn_Coins);
        Earn_Coins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Intent in=new Intent(getContext(),Coins_Activity.class);
                in.putExtra("important","not_important");
                startActivity(in);
            }
        });


        Todo_Card=(CardView)view.findViewById(R.id.Todo_Card);
        Todo_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),Todo_Activity.class));
            }
        });

        share=(CardView)view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),Share_Group_Activity.class));
            }
        });


        return view;
    }



    }

