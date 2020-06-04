package com.err.scullapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class New_Expense_Activity extends AppCompatActivity {
    private ArrayList<String> credit = new ArrayList<>();
    private ArrayList<String> debit = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();

    ArrayList<String> Number_Of=new ArrayList<>();

    NewExpenseRecycleAdapter recycler_adapter;

    RecyclerView New_Expense_Recycler;
    EditText Total_Amount,Amount_For;

    CardView Add_Expense;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__expense_);


        getSupportActionBar().setTitle("New Expense");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        New_Expense_Recycler=(RecyclerView)findViewById(R.id.New_Expense_Recycler);





        names=(ArrayList<String>) getIntent().getSerializableExtra("names");
        credit=(ArrayList<String>) getIntent().getSerializableExtra("credit");
        debit=(ArrayList<String>) getIntent().getSerializableExtra("debit");
        date=(ArrayList<String>) getIntent().getSerializableExtra("date");

        Number_Of=(ArrayList<String>) getIntent().getSerializableExtra("number");
        New_Expense_Recycler.setLayoutManager(new LinearLayoutManager(New_Expense_Activity.this));
        New_Expense_Recycler.setAdapter(recycler_adapter);

        Amount_For=(EditText)findViewById(R.id.Amount_For);

        ArrayList<String> Amount_Due=new ArrayList<>();

        Amount_Due.clear();
        for(int i=0;i<names.size();i++){
            Amount_Due.add(i,"0");

        }
        ArrayList<String> credit_amount=new ArrayList<>();
        credit_amount.clear();
        for(int i=0;i<names.size();i++){
            credit_amount.add(i,"0");
        }

        recycler_adapter=new NewExpenseRecycleAdapter(names,credit,debit,New_Expense_Activity.this,Amount_Due,credit_amount,Amount_For.getText().toString(),Number_Of);
        New_Expense_Recycler.setAdapter(recycler_adapter);

        New_Expense_Recycler.setNestedScrollingEnabled(false);

        Total_Amount=(EditText)findViewById(R.id.Total_Amount);
        Total_Amount.addTextChangedListener(watch);

        Amount_For.addTextChangedListener(watch1);

        Add_Expense=(CardView)findViewById(R.id.Add_Expense);
Add_Expense.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        recycler_adapter.AddExpense();
    }
});

    }

    TextWatcher watch = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

            if(arg0.toString().isEmpty()) {
                 ArrayList<String> Amount_Due=new ArrayList<>();
                 Amount_Due.clear();
                for(int i=0;i<names.size();i++){
                    Amount_Due.add(i,"0");
                }


                ArrayList<String> credit_amount=new ArrayList<>();
                credit_amount.clear();
                for(int i=0;i<names.size();i++){
                    credit_amount.add(i,"0");
                }

                recycler_adapter.ChangeEditText("0",Amount_Due,credit_amount);

            }
            else {
                ArrayList<String> Amount_Due=new ArrayList<>();
                Amount_Due.clear();
               double edtamount=0;
                double total=Double.parseDouble(arg0.toString());
                int size=names.size();
                String str="0";
                if(total>0) {
                    edtamount=total/size;
                    str = String.format("%.02f",edtamount);
                }

                for(int i=0;i<names.size();i++){
                    Amount_Due.add(i,str);
                }

                ArrayList<String> credit_amount=new ArrayList<>();
                credit_amount.clear();
                for(int i=0;i<names.size();i++){
                    credit_amount.add(i,"0");
                }


                recycler_adapter.ChangeEditText("0",Amount_Due,credit_amount);

                recycler_adapter.ChangeEditText(arg0.toString(),Amount_Due,credit_amount);

            }

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub


        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
            // TODO Auto-generated method stub


        }};



    TextWatcher watch1 = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub



                recycler_adapter.ChangeEditTextFor(arg0.toString());



        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub


        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
            // TODO Auto-generated method stub


        }};


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
