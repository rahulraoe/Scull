package com.err.scullapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Join_Group_Activity extends AppCompatActivity {

    EditText Group_Id,Group_Password;
    Button Join_Group;

    String ID,PASSWORD;

    FirebaseFirestore  db=FirebaseFirestore.getInstance();
    int count=0;

    String Group_Members_Id;

    String group_name,group_id,group_creator,group_url,group_date;

    private ProgressDialog myProgressDialog;
    String coins;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join__group_);

        Group_Id=(EditText)findViewById(R.id.Group_Id);


        Group_Password=(EditText)findViewById(R.id.Group_Password);
        getSupportActionBar().setTitle("Join Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();

Join_Group=(Button)findViewById(R.id.Join_Group);


        final SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
        coins=pref.getString("coins","0");
        if(Integer.parseInt(coins)<150) {
            AlertDialog.Builder builder=new AlertDialog.Builder(Join_Group_Activity.this);
            builder.setMessage("You dont have 150 coins to create group.Earn coins.")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent in =new Intent(Join_Group_Activity.this,Coins_Activity.class);
                            in.putExtra("important","important");
                            startActivity(in);
                        }
                    });

            AlertDialog alertDialog=builder.create();
            alertDialog.setTitle("ERROR");
            alertDialog.show();

        }





        Join_Group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                coins=pref.getString("coins","0");

                ID=Group_Id.getText().toString();
                PASSWORD=Group_Password.getText().toString();
                if(ID.isEmpty()){
                    Toast.makeText(Join_Group_Activity.this, "Please enter room id", Toast.LENGTH_SHORT).show();
                }
                else if(PASSWORD.isEmpty()){
                    Toast.makeText(Join_Group_Activity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(coins)<150) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(Join_Group_Activity.this);
                    builder.setMessage("You dont have 150 coins to create group.Earn coins.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent in =new Intent(Join_Group_Activity.this,Coins_Activity.class);
                                    in.putExtra("important","important");
                                    startActivity(in);
                                }
                            });

                    AlertDialog alertDialog=builder.create();
                    alertDialog.setTitle("ERROR");
                    alertDialog.show();

                }
                else
                {

                    myProgressDialog = ProgressDialog.show(Join_Group_Activity.this,
                            "HOLD ON...", "Joining Group", true);



                    db.collection("GROUPS")
                            .whereEqualTo("Group_Id", ID)
                            .whereEqualTo("Group_Password",PASSWORD)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful()) {

                                        QuerySnapshot docu = task.getResult();
                                        if (docu.isEmpty()) {
                                            AlertDialog.Builder builder=new AlertDialog.Builder(Join_Group_Activity.this);
                                            builder.setMessage("Id or password is incorrect.Please try again.")
                                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.cancel();
                                                        }
                                                    });

                                            AlertDialog alertDialog=builder.create();
                                            alertDialog.setTitle("ERROR");
                                            alertDialog.show();
                                            myProgressDialog.dismiss();
                                        }
                                        for (QueryDocumentSnapshot document : task.getResult()) {

//getting group members id
                                            db.collection("GROUPS").document(document.getId()).collection("GROUP_MEMBERS")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    Group_Members_Id=document.getId();
                                                                }
                                                            } else {

                                                            }
                                                        }
                                                    });








                                            DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                                            final String date = dateFormat2.format(new Date()).toString();

                                            DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
                                            final String timeline = dateFormat222.format(new Date()).toString();


                                            final Map<String, Object> data2 = new HashMap<>();
                                            data2.put("Creator_name",document.getString("Creator_name"));
                                            data2.put("Group_name",document.getString("Group_name"));
                                            data2.put("Group_Password",document.getString("Group_Password"));
                                            data2.put("Date", date);
                                            data2.put("TimeLine",timeline);
                                            data2.put("Group_Count",document.getString("Group_Count"));
                                            data2.put("Group_Pic_Url",document.getString("'Group_Pic_Url"));
                                            data2.put("Group_Doc_Id",document.getId());
                                            data2.put("Group_Members_Doc_Id",Group_Members_Id);

                                            mAuth = FirebaseAuth.getInstance();
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            group_id=document.getId();
                                            group_creator=document.getString("Creator_name");
                                            group_url=document.getString("'Group_Pic_Url");
                                            group_date=document.getString("Date");

                                            group_name=document.getString("Group_name");








                                            db.collection("USERS_DATA").document(user.getUid()).collection("MY_GROUPS")
                                                    .add(data2)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {




                                                            DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                                                            final String date = dateFormat2.format(new Date()).toString();
                                                            SharedPreferences preferences=getApplication().getSharedPreferences("User_Details",0);
                                                            DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
                                                            final String timeline = dateFormat222.format(new Date()).toString();
                                                            Map<String, Object> data2 = new HashMap<>();

                                                            data2.put("Date", date);
                                                            data2.put("text",preferences.getString("User_Name","User")+" Joined group.");
                                                            data2.put("TimeLine",timeline);





                                                            //group activity

                                                            db.collection("GROUPS").document(group_id).collection("GROUP_ACTIVITY")
                                                                    .add(data2)
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {

                                                                            final Intent intent = new Intent(Join_Group_Activity.this, View_Group_Activity.class);
                                                                            intent.putExtra("Group_Name",group_name);
                                                                            intent.putExtra("Doc_Id",group_id);
                                                                            SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
                                                                            SharedPreferences.Editor editor = pref.edit();
                                                                            pref.edit().putString("Active_Group",group_name).apply();
                                                                            pref.edit().putString("Active_Group_Members_Id",Group_Members_Id).apply();
                                                                            pref.edit().putString("Active_Group_Id",group_id).apply();
                                                                            pref.edit().putString("Active_Group_Creator",group_creator).apply();
                                                                            pref.edit().putString("Active_group_Pic_Url",group_url).apply();
                                                                            pref.edit().putString("Active_group_Date",group_date).apply();
                                                                            pref.edit().putString("Admin",group_creator).apply();
                                                                            editor.apply();
                                                                            final FirebaseUser user = mAuth.getCurrentUser();

                                                                            int c=Integer.parseInt(coins)-150;
                                                                            pref.edit().putString("coins",String.valueOf(c)).apply();
                                                                            editor.apply();

                                                                            final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid());
                                                                            contact.update("Coins",String.valueOf(c))
                                                                                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {





                                                                                            myProgressDialog.dismiss();
                                                                                            // Toast.makeText(con, "rahul"+group_members_doc_id.get(position), Toast.LENGTH_SHORT).show();
                                                                                            startActivity(intent);
                                                                                        }
                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
myProgressDialog.dismiss();
                                                                                }
                                                                            });



                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(Join_Group_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                                                    myProgressDialog.dismiss();
                                                                        }
                                                                    });









                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(Join_Group_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                                    myProgressDialog.dismiss();
                                                        }
                                                    });














                                        }
                                    } else {

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Join_Group_Activity.this, "no document", Toast.LENGTH_SHORT).show();
                            myProgressDialog.dismiss();
                        }
                    });

                }


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
