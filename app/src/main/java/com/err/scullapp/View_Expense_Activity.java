package com.err.scullapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class View_Expense_Activity extends AppCompatActivity {

    String total_credit,total_debit,number,date,name,Group_Name;
TextView no_of,total_amount,total_amount_for,date_text,total_heading_for;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__expense_);
        name=getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        SharedPreferences preferences=getApplicationContext().getSharedPreferences("User_Details",0);

        Group_Name=preferences.getString("Active_Group","Group");





        total_credit=getIntent().getStringExtra("total_credit");
        number=getIntent().getStringExtra("number");
        total_debit=getIntent().getStringExtra("total_debit");
        date=getIntent().getStringExtra("date");



        no_of=(TextView)findViewById(R.id.no_of);
        total_amount=(TextView)findViewById(R.id.total_amount);
        total_amount_for=(TextView)findViewById(R.id.total_amount_for);
        date_text=(TextView)findViewById(R.id.date_text);
        total_heading_for=(TextView)findViewById(R.id.total_heading_for);

        total_heading_for.setText("Total amount spent for "+name);
        no_of.setText(number);
        total_amount.setText(total_credit);
        total_amount_for.setText(total_debit);
        date_text.setText(name+" joined group "+Group_Name+ " on "+date);


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
