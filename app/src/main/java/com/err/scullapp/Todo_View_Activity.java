package com.err.scullapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class Todo_View_Activity extends AppCompatActivity {

    String titlee,contentt,datee,color;

    TextView title,content,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo__view_);



        titlee=getIntent().getStringExtra("title");
        contentt=getIntent().getStringExtra("content");
        datee=getIntent().getStringExtra("date");
        color=getIntent().getStringExtra("color");

        getSupportActionBar().setTitle(titlee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        title=(TextView)findViewById(R.id.title);
        content=(TextView)findViewById(R.id.content);
        date=(TextView)findViewById(R.id.date);

        title.setText(titlee);
        content.setText(contentt);
        date.setText("Created on "+datee);



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
