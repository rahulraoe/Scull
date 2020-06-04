package com.err.scullapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Change_Profile_Activity extends AppCompatActivity {


    private Uri filePath;

    private final int IMAGE_REQUEST = 101;

    ImageView Uploaded_Image;
    byte[] data_Bytes_Final_Db;



    FirebaseStorage storage;
    StorageReference storageReference;

    CardView Upload_Memory;

    Uri downloadUrll;

    ProgressDialog progressDialog;

    CircleImageView Group_Image;

    Button change;
String user_name;
    String docc_id,group_doc_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__profile_);

        getSupportActionBar().setTitle("Change Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        SharedPreferences pref=getApplicationContext().getSharedPreferences("User_Details",0);
       user_name= pref.getString("User_Name","User");
        docc_id=pref.getString("Active_Group_Id","Empty");
        group_doc_id=pref.getString("Active_Group_Members_Id","Empty");
        Group_Image=(CircleImageView)findViewById(R.id.Group_Image);
        change=(Button)findViewById(R.id.change);
        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST);


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImageToFirestore();
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
               Group_Image.setImageBitmap(bitmap);



                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 65, baos);
                data_Bytes_Final_Db = baos.toByteArray();




            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }



    private void UploadImageToFirestore() {

        if (filePath != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("Group_Profiles/" + UUID.randomUUID().toString());
            ref.putBytes(data_Bytes_Final_Db)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        downloadUrll = uri;

                                        //    Toast.makeText(Upload_Gallery_Activity.this, "yes"+downloadUrll, Toast.LENGTH_SHORT).show();


                                        UploadDataToFirestore();


                                    }
                                });
                            }
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Change_Profile_Activity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });


        }

    }


    private void UploadDataToFirestore() {



        DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
        final String date = dateFormat2.format(new Date()).toString();

        DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
        final String timeline = dateFormat222.format(new Date()).toString();












        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final Map<String, Object> data = new HashMap<>();

        data.put("Date",date);
        data.put("Added_By",user_name);
        data.put("TimeLine",timeline);
        data.put("text",user_name+" changed group profile.");


        final DocumentReference contact = db.collection("GROUPS").document(docc_id);
        contact.update("Group_Pic_Url", downloadUrll.toString())
                .addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.collection("GROUPS").document(docc_id).collection("GROUP_ACTIVITY")
                                .add(data)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        String coins;
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        coins=pref.getString("coins","0");
                                        int c=Integer.parseInt(coins)-75;
                                        pref.edit().putString("coins",String.valueOf(c)).apply();
                                        pref.edit().putString("Active_group_Pic_Url",downloadUrll.toString()).apply();

                                        editor.apply();
                                        FirebaseAuth mAuth=FirebaseAuth.getInstance();
                                        final FirebaseUser user = mAuth.getCurrentUser();




                                        final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid());
                                        contact.update("Coins",String.valueOf(c))
                                                .addOnSuccessListener(new OnSuccessListener< Void >() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast.makeText(Change_Profile_Activity.this, "Profile Pic changed", Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();

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
                                        Toast.makeText(Change_Profile_Activity.this, "Error occured.Try again", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Change_Profile_Activity.this, "error"+e, Toast.LENGTH_SHORT).show();
            }
        });










    }
        @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
