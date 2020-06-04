package com.err.scullapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


public class RegisterActivity extends AppCompatActivity  {
    private FirebaseAuth mAuth;
    EditText user_name,user_email,user_password;
    Button register;
    TextView terms;

    private ProgressDialog myProgressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RelativeLayout male_rel,female_rel;
    CircleImageView Male_Image,Female_Image;

    String GENDER="male";


    String FEMALE="https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/female%20icon%20(1).png?alt=media&token=4589c2db-fc36-4be5-9b34-591c0f6ae4a5";
    String MALE="https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/male%20icon%20(1).png?alt=media&token=981d1327-1765-44bb-8078-bc1cd0896778";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        user_name=(EditText)findViewById(R.id.user_name);

        user_email=(EditText)findViewById(R.id.user_email);

        user_password=(EditText)findViewById(R.id.user_password);
        male_rel=(RelativeLayout)findViewById(R.id.male_rel);
        female_rel=(RelativeLayout)findViewById(R.id.female_rel);

        Male_Image=(CircleImageView)findViewById(R.id.Male_Image);
        Female_Image=(CircleImageView)findViewById(R.id.Female_Image);


        Resources res = getResources(); //resource handle
        Drawable drawable = res.getDrawable(R.drawable.image_corner); //new Image that was added to the res folder
        male_rel.setBackground(drawable);
        Glide
                .with(RegisterActivity.this)
                .load(FEMALE)
                .centerCrop()
                .into(Female_Image);



        Glide
                .with(RegisterActivity.this)
                .load(MALE)
                .centerCrop()
                .into(Male_Image);
        Male_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                female_rel.setBackground(null);
                Resources res = getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.image_corner); //new Image that was added to the res folder
                male_rel.setBackground(drawable);
                GENDER="male";
            }
        });

        Female_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male_rel.setBackground(null);
                Resources res = getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.image_corner); //new Image that was added to the res folder
                female_rel.setBackground(drawable);
                GENDER="female";
            }
        });

        register=(Button)findViewById(R.id.register);
        terms=(TextView)findViewById(R.id.terms);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,SignIn_Activity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isnumber = true;

                isnumber = user_name.getText().toString().matches("-?\\d+(\\.\\d+)?");

                if(user_name.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this, "User name cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(isnumber){
                    Toast.makeText(RegisterActivity.this, "Name cannot be a number", Toast.LENGTH_SHORT).show();
                }
                else if(!isValid(user_email.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                }
                else if(user_password.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(user_password.getText().toString().length()<6){
                    Toast.makeText(RegisterActivity.this, "Password should be greater than 6 characters", Toast.LENGTH_SHORT).show();
                }

                else if(!isOnline()){
                    Toast.makeText(RegisterActivity.this, "No internet", Toast.LENGTH_LONG).show();
                }
                else
                {

                    myProgressDialog = ProgressDialog.show(RegisterActivity.this,
                            "HOLD ON...", "Registering "+user_name.getText().toString()+" with Scull", true);

                    final Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(user_email.getText().toString(), user_password.getText().toString())
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.clear().apply();
                                        pref.edit().putString("User_Name", user_name.getText().toString()).apply();
                                        pref.edit().putString("User_Email", user_email.getText().toString()).apply();
                                        pref.edit().putString("Active_Group","No_Active_Group").apply();
                                        pref.edit().putString("Active_Group_Id","Empty").apply();
                                        pref.edit().putString("coins","350").apply();
                                        editor.apply();
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        final String token_id= FirebaseInstanceId.getInstance().getToken();
                                        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy hh.mm aa");
                                        final String    date = dateFormat2.format(new Date()).toString();
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("Name", user_name.getText().toString());
                                        data.put("Email", user_email.getText().toString());
                                        data.put("Last_Login_Date", date);
                                        data.put("First_Login_Date",date);
                                        data.put("UID",user.getUid());
                                        data.put("Token_Id",token_id);
                                        data.put("Gender",GENDER);
                                        data.put("Coins","350");
                                        if(GENDER.equalsIgnoreCase("male")) {
                                            data.put("Profile_Url", MALE);
                                            pref.edit().putString("User_Profile_Url",MALE).apply();
                                            editor.apply();
                                        }
else {
                                            data.put("Profile_Url", FEMALE);
                                            pref.edit().putString("User_Profile_Url", FEMALE).apply();
                                            editor.apply();
                                        }





                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(user_name.getText().toString())
                                                .build();
                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                        }
                                                    }
                                                });






                                        db.collection("USERS_DATA").document(user.getUid())
                                                .set(data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                        myProgressDialog.dismiss();
                                                        startActivity(new Intent(RegisterActivity.this,Home_Activity.class));
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        myProgressDialog.dismiss();
                                                        Toast.makeText(RegisterActivity.this, "Registeration Failed"+e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });






                                    }


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                        myProgressDialog.dismiss();

                                        String str=e.getMessage();
                                    assert str != null;
                                    if(str.contains("use by another account")) {
                                        AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setMessage("Email id already exists.Want to login?")
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        startActivity(new Intent(RegisterActivity.this,SignIn_Activity.class));
                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                       dialogInterface.cancel();
                                                    }
                                                });

                                        AlertDialog alertDialog=builder.create();
                                        alertDialog.setTitle("ERROR");
                                        alertDialog.show();

                                    }
                                    else
                                        Toast.makeText(RegisterActivity.this, "Registeration Failed" + e.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            });
                }
            }
        });
    }






    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
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
