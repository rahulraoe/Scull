package com.err.scullapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Share_Expense_Activity extends AppCompatActivity {



    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference notebookRef;
    ShareExpenseRecycleAdapter aadapter;
    RecyclerView Share_Expense_recycler;

    String DOC_ID,Group_Name;
   CircleImageView fab;



    private ArrayList<String> credit = new ArrayList<>();
    private ArrayList<String> debit = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();
    private ArrayList<String> Number_of = new ArrayList<>();



    ArrayList<ShareExpenseModel> model;


    String coins;
    String group_members_id,Admin,new_user;

    CardView Settle_Amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share__expense_);

        model = new ArrayList<>();

           final SharedPreferences preferences=getApplicationContext().getSharedPreferences("User_Details",0);
        coins=preferences.getString("coins","0");

                Group_Name=preferences.getString("Active_Group","Group");
        Admin=preferences.getString("Admin","Hello");

        DOC_ID=preferences.getString("Active_Group_Id","Empty");



        group_members_id=preferences.getString("Active_Group_Members_Id","Empty");


        if(Admin.equalsIgnoreCase("hello")){
            final DocumentReference docRef = db.collection("GROUPS").document(DOC_ID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            pref.edit().putString("Admin",document.getString("Creator_name")).apply();
                            Admin=document.getString("Creator_name");
                            editor.apply();
                        }
                    }
                }
            });
        }
        new_user=preferences.getString("User_Name","User");

        getSupportActionBar().setTitle(Group_Name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        if(group_members_id.equalsIgnoreCase("empty")){
            db.collection("GROUPS").document(DOC_ID).collection("GROUP_MEMBERS")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    group_members_id=document.getId();
                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();
                                    pref.edit().putString("Active_Group_Members_Id",group_members_id).apply();
                                    editor.apply();
                                    LoadDataForNextActivity(DOC_ID);
                                }
                            } else {

                            }
                        }
                    });
        }
        else
            LoadDataForNextActivity(DOC_ID);





        notebookRef= db.collection("GROUPS").document(DOC_ID).collection("GROUP_MEMBERS");

        Share_Expense_recycler=(RecyclerView)findViewById(R.id.Share_Expense_recycler);
        Share_Expense_recycler.setNestedScrollingEnabled(false);
        
       // SetReyclerView();


        fab=(CircleImageView) findViewById(R.id.fab);
        Glide.with(Share_Expense_Activity.this)
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/add%20%20icon.png?alt=media&token=cea1478a-3efd-471f-99f6-854401999844")
                .into(fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coins=preferences.getString("coins","0");

                if(Integer.parseInt(coins)<50) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(Share_Expense_Activity.this);
                    builder.setMessage("You dont have 50 coins to create group.Earn coins.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent in =new Intent(Share_Expense_Activity.this,Coins_Activity.class);
                                    in.putExtra("important","important");
                                    startActivity(in);
                                }
                            });

                    AlertDialog alertDialog=builder.create();
                    alertDialog.setTitle("ERROR");
                    alertDialog.show();

                }

                else if(!Admin.equalsIgnoreCase(new_user)){
                    AlertDialog.Builder builder=new AlertDialog.Builder(Share_Expense_Activity.this);
                    builder.setMessage("You dont have permissions to add an expense.Admin "+Admin +" has to add a new expense.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).setNegativeButton("But Why?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AlertDialog.Builder builder=new AlertDialog.Builder(Share_Expense_Activity.this);
                            builder.setMessage("If anyone can add an expense it wont be a perfect accounting because anyone can add any amount of their wish.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });

                            AlertDialog alertDialog=builder.create();
                            alertDialog.setTitle("Reason");
                            alertDialog.show();
                        }
                    });

                    AlertDialog alertDialog=builder.create();
                    alertDialog.setTitle("ERROR");
                    alertDialog.show();

                }


                else {
                    Intent in = new Intent(Share_Expense_Activity.this, New_Expense_Activity.class);
                    in.putExtra("credit", credit);
                    in.putExtra("debit", debit);
                    in.putExtra("date", date);
                    in.putExtra("names", names);
                    in.putExtra("number", Number_of);
                    startActivity(in);

                }
            }
        });





        Settle_Amount=(CardView)findViewById(R.id.Settle_Amount);
        Settle_Amount.setVisibility(View.GONE);
        Settle_Amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Share_Expense_Activity.this, Settle_Expense_Activity.class);
                in.putExtra("credit", credit);
                in.putExtra("debit", debit);
                in.putExtra("names", names);
                startActivity(in);
            }
        });


    }

//    private void SetReyclerView() {
//
//
////        Query query = notebookRef;
////        FirestoreRecyclerOptions<ShareExpenseModel> options = new FirestoreRecyclerOptions.Builder<ShareExpenseModel>()
////                .setQuery(query, ShareExpenseModel.class)
////                .build();
////        aadapter = new ShareExpenseRecycleAdapter(options);
//
//        Share_Expense_recycler.setLayoutManager(new LinearLayoutManager(Share_Expense_Activity.this));
//        Share_Expense_recycler.setAdapter(aadapter);
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        aadapter.startListening();
//
//    }
//
//
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        aadapter.stopListening();
//
//    }



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


    private void LoadDataForNextActivity(String docid) {

        final DocumentReference docRef = db.collection("GROUPS").document(DOC_ID).collection("GROUP_MEMBERS").document(group_members_id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(Share_Expense_Activity.this, "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    names= (ArrayList<String>) snapshot.get("Name");
                    credit=(ArrayList<String>) snapshot.get("Credit_Amount");
                    debit=(ArrayList<String>) snapshot.get("Debit_Amount");
                    date=(ArrayList<String>) snapshot.get("Date");
                    Number_of=(ArrayList<String>) snapshot.get("Number_Of");



                    ShareExpenseRecycleAdapter adapter=new ShareExpenseRecycleAdapter(credit,debit,names,date,Number_of);

                    Share_Expense_recycler.setLayoutManager(new LinearLayoutManager(Share_Expense_Activity.this));
                    Share_Expense_recycler.setAdapter(adapter);

                } else {
                    Toast.makeText(Share_Expense_Activity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }

}
