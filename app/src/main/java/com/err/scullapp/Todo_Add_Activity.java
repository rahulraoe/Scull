package com.err.scullapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Todo_Add_Activity extends AppCompatActivity {

    RadioButton shop,place;

    String collection="TODO_LIST";
    Button Add_Todo;

    EditText title,content;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;


    ArrayList<String> titlee=new ArrayList<>();
    ArrayList<String> contentt=new ArrayList<>();
    ArrayList<String> color=new ArrayList<>();
    ArrayList<String> date=new ArrayList<>();
    ArrayList<String> timeline=new ArrayList<>();



    ArrayList<String> titlep=new ArrayList<>();
    ArrayList<String> contentp=new ArrayList<>();
    ArrayList<String> colorp=new ArrayList<>();
    ArrayList<String> datep=new ArrayList<>();
    ArrayList<String> timelinep=new ArrayList<>();


    private ProgressDialog myProgressDialog;
    SharedPreferences preferences ;

    String coins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo__add_);

        getSupportActionBar().setTitle("Todo List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shop=(RadioButton)findViewById(R.id.shop);
        place=(RadioButton)findViewById(R.id.place);


        title=(EditText)findViewById(R.id.title);
        content=(EditText)findViewById(R.id.content);
        preferences=getApplicationContext().getSharedPreferences("User_Details",0);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

       shop.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               shop.setChecked(true);
               place.setChecked(false);
               collection="TODO_LIST";

               title.setHint("List Title");
               content.setHint("Add Items");
           }
       });

      place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                place.setChecked(true);
                shop.setChecked(false);
                collection="TODO_PLACES";


                title.setHint("Place Name");
                content.setHint("Something about the place");
            }
        });
        Add_Todo=(Button)findViewById(R.id.Add_Todo);

        coins=preferences.getString("coins","0");
        Add_Todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(title.getText().toString().isEmpty()){
                    Toast.makeText(Todo_Add_Activity.this,"Title cannot be empty", Toast.LENGTH_LONG).show();
                }
                else if(title.getText().toString().length()<3){
                    Toast.makeText(Todo_Add_Activity.this,"Title is too short", Toast.LENGTH_LONG).show();
                }
                else if(content.getText().toString().isEmpty()){
                    Toast.makeText(Todo_Add_Activity.this,"content cannot be empty", Toast.LENGTH_LONG).show();
                }
                else if(content.getText().toString().length()<5){
                    Toast.makeText(Todo_Add_Activity.this,"content is too short", Toast.LENGTH_LONG).show();
                }
                else
                {

                    if(collection.equalsIgnoreCase("TODO_LIST"))
                        AddDataList();
                    else
                        AddDataPlaces();











                }
            }
        });
    }

    private void AddDataPlaces() {

        myProgressDialog = ProgressDialog.show(Todo_Add_Activity.this,
                "HOLD ON...", "Adding todo item", true);



        if(!isOnline()){

            DocumentReference docRef = db.collection("USERS_DATA").document(user.getUid()).collection("TODO_PLACES").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {


                            titlep= (ArrayList<String>)document.get("Title");
                            contentp=(ArrayList<String>) document.get("Content");
                            colorp=(ArrayList<String>) document.get("Color");
                            datep=(ArrayList<String>) document.get("Date");
                            timelinep=(ArrayList<String>) document.get("Timeline");
                            DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
                            final String timelinee = dateFormat222.format(new Date()).toString();
                            DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                            final String datee = dateFormat2.format(new Date()).toString();


                            titlep.add(0,title.getText().toString());
                            contentp.add(0,content.getText().toString());
                            colorp.add(0,"green");
                            datep.add(0,datee);
                            timelinep.add(0,timelinee);


                            Map<String, Object> data = new HashMap<>();
                            data.put("Title", titlep);
                            data.put("Content", contentp);
                            data.put("Color", colorp);
                            data.put("Date", datep);
                            data.put("Timeline", timelinep);

                            db.collection("USERS_DATA").document(user.getUid()).collection("TODO_PLACES").document(user.getUid())
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Todo_Add_Activity.this, "Error here", Toast.LENGTH_SHORT).show();
                                            myProgressDialog.dismiss();
                                        }
                                    });


                            int c=Integer.parseInt(coins)-50;
                            SharedPreferences.Editor editor = preferences.edit();
                            preferences.edit().putString("coins",String.valueOf(c)).apply();
                            editor.apply();

                            final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid());
                            contact.update("Coins",String.valueOf(c))
                                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                                        @Override
                                        public void onSuccess(Void aVoid) {



                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    myProgressDialog.dismiss();
                                }
                            });

                            Toast.makeText(Todo_Add_Activity.this, "Succesfull", Toast.LENGTH_SHORT).show();
                            myProgressDialog.dismiss();
                            finish();


                        }
                        else
                        {



                            DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
                            final String timelinee = dateFormat222.format(new Date()).toString();


                            DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                            final String datee = dateFormat2.format(new Date()).toString();


                            titlep.add(title.getText().toString());
                            contentp.add(content.getText().toString());
                            colorp.add("green");
                            datep.add( datee);
                            timelinep.add(timelinee);


                            Map<String, Object> data = new HashMap<>();
                            data.put("Title", titlep);
                            data.put("Content", contentp);
                            data.put("Color", colorp);
                            data.put("Date", datep);
                            data.put("Timeline", timelinep);


                            db.collection("USERS_DATA").document(user.getUid()).collection("TODO_PLACES").document(user.getUid())
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Todo_Add_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            int c=Integer.parseInt(coins)-50;
                            SharedPreferences.Editor editor = preferences.edit();
                            preferences.edit().putString("coins",String.valueOf(c)).apply();
                            editor.apply();

                            final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid());
                            contact.update("Coins",String.valueOf(c))
                                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                                        @Override
                                        public void onSuccess(Void aVoid) {



                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    myProgressDialog.dismiss();
                                }
                            });

                            Toast.makeText(Todo_Add_Activity.this, "Succesfull", Toast.LENGTH_SHORT).show();
                            myProgressDialog.dismiss();
