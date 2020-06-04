package com.err.scullapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class Upload_Gallery_Activity extends AppCompatActivity {

    String CHOICE;

    private Uri filePath;

    private final int IMAGE_REQUEST = 101;

    ImageView Uploaded_Image;
    byte[] data_Bytes_Final_Db;



    FirebaseStorage storage;
    StorageReference storageReference;

    CardView Upload_Memory;

    Uri downloadUrll;

    ProgressDialog progressDialog;



    EditText About_Memory;

    String About_Memory_String="empty";

    String DOC_ID,user_name,DECIDER;
    String coins;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__gallery_);
        CHOICE=getIntent().getStringExtra("Choice");
        DECIDER=getIntent().getStringExtra("decider");
if(DECIDER.equalsIgnoreCase("gallery"))
        getSupportActionBar().setTitle("Upload Memory");
else
    getSupportActionBar().setTitle("Upload Photo");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Uploaded_Image=(ImageView)findViewById(R.id.Uploaded_Image);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0);
        coins=pref.getString("coins","0");
        DOC_ID=pref.getString("Active_Group_Id","Empty");
        user_name=pref.getString("User_Name","Empty");

        mAuth = FirebaseAuth.getInstance();

        About_Memory=(EditText)findViewById(R.id.About_Memory) ;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Upload_Memory=(CardView)findViewById(R.id.Upload_Memory);

        Upload_Memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                About_Memory_String=About_Memory.getText().toString();
                if(DECIDER.equalsIgnoreCase("gallery"))
                UploadImageToFirestore();
                else
                {
                    UploadImageToFirestoreBills();
                }
            }
        });


        if(CHOICE.equalsIgnoreCase("Gallery")){
            UploadImageFromGallery();
        }

    }



    private void UploadImageFromGallery() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"),IMAGE_REQUEST);



        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, IMAGE_REQUEST);
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
                Uploaded_Image.setImageBitmap(bitmap);



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

        if(filePath != null)
        {
           progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

           final StorageReference ref = storageReference.child("Group_Memories/"+ UUID.randomUUID().toString());
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
                            Toast.makeText(Upload_Gallery_Activity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });



        }


        else
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Please select image to upload.")
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
    }



    private void UploadImageToFirestoreBills() {

        if(filePath != null)
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("Group_Bills/"+ UUID.randomUUID().toString());
            ref.putBytes(data_Bytes_Final_Db)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        downloadUrll = uri;

                                        //.makeText(Upload_Gallery_Activity.this, "yes"+downloadUrll, Toast.LENGTH_SHORT).show();



                                        UploadDataToFirestoreBills();



                                    }
                                });
                            }
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Upload_Gallery_Activity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });



        }


        else
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Please select image to upload.")
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

    }

    private void UploadDataToFirestoreBills() {


        DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
        final String date = dateFormat2.format(new Date()).toString();

        DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
        final String timeline = dateFormat222.format(new Date()).toString();





        final Map<String, Object> data1 = new HashMap<>();
        data1.put("Pic_Url", downloadUrll.toString());
        data1.put("Date", date);
        data1.put("TimeLine",timeline);

        data1.put("Added_By",user_name);
        if(About_Memory_String.equalsIgnoreCase("Empty"))
            data1.put("About_Memory","No Data");
        else
            data1.put("About_Memory",About_Memory_String);







        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final Map<String, Object> data = new HashMap<>();

        data.put("Date",date);
        data.put("Added_By",user_name);
        data.put("TimeLine",timeline);

        data.put("text",user_name+" uploaded new photo in Bills and Tickets.");


        db.collection("GROUPS").document(DOC_ID).collection("GROUP_BILLS")
                .add(data1)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {



                        db.collection("GROUPS").document(DOC_ID).collection("GROUP_ACTIVITY")
                                .add(data)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        int c=Integer.parseInt(coins)-75;
                                        pref.edit().putString("coins",String.valueOf(c)).apply();
                                        editor.apply();


                                        final FirebaseUser user = mAuth.getCurrentUser();

                                        final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid());
                                        contact.update("Coins",String.valueOf(c))
                                                .addOnSuccessListener(new OnSuccessListener< Void >() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast.makeText(Upload_Gallery_Activity.this, "Memory Uploaded Successfully", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(Upload_Gallery_Activity.this, "Error occured.Try again", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Upload_Gallery_Activity.this, "Error occured.Try again", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
                    }
                });




    }


    private void UploadDataToFirestore() {



        DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
        final String date = dateFormat2.format(new Date()).toString();

        DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
        final String timeline = dateFormat222.format(new Date()).toString();





        final Map<String, Object> data1 = new HashMap<>();
        data1.put("Pic_Url", downloadUrll.toString());
        data1.put("Date", date);
        data1.put("TimeLine",timeline);

        data1.put("Added_By",user_name);
        if(About_Memory_String.equalsIgnoreCase("Empty"))
            data1.put("About_Memory","No Data");
        else
            data1.put("About_Memory",About_Memory_String);







        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final Map<String, Object> data = new HashMap<>();

        data.put("Date",date);
        data.put("Added_By",user_name);
        data.put("TimeLine",timeline);

        data.put("text",user_name+" uploaded a new memory.");


        db.collection("GROUPS").document(DOC_ID).collection("GROUP_GALLERY")
                .add(data1)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {



                        db.collection("GROUPS").document(DOC_ID).collection("GROUP_ACTIVITY")
                                .add(data)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {


                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        int c=Integer.parseInt(coins)-75;
                                        pref.edit().putString("coins",String.valueOf(c)).apply();
                                        editor.apply();


                                        final FirebaseUser user = mAuth.getCurrentUser();

                                        final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid());
                                        contact.update("Coins",String.valueOf(c))
                                                .addOnSuccessListener(new OnSuccessListener< Void >() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast.makeText(Upload_Gallery_Activity.this, "Memory Uploaded Successfully", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(Upload_Gallery_Activity.this, "Error occured.Try again", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Upload_Gallery_Activity.this, "Error occured.Try again", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
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
