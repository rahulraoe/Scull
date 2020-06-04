package com.err.scullapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class View_Group_Activity extends AppCompatActivity {

    String Group_Name,DOC_ID,Creator_Name,Group_Date;

    CircleImageView Group_Image;
    TextView Created_By;
    ImageView firstimg,secondimg,thirdimg,sixthimg,seventhimg,eigthimg;
    RecyclerView New_Expense_Recycler;


    RelativeLayout No_Active_Groups_Rel, Main_Rel;
    CardView Split_Expense_Card,Group_Activity_Card,Gallery_Card,Tickets_Card,Location_Card,Todo_Card;

    ImageView fourthimg,fifthimg;

    CardView share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__group_);


        DOC_ID= getIntent().getStringExtra("Doc_Id");
        Group_Name= getIntent().getStringExtra("Group_Name");
        getSupportActionBar().setTitle(Group_Name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Group_Image=(CircleImageView)findViewById(R.id.Group_Image);
        Created_By=(TextView)findViewById(R.id.Created_By);


        No_Active_Groups_Rel = (RelativeLayout) findViewById(R.id.No_Active_Groups_Rel);
        Main_Rel = (RelativeLayout) findViewById(R.id.Main_Rel);


        SharedPreferences preferences=getApplicationContext().getSharedPreferences("User_Details",0);

        Creator_Name=preferences.getString("Active_Group_Creator","User");

        Group_Date=preferences.getString("Active_group_Date","Date");
        String url=preferences.getString("Active_group_Pic_Url","https://firebasestorage.googleapis.com/v0/b/scullapp-1cb15.appspot.com/o/iconfinder_users_1902261.png?alt=media&token=fddfa706-6881-4609-b7f9-b7f276de7ced");
        Glide.with(View_Group_Activity.this)
                .load(url)
.into(Group_Image);

        Created_By.setText("Created by "+Creator_Name+" on "+Group_Date+".");


        fourthimg=(ImageView)findViewById(R.id.fourthimg);
        fifthimg=(ImageView)findViewById(R.id.fifthimg);
        Glide.with(View_Group_Activity.this)
                .load("https://firebasestorage.googleapis.com/v0/b/scullapp-1cb15.appspot.com/o/ICONS%2Fgallery.png?alt=media&token=68b35973-0b9f-4a84-9983-29afd4844978")
                .into(fourthimg);

        Glide.with(View_Group_Activity.this)
                .load("https://firebasestorage.googleapis.com/v0/b/scullapp-1cb15.appspot.com/o/ICONS%2Flocation.png?alt=media&token=d2043ccc-cbe3-44f3-8e85-daf250a75be7")
                .into(fifthimg);


        Split_Expense_Card = (CardView) findViewById(R.id.Split_Expense_Card);
        Split_Expense_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(View_Group_Activity.this,Share_Expense_Activity.class);

                startActivity(in);
            }
        });


        Group_Activity_Card=(CardView)findViewById(R.id.Group_Activity_Card);
        Group_Activity_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(View_Group_Activity.this,Group_Activity_Activity.class));


            }
        });


        fourthimg=(ImageView)findViewById(R.id.fourthimg);
        fifthimg=(ImageView)findViewById(R.id.fifthimg);





        firstimg=(ImageView)findViewById(R.id. firstimg);
        secondimg=(ImageView)findViewById(R.id.secondimg);
        thirdimg=(ImageView)findViewById(R.id.thirdimg);
        sixthimg=(ImageView)findViewById(R.id.sixthimg);
        seventhimg=(ImageView)findViewById(R.id.seventhimg);
        eigthimg=(ImageView)findViewById(R.id.eigthimg);









        Glide.with(View_Group_Activity.this)
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/split%20icon.png?alt=media&token=1846ed05-19f9-4a31-ae35-d2986717a3f0")
                .into(firstimg);
        Glide.with(View_Group_Activity.this)
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/tickets%20%20icon.png?alt=media&token=5b016276-b8a6-49b6-8e52-08a769f08685")
                .into(secondimg);
        Glide.with(View_Group_Activity.this)
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/Itinerary.png?alt=media&token=2bde12ee-0280-4e2d-97b8-42f8821d3116")
                .into(thirdimg);

        Glide.with(View_Group_Activity.this)
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/gallery%20icon%20(1).png?alt=media&token=bccf08ee-b53a-4436-9328-89abfe03fd7a")
                .into(fourthimg);

        Glide.with(View_Group_Activity.this)
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/location%20icon.png?alt=media&token=c48b0b60-b6a3-4792-a5fd-d4d9bc34aa23")
                .into(fifthimg);


        Glide.with(View_Group_Activity.this)
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/contacts%20icon%20(1).png?alt=media&token=615da667-31f8-4381-a1dc-a37d2da44201")
                .into(sixthimg);
        Glide.with(View_Group_Activity.this)
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/Group_Activity.png?alt=media&token=d85ab81a-19f1-4e82-9f4b-00b86841dc06")
                .into(seventhimg);
        Glide.with(View_Group_Activity.this)
                //.load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/group_feedback.png?alt=media&token=1336f38b-dfae-47c6-b188-91f7a55d0293")
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/earn%20a%20%20icon.png?alt=media&token=d45d2ead-a4f1-4515-921f-2b8c2edf81e3")
                .into(eigthimg);



        Todo_Card=(CardView)findViewById(R.id.Todo_Card);
        Todo_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(View_Group_Activity.this,Todo_Activity.class));
            }
        });







        if (Group_Name.equalsIgnoreCase("No_Active_Group")) {
            No_Active_Groups_Rel.setVisibility(View.VISIBLE);
            Main_Rel.setVisibility(View.INVISIBLE);

        } else {
            No_Active_Groups_Rel.setVisibility(View.INVISIBLE);
            Main_Rel.setVisibility(View.VISIBLE);
            Created_By.setText("Created by " + Creator_Name + " on " + Group_Date + ".");
        }


        // LoadDataForNextActivity(DOC_ID);

        Gallery_Card=(CardView)findViewById(R.id.Gallery_Card);
        Gallery_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(View_Group_Activity.this,Group_Gallery_Activity.class));
            }
        });


        Tickets_Card=(CardView)findViewById(R.id.Tickets_Card);
        Tickets_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(View_Group_Activity.this,Bills_Tickets_Activity.class));
            }
        });


        Location_Card=(CardView)findViewById(R.id.Location_Card);
        Location_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(View_Group_Activity.this,Location_Bookmark_Activity.class));
            }
        });


        share=(CardView)findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(View_Group_Activity.this,Share_Group_Activity.class));
            }
        });


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
