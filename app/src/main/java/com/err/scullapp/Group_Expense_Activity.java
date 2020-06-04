package com.err.scullapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Group_Expense_Activity extends AppCompatActivity {
    MyGroupActivityRecyclerAdapter recycler_adapter;

    RelativeLayout Rel_My_Groups;
    RecyclerView My_Groups_Recycler;
    ArrayList<My_Group_Model> model;

    private ArrayList<String> date= new ArrayList<>();
    private ArrayList<String> text= new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView no_expense;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group__expense_);

        My_Groups_Recycler=(RecyclerView)findViewById(R.id.Group_Activity_Recycler);

        Rel_My_Groups=(RelativeLayout)findViewById(R.id.Rel_Group_Activity);
        no_expense=(TextView)findViewById(R.id.no_expense);
        no_expense.setVisibility(View.GONE);

        model=new ArrayList<>();

        getSupportActionBar().setTitle("Group Expenses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        RetrieveData();
        LoadRecyclerView();




    }

    private void LoadRecyclerView() {
        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        //My_Groups_Recycler.setLayoutManager(staggeredGridLayoutManager);
        My_Groups_Recycler.setLayoutManager(new LinearLayoutManager(Group_Expense_Activity.this));
        My_Groups_Recycler.setAdapter(recycler_adapter);
    }

    private void RetrieveData() {

        SharedPreferences preferences=getApplicationContext().getSharedPreferences("User_Details",0);

        String docid=preferences.getString("Active_Group_Id","Empty");

        db.collection("GROUPS").document(docid).collection("GROUP_EXPENSES").orderBy("TimeLine", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot querySnapshot:task.getResult()){

                            My_Group_Model modell=new My_Group_Model(querySnapshot.getString("Date"),querySnapshot.getString("text"));
                            model.add(modell);

                        }

                        for (int i = 0; i < model.size(); i++) {

                            date.add(model.get(i).getDate_acti());
                            text.add(model.get(i).getText_acti());

                        }

                        if(model.size()==0)
                            no_expense.setVisibility(View.VISIBLE);


                        recycler_adapter=new MyGroupActivityRecyclerAdapter(date,text,Group_Expense_Activity.this);
                        My_Groups_Recycler.setAdapter(recycler_adapter);
                        Rel_My_Groups.setVisibility(View.GONE);
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Group_Expense_Activity.this, "ERROR OCCURED TRY AGAIN", Toast.LENGTH_LONG).show();
                Rel_My_Groups.setVisibility(View.GONE);
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
