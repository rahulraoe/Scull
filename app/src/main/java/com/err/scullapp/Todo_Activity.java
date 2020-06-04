package com.err.scullapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Todo_Activity extends AppCompatActivity {
    CircleImageView fab;
    TextView heading_one,heading_two,no_one,no_two;
    RecyclerView Todo_rec_one,Todo_rec_two;


    ArrayList<String> title=new ArrayList<>();
    ArrayList<String> content=new ArrayList<>();
    ArrayList<String> color=new ArrayList<>();
    ArrayList<String> date=new ArrayList<>();
    ArrayList<String> timeline=new ArrayList<>();



    ArrayList<String> titlep=new ArrayList<>();
    ArrayList<String> contentp=new ArrayList<>();
    ArrayList<String> colorp=new ArrayList<>();
    ArrayList<String> datep=new ArrayList<>();
    ArrayList<String> timelinep=new ArrayList<>();


    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;


    TodoRecyclerAdapter recycler_adapter;
    TodoRecyclerAdapter recycler_adapterp;

    String coins;
    String to;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_);

        getSupportActionBar().setTitle("Todo List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab=(CircleImageView) findViewById(R.id.fab);
        Glide.with(Todo_Activity.this)
                .load("https://firebasestorage.googleapis.com/v0/b/scull-app.appspot.com/o/add%20%20icon.png?alt=media&token=cea1478a-3efd-471f-99f6-854401999844")
                .into(fab);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("User_Details", 0); // 0 - for private mode
        to=pref.getString("Todo","0");
        coins=pref.getString("coins","0");
        if(Integer.parseInt(to)<=1) {
            AlertDialog.Builder builder=new AlertDialog.Builder(Todo_Activity.this);
            builder.setMessage("This notes will only visible to you.Group members cannot see this list.Long click on the list card to view options.")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            SharedPreferences.Editor editor = pref.edit();
                            pref.edit().putString("Todo",String.valueOf(Integer.parseInt(to)+1)).apply();
                            editor.apply();
                            dialogInterface.cancel();


                        }
                    });

            AlertDialog alertDialog=builder.create();
            alertDialog.setTitle("ERROR");
            alertDialog.show();

        }



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Integer.parseInt(coins)<50) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(Todo_Activity.this);
                    builder.setMessage("You dont have 50 coins to create a new Todo list.Earn coins.")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent in =new Intent(Todo_Activity.this,Coins_Activity.class);
                                    in.putExtra("important","important");
                                    startActivity(in);
                                }
                            });

                    AlertDialog alertDialog=builder.create();
                    alertDialog.setTitle("ERROR");
                    alertDialog.show();

                }
                else
                   startActivity(new Intent(Todo_Activity.this,Todo_Add_Activity.class));
            }
        });

        heading_one=(TextView)findViewById(R.id.heading_one);
        heading_two=(TextView)findViewById(R.id.heading_two);

        no_one=(TextView)findViewById(R.id.no_one);
        no_two=(TextView)findViewById(R.id.no_two);

        no_two.setVisibility(View.GONE);
no_one.setVisibility(View.GONE);

        Todo_rec_one=(RecyclerView)findViewById(R.id.Todo_rec_one);
        Todo_rec_two=(RecyclerView)findViewById(R.id.Todo_rec_two);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();




        SetRecyclerOne();
        SetRecyclerTwo();






    }

    private void SetRecyclerOne() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        Todo_rec_one.setLayoutManager(staggeredGridLayoutManager);
        Todo_rec_one.setNestedScrollingEnabled(false);
        Todo_rec_one.setAdapter(recycler_adapter);



















        DocumentReference docRef = db.collection("USERS_DATA").document(user.getUid()).collection("TODO_LIST").document(user.getUid());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(Todo_Activity.this, "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    title= (ArrayList<String>)snapshot.get("Title");
                    content=(ArrayList<String>) snapshot.get("Content");
                    color=(ArrayList<String>) snapshot.get("Color");
                    date=(ArrayList<String>) snapshot.get("Date");
                    timeline=(ArrayList<String>) snapshot.get("Timeline");

                    if(title.size()==0){
                        no_one.setVisibility(View.VISIBLE);
                    }
                    else if(title.size()>0)
                        no_one.setVisibility(View.GONE);

                    recycler_adapter=new TodoRecyclerAdapter(title,content,color,date,timeline,"list");
                    Todo_rec_one.setAdapter(recycler_adapter);
                    Todo_rec_one.setNestedScrollingEnabled(false);

                } else {
                    no_one.setVisibility(View.VISIBLE);
                }
            }
        });












    }

    private void SetRecyclerTwo() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        Todo_rec_two.setLayoutManager(staggeredGridLayoutManager);
        Todo_rec_two.setNestedScrollingEnabled(false);
        Todo_rec_two.setAdapter(recycler_adapterp);



        DocumentReference docRef = db.collection("USERS_DATA").document(user.getUid()).collection("TODO_PLACES").document(user.getUid());


        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(Todo_Activity.this, "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    titlep= (ArrayList<String>)snapshot.get("Title");
                    contentp=(ArrayList<String>) snapshot.get("Content");
                    colorp=(ArrayList<String>) snapshot.get("Color");
                    datep=(ArrayList<String>) snapshot.get("Date");
                    timelinep=(ArrayList<String>) snapshot.get("Timeline");

                    if(titlep.size()==0){
                        no_two.setVisibility(View.VISIBLE);
                    }
                    else if(titlep.size()>0)
                        no_two.setVisibility(View.GONE);

                    recycler_adapterp=new TodoRecyclerAdapter(titlep,contentp,colorp,datep,timelinep,"place");
                    Todo_rec_two.setAdapter(recycler_adapterp);
                    Todo_rec_two.setNestedScrollingEnabled(false);


                } else {
                    no_two.setVisibility(View.VISIBLE);

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
