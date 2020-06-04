package com.err.scullapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class View_Gallery_Item_Activity extends AppCompatActivity {

TextView Memory_Text,Image_Details;

String pic_url,memory_text_string,text;
   ImageView Group_Gallery_Image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__gallery__item_);


        getSupportActionBar().setTitle("Group Memories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pic_url=getIntent().getStringExtra("Pic_Url");
        memory_text_string=getIntent().getStringExtra("memory");
        text=getIntent().getStringExtra("text");

        Group_Gallery_Image=(ImageView) findViewById(R.id.Group_Gallery_Image);



        Memory_Text=(TextView)findViewById(R.id.Memory_Text);
        Image_Details=(TextView)findViewById(R.id.Image_Details);
        // Return Image's base 64 code


        // ImageViewZoomConfig

        Glide.with(View_Gallery_Item_Activity.this)
                .load(pic_url)
                .into(Group_Gallery_Image);

        Memory_Text.setText(memory_text_string);


        Image_Details.setText(text);




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