finish();

                        }

                    } else {
                        Toast.makeText(Todo_Add_Activity.this, "Error here here", Toast.LENGTH_SHORT).show();

                    }
                }
            });







        }


        else {

            DocumentReference docRef = db.collection("USERS_DATA").document(user.getUid()).collection("TODO_PLACES").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {


                            titlep= (ArrayList<String>) document.get("Title");
                            contentp = (ArrayList<String>) document.get("Content");
                            colorp = (ArrayList<String>) document.get("Color");
                            datep = (ArrayList<String>) document.get("Date");
                            timelinep = (ArrayList<String>) document.get("Timeline");


                            DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
                            final String timelinee = dateFormat222.format(new Date()).toString();


                            DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                            final String datee = dateFormat2.format(new Date()).toString();


                            titlep.add(0, title.getText().toString());
                            contentp.add(0, content.getText().toString());
                            colorp.add(0, "green");
                            datep.add(0, datee);
                            timelinep.add(0, timelinee);


                            Map<String, Object> data = new HashMap<>();
                            data.put("Title", titlep);
                            data.put("Content", contentp);
                            data.put("Color", colorp);
                            data.put("Date", datep);
                            data.put("Timeline", timelinep);

                            int c=Integer.parseInt(coins)-50;
                            SharedPreferences.Editor editor = preferences.edit();
                            preferences.edit().putString("coins",String.valueOf(c)).apply();
                            editor.apply();

                            final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid());
                            contact.update("Coins",String.valueOf(c))
                                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                                        @Override
                                        public void onSuccess(Void aVoid) {



                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    myProgressDialog.dismiss();
                                }
                            });




                            db.collection("USERS_DATA").document(user.getUid()).collection("TODO_PLACES").document(user.getUid())
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Todo_Add_Activity.this, "Succesfull", Toast.LENGTH_SHORT).show();
                                            myProgressDialog.dismiss();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Todo_Add_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {



                            DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
                            final String timelinee = dateFormat222.format(new Date()).toString();


                            DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                            final String datee = dateFormat2.format(new Date()).toString();


                            titlep.add(title.getText().toString());
                            contentp.add(content.getText().toString());
                            colorp.add("green");
                            datep.add( datee);
                            timelinep.add(timelinee);


                            Map<String, Object> data = new HashMap<>();
                            data.put("Title", titlep);
                            data.put("Content", contentp);
                            data.put("Color", colorp);
                            data.put("Date", datep);
                            data.put("Timeline", timelinep);


                            int c=Integer.parseInt(coins)-50;
                            SharedPreferences.Editor editor = preferences.edit();
                            preferences.edit().putString("coins",String.valueOf(c)).apply();
                            editor.apply();

                            final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid());
                            contact.update("Coins",String.valueOf(c))
                                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                                        @Override
                                        public void onSuccess(Void aVoid) {



                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    myProgressDialog.dismiss();
                                }
                            });


                            db.collection("USERS_DATA").document(user.getUid()).collection("TODO_PLACES").document(user.getUid())
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Todo_Add_Activity.this, "Succesfull", Toast.LENGTH_SHORT).show();
                                            myProgressDialog.dismiss();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Todo_Add_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });





                        }
                    } else {
                        Toast.makeText(Todo_Add_Activity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }






    }
























    private void AddDataList() {

        myProgressDialog = ProgressDialog.show(Todo_Add_Activity.this,
                "HOLD ON...", "Adding todo item", true);



        if(!isOnline()){






            DocumentReference docRef = db.collection("USERS_DATA").document(user.getUid()).collection("TODO_LIST").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {


                            titlee= (ArrayList<String>)document.get("Title");
                            contentt=(ArrayList<String>) document.get("Content");
                            color=(ArrayList<String>) document.get("Color");
                            date=(ArrayList<String>) document.get("Date");
                            timeline=(ArrayList<String>) document.get("Timeline");



                            DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
                            final String timelinee = dateFormat222.format(new Date()).toString();


                            DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                            final String datee = dateFormat2.format(new Date()).toString();


                            titlee.add(0,title.getText().toString());
                            contentt.add(0,content.getText().toString());
                            color.add(0,"green");
                            date.add(0,datee);
                            timeline.add(0,timelinee);


                            Map<String, Object> data = new HashMap<>();
                            data.put("Title", titlee);
                            data.put("Content", contentt);
                            data.put("Color", color);
                            data.put("Date", date);
                            data.put("Timeline", timeline);


                            int c=Integer.parseInt(coins)-50;
                            SharedPreferences.Editor editor = preferences.edit();
                            preferences.edit().putString("coins",String.valueOf(c)).apply();
                            editor.apply();

                            final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid());
                            contact.update("Coins",String.valueOf(c))
                                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                                        @Override
                                        public void onSuccess(Void aVoid) {



                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    myProgressDialog.dismiss();
                                }
                            });





                            db.collection("USERS_DATA").document(user.getUid()).collection("TODO_LIST").document(user.getUid())
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Todo_Add_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            Toast.makeText(Todo_Add_Activity.this, "Succesfull", Toast.LENGTH_SHORT).show();
                            myProgressDialog.dismiss();
                            finish();

                        } else {





                            DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
                            final String timelinee = dateFormat222.format(new Date()).toString();


                            DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                            final String datee = dateFormat2.format(new Date()).toString();


                            titlee.add( title.getText().toString());
                            contentt.add(content.getText().toString());
                            color.add("green");
                            date.add(datee);
                            timeline.add(timelinee);


                            Map<String, Object> data = new HashMap<>();
                            data.put("Title", titlee);
                            data.put("Content", contentt);
                            data.put("Color", color);
                            data.put("Date", date);
                            data.put("Timeline", timeline);

                            int c=Integer.parseInt(coins)-50;
                            SharedPreferences.Editor editor = preferences.edit();
                            preferences.edit().putString("coins",String.valueOf(c)).apply();
                            editor.apply();

                            final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid());
                            contact.update("Coins",String.valueOf(c))
                                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                                        @Override
                                        public void onSuccess(Void aVoid) {



                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    myProgressDialog.dismiss();
                                }
                            });

                            db.collection("USERS_DATA").document(user.getUid()).collection("TODO_LIST").document(user.getUid())
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Todo_Add_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                            Toast.makeText(Todo_Add_Activity.this, "Succesfull", Toast.LENGTH_SHORT).show();
                            myProgressDialog.dismiss();
                            finish();



                        }
                    } else {
                        Toast.makeText(Todo_Add_Activity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                }
            });







        }


        else {

            DocumentReference docRef = db.collection("USERS_DATA").document(user.getUid()).collection("TODO_LIST").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {


                            titlee = (ArrayList<String>) document.get("Title");
                            contentt = (ArrayList<String>) document.get("Content");
                            color = (ArrayList<String>) document.get("Color");
                            date = (ArrayList<String>) document.get("Date");
                            timeline = (ArrayList<String>) document.get("Timeline");


                            DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
                            final String timelinee = dateFormat222.format(new Date()).toString();


                            DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                            final String datee = dateFormat2.format(new Date()).toString();


                            titlee.add(0, title.getText().toString());
                            contentt.add(0, content.getText().toString());
                            color.add(0, "green");
                            date.add(0, datee);
                            timeline.add(0, timelinee);


                            Map<String, Object> data = new HashMap<>();
                            data.put("Title", titlee);
                            data.put("Content", contentt);
                            data.put("Color", color);
                            data.put("Date", date);
                            data.put("Timeline", timeline);
                            int c=Integer.parseInt(coins)-50;
                            SharedPreferences.Editor editor = preferences.edit();
                            preferences.edit().putString("coins",String.valueOf(c)).apply();
                            editor.apply();

                            final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid());
                            contact.update("Coins",String.valueOf(c))
                                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                                        @Override
                                        public void onSuccess(Void aVoid) {



                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    myProgressDialog.dismiss();
                                }
                            });



                            db.collection("USERS_DATA").document(user.getUid()).collection("TODO_LIST").document(user.getUid())
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Todo_Add_Activity.this, "Succesfull", Toast.LENGTH_SHORT).show();
                                            myProgressDialog.dismiss();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Todo_Add_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {






                            DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
                            final String timelinee = dateFormat222.format(new Date()).toString();


                            DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                            final String datee = dateFormat2.format(new Date()).toString();


                            titlee.add( title.getText().toString());
                            contentt.add(content.getText().toString());
                            color.add("green");
                            date.add(datee);
                            timeline.add(timelinee);


                            Map<String, Object> data = new HashMap<>();
                            data.put("Title", titlee);
                            data.put("Content", contentt);
                            data.put("Color", color);
                            data.put("Date", date);
                            data.put("Timeline", timeline);

                            int c=Integer.parseInt(coins)-50;
                            SharedPreferences.Editor editor = preferences.edit();
                            preferences.edit().putString("coins",String.valueOf(c)).apply();
                            editor.apply();

                            final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid());
                            contact.update("Coins",String.valueOf(c))
                                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                                        @Override
                                        public void onSuccess(Void aVoid) {



                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    myProgressDialog.dismiss();
                                }
                            });




                            db.collection("USERS_DATA").document(user.getUid()).collection("TODO_LIST").document(user.getUid())
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Todo_Add_Activity.this, "Succesfull", Toast.LENGTH_SHORT).show();
                                            myProgressDialog.dismiss();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Todo_Add_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });




                        }
                    } else {
                        Toast.makeText(Todo_Add_Activity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                }
            });


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



    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

}
