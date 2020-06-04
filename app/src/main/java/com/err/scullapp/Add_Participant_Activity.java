package com.err.scullapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Add_Participant_Activity extends AppCompatActivity {
    private ArrayList<String> credit = new ArrayList<>();
    private ArrayList<String> debit = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();
    private ArrayList<String> Number_of = new ArrayList<>();


    FirebaseFirestore db=FirebaseFirestore.getInstance();

    String docc_id,group_doc_id;

    EditText participant;
    Button add;
    String user_name;
    private ProgressDialog myProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__participant_);


        getSupportActionBar().setTitle("Add Participant");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        add=(Button)findViewById(R.id.add);
        participant=(EditText)findViewById(R.id.participant);
        SharedPreferences preferences=getApplicationContext().getSharedPreferences("User_Details",0);
        docc_id=preferences.getString("Active_Group_Id","Empty");
        group_doc_id=preferences.getString("Active_Group_Members_Id","Empty");
        user_name= preferences.getString("User_Name","User");

        db.collection("GROUPS").document(docc_id).collection("GROUP_MEMBERS").document(group_doc_id)
                .get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                if(snapshot!=null){
                    names= (ArrayList<String>) snapshot.get("Name");
                    credit=(ArrayList<String>) snapshot.get("Credit_Amount");
                    debit=(ArrayList<String>) snapshot.get("Debit_Amount");
                    date=(ArrayList<String>) snapshot.get("Date");
                    Number_of=(ArrayList<String>) snapshot.get("Number_Of");
                }
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count=0;
                String nam  = participant.getText().toString();
                boolean isnumber = true;

                isnumber = nam.matches("-?\\d+(\\.\\d+)?");
                for(int r=0;r<names.size();r++){
                    if(names.get(r).equalsIgnoreCase(participant.getText().toString())){
                        count++;
                    }
                }
                if(participant.getText().toString().isEmpty()){
                    Toast.makeText(Add_Participant_Activity.this, "Please Add participant", Toast.LENGTH_LONG).show();
                }


                else if(isnumber){
                    Toast.makeText(Add_Participant_Activity.this, "Name cannot be number", Toast.LENGTH_LONG).show();
                }

                else if(count>0) {

                    Toast.makeText(Add_Participant_Activity.this, participant.getText().toString() +" already Exists.", Toast.LENGTH_SHORT).show();

                }
                else{
                    myProgressDialog = ProgressDialog.show(Add_Participant_Activity.this,
                            "HOLD ON...", "Adding new member "+participant.getText().toString(), true);
                    DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                    final String datee = dateFormat2.format(new Date()).toString();
                    nam = nam.substring(0, 1).toUpperCase() + nam.substring(1).toLowerCase();
                    names.add(nam);
                    credit.add("0");
                    debit.add("0");
                    Number_of.add("0");
                    date.add(datee);

                    final DocumentReference contact1 = db.collection("GROUPS").document(docc_id);
                    String si= String.valueOf(names.size());
                    contact1.update("Group_Count",si)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });

                    final DocumentReference contact = db.collection("GROUPS").document(docc_id).collection("GROUP_MEMBERS").document(group_doc_id);;
                    contact.update("Credit_Amount", credit);
                    contact.update("Number_Of", Number_of);
                    contact.update("Date", date);
                    contact.update("Name",names);
                    contact.update("Debit_Amount", debit)
                            .addOnSuccessListener(new OnSuccessListener< Void >() {
                                @Override
                                public void onSuccess(Void aVoid) {





                                    DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
                                    final String date = dateFormat2.format(new Date()).toString();

                                    DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
                                    final String timeline = dateFormat222.format(new Date()).toString();
                                    Map<String, Object> data2 = new HashMap<>();

                                    data2.put("Date", date);
                                    data2.put("text",user_name+" added "+participant.getText().toString());
                                    data2.put("TimeLine",timeline);

                                    db.collection("GROUPS").document(docc_id).collection("GROUP_ACTIVITY")
                                            .add(data2)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    myProgressDialog.dismiss();


                                                    AlertDialog.Builder builder=new AlertDialog.Builder(Add_Participant_Activity.this);


                                                    builder.setMessage("New member added successfully")
                                                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    finish();
                                                                }
                                                            });
                                                    AlertDialog alertDialog=builder.create();
                                                    alertDialog.setTitle("ERROR");
                                                    alertDialog.show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {


                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Add_Participant_Activity.this, "error"+e, Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
