package com.err.scullapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Settle_Expense_Activity extends AppCompatActivity {
    private ArrayList<String> credit = new ArrayList<>();

    private ArrayList<String> debit = new ArrayList<>();

    private ArrayList<String> names = new ArrayList<>();
     ArrayList<String> list=new ArrayList<>();
    int size=0;
    TextView demo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle__expense_);



        getSupportActionBar().setTitle("Settle Expenses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        demo=(TextView)findViewById(R.id.demo);



        credit=(ArrayList<String>) getIntent().getSerializableExtra("credit");
        debit=(ArrayList<String>) getIntent().getSerializableExtra("debit");
        names=(ArrayList<String>) getIntent().getSerializableExtra("names");


        size=credit.size();
        double credit1[]=new double[size];
        String names1[]=new String[size];

        for(int i=0;i<size;i++){

            double d=Double.parseDouble(credit.get(i))-Double.parseDouble(debit.get(i));
            if(d==0.00)
                credit1[i]=0.98;
            else
            credit1[i]=d;

            names1[i]=names.get(i);
        }



        double temp=0;
        String s;
        for(int i=0;i<names1.length;i++)
        {
            for(int j=i+1;j<names1.length;j++)
            {
                if(credit1[i]>credit1[j])
                {
                    s=names1[i];
                    names1[i]=names1[j];
                    names1[j]=s;

                    temp=credit1[i];
                    credit1[i]=credit1[j];
                    credit1[j]=temp;
                }
            }
        }


        int sum = 0;
        int n = credit1.length;
        printSubsets(credit1, n, sum,names1);


        Set<String> set1= new HashSet<>(list);
        list.clear();
        list.addAll(set1);


        String abc="";
        for(int i=0;i<list.size();i++)
        {
abc=abc+list.get(i)+"\n";



        }
demo.setText(abc);


    }

    public  void printSubsets(double[] set, int n, int sum,String[] abc) {

        double ans[]=new double[100];
        String ans1[]=new String[100];
        String hello[]=new String[n];

        int k=0;
        int h=0;


        int totalSubSets = (1 << n);
        for (int i = 1; i < totalSubSets; ++i) {

            // loop over all possible subsets

            double curSum = 0;
            for (int j = n - 1; j >= 0; --j) {
                if(set[j]==9.9898)
                    continue;
                if (((i >> j) & 1) > 0) { // if bit at jth position is 1 take that value
                    curSum +=set[j];
                }
            }
            int count=0;

            if (curSum == sum) { // valid subset found, then print it

                ans[k]=00;
                ans1[k]="hello";
                k++;

                for (int j = n - 1; j >= 0; --j) {
                    // looping in reverse order to print set in decreasing order
                    count=0;
                    if (((i >> j) & 1) > 0) { // if bit at jth position is 1 take that value
                        if(set[j]!=0){

                            ans[k]=set[j];
                            ans1[k]=abc[j];


                            k++;

                            count++;

                        }



                    }
                }

            }
        }





        double[] rahul=new double[k];
        String[] rao=new String[k];
        int r=0;

        ans1[k]="hello";
        ans[k]=0.0;
        k++;





        for(int i=1;i<k;i++)
        {


            if(ans1[i].equals("hello")){
                r=0;

                for(int j=1;j<i;j++){

                    ans1[i]="over";
                    if(!ans1[j].equals("over")){
                        rahul[r]=ans[j];
                        rao[r]=ans1[j];
                        r++;

                        ans1[j]="over";
                        ans[j]=0.0;

                    }


                }

                printMethod(rahul,rao,r);









            }









        }



    }

    private  void printMethod(double[] ra,String[] roo,int d) {



        String[] result=new String[d];
        int k=0;





        for(int i=1;i<d;i++)
        {
            if((ra[d-i]<0)){
                result[k]=roo[d-i]+" owes "+Math.abs(ra[d-i])+" to "+roo[0];
                list.add(result[k]);


                k++;

            }

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
