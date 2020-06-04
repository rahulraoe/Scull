package com.err.scullapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class My_Groups_Activity extends AppCompatActivity {

    private ArrayList<String> names= new ArrayList<>();
    private ArrayList<String> count = new ArrayList<>();
    private ArrayList<String> date= new ArrayList<>();
    private ArrayList<String> doc_id= new ArrayList<>();
    private ArrayList<String> Creator_names=new ArrayList<>();
    private ArrayList<String> urls=new ArrayList<>();


    private ArrayList<String> group_members_doc_id=new ArrayList<>();


    RecyclerView My_Groups_Recycler;
    ArrayList<My_Group_Model>   model;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    MyGroupRecyclerAdapter recycler_adapter;

    RelativeLayout Rel_My_Groups;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppBarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__groups_);


        getSupportActionBar().setTitle("My Groups");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        My_Groups_Recycler=(RecyclerView)findViewById(R.id.My_Groups_Recycler);

        Rel_My_Groups=(RelativeLayout)findViewById(R.id.Rel_My_Groups);

        model=new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();


        AdLoader.Builder builder = new AdLoader.Builder(
                My_Groups_Activity.this, "ca-app-pub-3940256099942544/2247696110");
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                TemplateView template =findViewById(R.id.my_template);
                template.setNativeAd(unifiedNativeAd);
            }
        });
        AdLoader adLoader = builder.build();
        adLoader.loadAd(new AdRequest.Builder().build());


        RetrieveData(user.getUid());

        LoadRecyclerView();




    }

    private void LoadRecyclerView() {
        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        //My_Groups_Recycler.setLayoutManager(staggeredGridLayoutManager);
        My_Groups_Recycler.setLayoutManager(new LinearLayoutManager(My_Groups_Activity.this));
        My_Groups_Recycler.setAdapter(recycler_adapter);
    }

    private void RetrieveData(String uid) {
        db.collection("USERS_DATA").document(uid).collection("MY_GROUPS").orderBy("TimeLine", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot querySnapshot:task.getResult()){

                            My_Group_Model modell=new My_Group_Model(querySnapshot.getString("Group_name"),querySnapshot.getString("Group_Count"),querySnapshot.getString("Creator_name"),querySnapshot.getString("Date"),querySnapshot.getString("Group_Doc_Id"),querySnapshot.getString("Group_Pic_Url"),querySnapshot.getString("Group_Members_Doc_Id"));
                            model.add(modell);

                        }

                        for (int i = 0; i < model.size(); i++) {
                            names.add(model.get(i).getGroup_Name());
                            count.add(model.get(i).getGroup_Count());
                            date.add(model.get(i).getDate());
                            doc_id.add(model.get(i).getDoc_Id());

                            Creator_names.add(model.get(i).getCreator_name());
                            urls.add(model.get(i).getGroup_Pic_url());

                            group_members_doc_id.add(model.get(i).getGROUP_MEMBERS_ID());

                        }


                        recycler_adapter=new MyGroupRecyclerAdapter(names,count,date,doc_id,Creator_names,urls,group_members_doc_id,My_Groups_Activity.this);
                        My_Groups_Recycler.setAdapter(recycler_adapter);
                        Rel_My_Groups.setVisibility(View.GONE);
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(My_Groups_Activity.this, "ERROR OCCURED TRY AGAIN", Toast.LENGTH_LONG).show();
                Rel_My_Groups.setVisibility(View.GONE);
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
