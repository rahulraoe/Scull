package com.err.scullapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Bills_Tickets_Activity extends AppCompatActivity {


    CircleImageView Upload_Image;




    private ArrayList<String> date= new ArrayList<>();
    private ArrayList<String> pic_url= new ArrayList<>();
    private ArrayList<String> Creator_names=new ArrayList<>();
    private ArrayList<String> memory=new ArrayList<>();


    RecyclerView Group_Gallery_Recycler;


    ArrayList<My_Group_Model>   model;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    GroupGalleryRecyclerAdapter recycler_adapter;

    RelativeLayout Rel_Group_Gallery,Rel_Group_Gallery_no;
    String DOC_ID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills__tickets_);


        getSupportActionBar().setTitle("Bills And Tickets");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Upload_Image=(CircleImageView)findViewById(R.id.Upload_Image);
        Group_Gallery_Recycler=(RecyclerView)findViewById(R.id.Group_Gallery_Recycler);
        Rel_Group_Gallery=(RelativeLayout)findViewById(R.id.Rel_Group_Gallery);
        Rel_Group_Gallery_no=(RelativeLayout)findViewById(R.id.Rel_Group_Gallery_no);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0);

        DOC_ID=pref.getString("Active_Group_Id","Empty");

        Glide.with(Bills_Tickets_Activity.this)
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/record%20%20icon.png?alt=media&token=21e1b176-fbfb-423d-9c7d-fac4f155f240")
                .into(Upload_Image)
        ;
        model=new ArrayList<>();
        Rel_Group_Gallery_no.setVisibility(View.GONE);
        LoadRecyclerView();
        RetrieveData();



        Upload_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
//                AlertDialog.Builder builder = new AlertDialog.Builder (Bills_Tickets_Activity.this);
//                builder.setTitle("Add Photo!");
//                builder.setItems(options, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int item) {
//                        if (options[item].equals("Take Photo"))
//                        {
////                            Intent pictureIntent = new Intent(
////                                    MediaStore.ACTION_IMAGE_CAPTURE
////                            );
////                            if(pictureIntent.resolveActivity(getPackageManager()) != null) {
////                                startActivityForResult(pictureIntent,
////                                        REQUEST_CAPTURE_IMAGE);
////                            }
//
//
//                            Intent in=new Intent(Bills_Tickets_Activity.this,Upload_Gallery_Activity.class);
//                            in.putExtra("Choice","Camera");
//                            startActivity(in);
//                        }
//                        else if (options[item].equals("Choose from Gallery"))
//                        {
////                            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////                            startActivityForResult(intent, 2);
//
//                            Intent in=new Intent(Bills_Tickets_Activity.this,Upload_Gallery_Activity.class);
//                            in.putExtra("Choice","Gallery");
//                            startActivity(in);
//
//                        }
//                        else if (options[item].equals("Cancel")) {
//                            dialog.dismiss();
//                        }
//                    }
//                });
//                builder.show();


                Intent in=new Intent(Bills_Tickets_Activity.this,Upload_Gallery_Activity.class);
                in.putExtra("Choice","Gallery");
                in.putExtra("decider","Bills");
                startActivity(in);

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


    private void LoadRecyclerView() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        Group_Gallery_Recycler.setLayoutManager(staggeredGridLayoutManager);
        Group_Gallery_Recycler.setAdapter(recycler_adapter);
    }

    private void RetrieveData() {
        db.collection("GROUPS").document(DOC_ID).collection("GROUP_BILLS").orderBy("TimeLine", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot querySnapshot:task.getResult()){

                            My_Group_Model modell=new My_Group_Model(querySnapshot.getString("Date"),querySnapshot.getString("Pic_Url"),querySnapshot.getString("About_Memory"),querySnapshot.getString("Added_By"));
                            model.add(modell);

                        }

                        for (int i = 0; i < model.size(); i++) {

                            date.add(model.get(i).getDate_gallery());
                            pic_url.add(model.get(i).getPic_url());

                            Creator_names.add(model.get(i).getCreator());
                            memory.add(model.get(i).getMemory());



                        }

                        if(model.size()<1){
                            Rel_Group_Gallery_no.setVisibility(View.VISIBLE);
                        }
                        recycler_adapter=new GroupGalleryRecyclerAdapter(date,pic_url,Creator_names,memory,"Bills");
                        Group_Gallery_Recycler.setAdapter(recycler_adapter);
                        Rel_Group_Gallery.setVisibility(View.GONE);
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Bills_Tickets_Activity.this, "ERROR OCCURED TRY AGAIN", Toast.LENGTH_LONG).show();
                Rel_Group_Gallery.setVisibility(View.GONE);
            }
        });

    }




}
