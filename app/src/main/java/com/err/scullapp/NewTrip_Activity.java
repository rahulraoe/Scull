package com.err.scullapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NewTrip_Activity extends AppCompatActivity {
    RecyclerView friends_recycler;
    private ArrayList<String> names = new ArrayList<>();
    EditText add_friends_et,Group_Password,trip_name;
    NewTripRecycleAdapter adapter;
    Button  create_group;
    private ProgressDialog myProgressDialog;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String MAIN_DOC_ID;
    int countt=0;
    String var;
     String user_name_str;

    private FirebaseAuth mAuth;
    String Group_Members_Id="";

    String coins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip_);

        getSupportActionBar().setTitle("Create Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        friends_recycler=(RecyclerView)findViewById(R.id.friends_recycler);
        add_friends_et=(EditText)findViewById(R.id.add_friends_et);
        Group_Password=(EditText)findViewById(R.id.Group_Password);
        trip_name=(EditText)findViewById(R.id.trip_name);
        create_group=(Button)findViewById(R.id.create_group);
        adapter = new NewTripRecycleAdapter(names);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        user_name_str=pref.getString("User_Name","User");
        coins=pref.getString("coins","0");


        if(Integer.parseInt(coins)<150) {
            AlertDialog.Builder builder=new AlertDialog.Builder(NewTrip_Activity.this);
            builder.setMessage("You dont have 150 coins to create group.Earn coins.")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent in =new Intent(NewTrip_Activity.this,Coins_Activity.class);
                            in.putExtra("important","important");
                            startActivity(in);
                        }
                    });

            AlertDialog alertDialog=builder.create();
            alertDialog.setTitle("ERROR");
            alertDialog.show();

        }


        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        friends_recycler.setAdapter(adapter);
        friends_recycler.setLayoutManager(new LinearLayoutManager(NewTrip_Activity.this));
        friends_recycler.setHasFixedSize(true);
        add_friends_et.addTextChangedListener(watch);
        add_friends_et.setImeOptions(EditorInfo.IME_ACTION_DONE);
        add_friends_et.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {



                    String nam  = add_friends_et.getText().toString();
                    boolean isnumber = true;

                    isnumber = nam.matches("-?\\d+(\\.\\d+)?");
                    if(isnumber){
                        Toast.makeText(NewTrip_Activity.this, "Name cannot be number", Toast.LENGTH_LONG).show();
                    }
                    else {
                        nam = nam.substring(0, 1).toUpperCase() + nam.substring(1).toLowerCase();
                        addToRecycler(nam);
                    }
                    add_friends_et.getText().clear();
                    InputMethodManager inputManager = (InputMethodManager)NewTrip_Activity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(NewTrip_Activity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);


                    return true;
                }
                return false;
            }
        });


        create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                coins=pref.getString("coins","0");

                if(trip_name.getText().toString().isEmpty()){
                    AlertDialog.Builder builder=new AlertDialog.Builder(NewTrip_Activity.this);
                    builder.setMessage("ENTER TRIP NAME.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                    AlertDialog alertDialog=builder.create();
                    alertDialog.setTitle("ERROR");
                    alertDialog.show();
                }
                else if(Group_Password.getText().toString().isEmpty()){
                    AlertDialog.Builder builder=new AlertDialog.Builder(NewTrip_Activity.this);
                    builder.setMessage("ENTER GROUP PASSWORD.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                    AlertDialog alertDialog=builder.create();
                    alertDialog.setTitle("ERROR");
                    alertDialog.show();
                }
else if(names.size()==0){
    AlertDialog.Builder builder=new AlertDialog.Builder(NewTrip_Activity.this);
    builder.setMessage("ADD FRIENDS TO CREATE A GROUP.")
            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

    AlertDialog alertDialog=builder.create();
    alertDialog.setTitle("ERROR");
    alertDialog.show();
}
else if(Integer.parseInt(coins)<150) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(NewTrip_Activity.this);
                    builder.setMessage("You dont have 150 coins to create group.Earn coins.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent in =new Intent(NewTrip_Activity.this,Coins_Activity.class);
                                    in.putExtra("important","important");
                                    startActivity(in);
                                }
                            });

                    AlertDialog alertDialog=builder.create();
                    alertDialog.setTitle("ERROR");
                    alertDialog.show();

                }


