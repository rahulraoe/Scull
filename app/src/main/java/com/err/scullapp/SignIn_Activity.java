package com.err.scullapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class SignIn_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText user_email,user_password;
    Button register;
    TextView terms,forgot_password;
    private ProgressDialog myProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_);

        getSupportActionBar().setTitle("Sign In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        user_email=(EditText)findViewById(R.id.user_email);

        user_password=(EditText)findViewById(R.id.user_password);

        register=(Button)findViewById(R.id.register);
        terms=(TextView)findViewById(R.id.terms);
        forgot_password=(TextView)findViewById(R.id.forgot_password);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignIn_Activity.this);
                LayoutInflater inflater = SignIn_Activity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.edit_text_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText user_email = (EditText) dialogView.findViewById(R.id.user_email);

                dialogBuilder.setTitle("Forgot Password");
                dialogBuilder.setPositiveButton("send", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int whichButton) {
                          if(!isValid(user_email.getText().toString())){
                            Toast.makeText(SignIn_Activity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                        }
                          else {
                              FirebaseAuth auth = FirebaseAuth.getInstance();
                              String emailAddress = user_email.getText().toString();

                              auth.sendPasswordResetEmail(emailAddress)
                                      .addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @Override
                                          public void onComplete(@NonNull Task<Void> task) {
                                              if (task.isSuccessful()) {
                                                  Toast.makeText(SignIn_Activity.this, "Email Sent", Toast.LENGTH_LONG).show();
                                                  dialog.cancel();
                                              }
                                          }
                                      }).addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {

String str=e.getMessage();
                                      assert str != null;
                                      if(str.contains("There is no user")) {
                                          AlertDialog.Builder builder=new AlertDialog.Builder(SignIn_Activity.this);
                                          builder.setMessage("Email id does not exist!Please Enter registered email.")
                                                  .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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
                                      Toast.makeText(SignIn_Activity.this, "Failed.Try again"+e.getMessage(), Toast.LENGTH_LONG).show();
                                  }
                              });

                          }
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });

//        forgot_password=(TextView)findViewById(R.id.forgot_password);
//        forgot_password.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth auth = FirebaseAuth.getInstance();
//                String emailAddress = "user@example.com";
//
//                auth.sendPasswordResetEmail(emailAddress)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Log.d(TAG, "Email sent.");
//                                }
//                            }
//                        });
//            }
//        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn_Activity.this,RegisterActivity.class));
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 if(!isValid(user_email.getText().toString())){
                    Toast.makeText(SignIn_Activity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                }
                else if(user_password.getText().toString().isEmpty()){
                    Toast.makeText(SignIn_Activity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(user_password.getText().toString().length()<=5){
                    Toast.makeText(SignIn_Activity.this, "Password should be greater than 5 characters", Toast.LENGTH_SHORT).show();
                }

                 else if(!isOnline()){
                     Toast.makeText(SignIn_Activity.this, "No internet", Toast.LENGTH_LONG).show();
                 }

                else
                {

                    myProgressDialog = ProgressDialog.show(SignIn_Activity.this,
                            "HOLD ON...", "Signing In", true);

                    mAuth.signInWithEmailAndPassword(user_email.getText().toString(), user_password.getText().toString())
                            .addOnCompleteListener(SignIn_Activity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                       final SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
                                        final SharedPreferences.Editor editor = pref.edit();
                                        editor.clear().apply();
                                        final FirebaseUser user = mAuth.getCurrentUser();
                                        pref.edit().putString("User_Name", user.getDisplayName()).apply();
                                        pref.edit().putString("User_Email", user_email.getText().toString()).apply();
                                        pref.edit().putString("Active_Group","No_Active_Group").apply();
                                        pref.edit().putString("Active_Group_Id","Empty").apply();


                                        editor.apply();

                                        FirebaseFirestore db=FirebaseFirestore.getInstance();

                                        final DocumentReference docRef = db.collection("USERS_DATA").document(user.getUid());
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        pref.edit().putString("coins",document.getString("Coins")).apply();
                                                        pref.edit().putString("User_Profile_Url",document.getString("Profile_Url")).apply();
                                                        editor.apply();
                                                        Toast.makeText(SignIn_Activity.this, "Successfull", Toast.LENGTH_SHORT).show();
                                                        myProgressDialog.dismiss();
                                                        startActivity(new Intent(SignIn_Activity.this,Home_Activity.class));
                                                        finish();

                                                    } else {

                                                        pref.edit().putString("coins","150").apply();
                                                        pref.edit().putString("User_Profile_Url",document.getString("Profile_Url")).apply();
                                                        editor.apply();
                                                        Toast.makeText(SignIn_Activity.this, "Successfull", Toast.LENGTH_SHORT).show();
                                                        myProgressDialog.dismiss();
                                                        startActivity(new Intent(SignIn_Activity.this,Home_Activity.class));
                                                        finish();
                                                    }
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    myProgressDialog.dismiss();
                                                    AlertDialog.Builder builder=new AlertDialog.Builder(SignIn_Activity.this);
                                                    builder.setMessage("Email or password is invalid.")
                                                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                   dialogInterface.cancel();
                                                                }
                                                            });

                                                    AlertDialog alertDialog=builder.create();
                                                    alertDialog.setTitle("Authentication Failed");
                                                    alertDialog.show();
                                                }
                                            }
                                        });



                                    } else {
                                        // If sign in fails, display a message to the user.
myProgressDialog.dismiss();
                                        Toast.makeText(SignIn_Activity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                        // ...
                                    }

                                    // ...
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
