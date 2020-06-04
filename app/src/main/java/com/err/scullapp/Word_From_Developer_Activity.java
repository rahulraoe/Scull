package com.err.scullapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class Word_From_Developer_Activity extends AppCompatActivity {

    TextView word;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word__from__developer_);

        getSupportActionBar().setTitle("Word From Developer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        word=(TextView)findViewById(R.id.word);
        word.setText("Hey,this is rahul developer of scull.We often think that we are very clever and " +
                "are capable of remembering everything that we did before 6 months or a year." +
                "But this is actually not true especially when it comes to accounting,remembering " +
                "dates and pin pointing exact locations.The main motivation behind building this " +
                "app is to remember and cherish the most memorable occassions with our " +
                "family or friends.Scull is a secure box of memories that will take you to" +
                " the past memories with just a click.We often feel shy when it comes to accounting" +
                "with friends(Personal opinion) scull provides a way to remove our shy " +
                "and provide a perfect ledger for expenses.We often want to revisit locations " +
                "that make us happy and want to share the location with friends but we forget " +
                "that location.Scull helps you to pin point exact location with longitude and latitude" +
                "and remeber a memory with that specific location.Check out other features " +
                "in the app.I hope you like my hardwork,leave " +
                "a playstore rating to make me improve the app.Cheers!");

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