else{

                    myProgressDialog = ProgressDialog.show(NewTrip_Activity.this,
                            "HOLD ON...", "Creating group "+trip_name.getText().toString(), true);
                    //Toast.makeText(NewTrip_Activity.this, "hello"+trip_creator_name.getText().toString()+trip_name.getText().toString(), Toast.LENGTH_SHORT).show();

                    DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                    final String date = dateFormat2.format(new Date()).toString();

                    DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
                    final String timeline = dateFormat222.format(new Date()).toString();



                    Random r = new Random( System.currentTimeMillis() );
                   String in= String.valueOf(((1 + r.nextInt(2)) * 10000 + r.nextInt(10000)));


                    final Map<String, Object> data = new HashMap<>();
                    data.put("Creator_name", user_name_str);
                    data.put("Group_name", trip_name.getText().toString());
                    data.put("Group_Password",Group_Password.getText().toString());
                    data.put("Group_Id",in);
                    data.put("Date", date);
                    data.put("TimeLine",timeline);
                    data.put("Group_Count",String.valueOf(names.size()+1));
                    data.put("Group_Pic_Url","https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/group%20icon.png?alt=media&token=c4197ca4-be9d-4401-959a-8f24a25d4baa");


                    db.collection("GROUPS")
                            .add(data)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Map<String, Object> data1 = new HashMap<>();
                                    data1.put("Date", date);
                                    data1.put("text",user_name_str+" created group "+trip_name.getText().toString()+".");
                                    data1.put("TimeLine",timeline);
                                    data1.put("Group_Count",String.valueOf(names.size()+1));

                                    MAIN_DOC_ID=documentReference.getId();

                                    final Map<String, Object> data2 = new HashMap<>();
                                    data2.put("Creator_name", user_name_str);
                                    data2.put("Group_name", trip_name.getText().toString());
                                    data2.put("Group_Password",Group_Password.getText().toString());
                                    data2.put("Date", date);
                                    data2.put("TimeLine",timeline);
                                    data2.put("Group_Count",String.valueOf(names.size()+1));
                                    data2.put("Group_Pic_Url","https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/group%20icon.png?alt=media&token=c4197ca4-be9d-4401-959a-8f24a25d4baa");
                                    data2.put("Group_Doc_Id",MAIN_DOC_ID);











                                    db.collection("GROUPS").document(documentReference.getId()).collection("GROUP_ACTIVITY")
                                            .add(data1)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                   Toast.makeText(NewTrip_Activity.this,"Group created successfully",Toast.LENGTH_SHORT).show();

                                                   CreateGroupMates(MAIN_DOC_ID);


                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    AlertDialog.Builder builder=new AlertDialog.Builder(NewTrip_Activity.this);
                                                    builder.setMessage("Group not created please try again later.")
                                                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    dialogInterface.cancel();
                                                                }
                                                            });

                                                    AlertDialog alertDialog=builder.create();
                                                    alertDialog.setTitle("ERROR");
                                                    alertDialog.show();

                                                }
                                            });




                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("rahul", "Error adding document", e);
                                    AlertDialog.Builder builder=new AlertDialog.Builder(NewTrip_Activity.this);
                                    builder.setMessage("Group not created please try again later.")
                                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            });

                                    AlertDialog alertDialog=builder.create();
                                    alertDialog.setTitle("ERROR");
                                    alertDialog.show();
                                }
                            });





                }


            }
        });



    }

    public void CreateGroupMates(final String string){
        new Thread(new Runnable(){
            public void run() {



                 ArrayList<String> credit = new ArrayList<>();
                 ArrayList<String> debit = new ArrayList<>();
                 ArrayList<String> Names_Db = new ArrayList<>();
                 ArrayList<String> dates = new ArrayList<>();
                ArrayList<String> number_of = new ArrayList<>();


                countt=0;
                Map<String, Object> mem = new HashMap<>();
                DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                final String date = dateFormat2.format(new Date()).toString();
                for(int i=-1;i<names.size();i++){

                    if(i==-1) {
                        Names_Db.add(user_name_str);
                        credit.add("0");
                        debit.add("0");
                        dates.add(date);
                        number_of.add("0");

                    }
                    else {
                        Names_Db.add(names.get(i));
                        credit.add("0");
                        debit.add("0");
                        dates.add(date);
                        number_of.add("0");
                        countt++;
                    }

                }//for loop end


//process dialog dismiss

                mem.put("Date", dates);
                mem.put("Credit_Amount", credit);
                mem.put("Debit_Amount",debit);
                mem.put("Name",Names_Db);
                mem.put("Number_Of",number_of);
                db.collection("GROUPS").document(string).collection("GROUP_MEMBERS")
                        .add(mem)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Group_Members_Id=documentReference.getId();




                                DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
                                final String timeline = dateFormat222.format(new Date()).toString();



                                final Map<String, Object> data2 = new HashMap<>();
                                data2.put("Creator_name", user_name_str);
                                data2.put("Group_name", trip_name.getText().toString());
                                data2.put("Group_Password",Group_Password.getText().toString());
                                data2.put("Date", date);
                                data2.put("TimeLine",timeline);
                                data2.put("Group_Count",String.valueOf(names.size()+1));
                                data2.put("Group_Pic_Url","https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/group%20icon.png?alt=media&token=c4197ca4-be9d-4401-959a-8f24a25d4baa");
                                data2.put("Group_Doc_Id",MAIN_DOC_ID);
                                data2.put("Group_Members_Doc_Id",Group_Members_Id);


                                final FirebaseUser user = mAuth.getCurrentUser();







                                db.collection("USERS_DATA").document(user.getUid()).collection("MY_GROUPS")
                                        .add(data2)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
//////////////////////new group created////////////////////////////////


                                                SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
                                                SharedPreferences.Editor editor = pref.edit();
                                                int c=Integer.parseInt(coins)-150;
                                                pref.edit().putString("coins",String.valueOf(c)).apply();
                                                editor.apply();
                                                final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid());
                                                contact.update("Coins",String.valueOf(c))
                                                        .addOnSuccessListener(new OnSuccessListener< Void >() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {







                                                                Intent intent = new Intent(NewTrip_Activity.this, View_Group_Activity.class);
                                                                intent.putExtra("Group_Name",trip_name.getText().toString());
                                                                intent.putExtra("Doc_Id",MAIN_DOC_ID);
                                                                SharedPreferences pref =getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
                                                                SharedPreferences.Editor editor = pref.edit();
                                                                pref.edit().putString("Active_Group",trip_name.getText().toString()).apply();
                                                                pref.edit().putString("Active_Group_Members_Id",Group_Members_Id).apply();
                                                                pref.edit().putString("Active_Group_Id",MAIN_DOC_ID).apply();
                                                                pref.edit().putString("Active_Group_Creator",user_name_str).apply();
                                                                pref.edit().putString("Active_group_Pic_Url","https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/group%20icon.png?alt=media&token=c4197ca4-be9d-4401-959a-8f24a25d4baa").apply();
                                                                pref.edit().putString("Active_group_Date",date).apply();
                                                                pref.edit().putString("Admin",user_name_str).apply();
                                                                editor.apply();
                                                                myProgressDialog.dismiss();
                                                                startActivity(intent);
                                                                finish();

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });





                GroupActivity();
            }
        }).start();
    }

    private void GroupActivity() {

        for(int i=0;i<names.size();i++){
            DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
            final String date = dateFormat2.format(new Date()).toString();

            DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
            final String timeline = dateFormat222.format(new Date()).toString();
            Map<String, Object> data2 = new HashMap<>();

            data2.put("Date", date);
            data2.put("text",user_name_str+" added "+names.get(i)+".");
            data2.put("TimeLine",timeline);

            db.collection("GROUPS").document(MAIN_DOC_ID).collection("GROUP_ACTIVITY")
                    .add(data2)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                        }
                    });
        }
    }


    private void addToRecycler(String str) {
        int count=0;

        for(int i=0;i<names.size();i++){
            if(names.get(i).equalsIgnoreCase(str)){
                count++;
            }
        }
        if(str.equalsIgnoreCase(user_name_str))
            count++;
        if(str.isEmpty()){
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if(count>0){

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage(str+" already exists.Enter new name.")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alertDialog=builder.create();
            alertDialog.setTitle("ERROR");
            alertDialog.show();
        }
        else{
            names.add(str);
            adapter.notifyDataSetChanged();

        }

    }




    TextWatcher watch = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

           

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
