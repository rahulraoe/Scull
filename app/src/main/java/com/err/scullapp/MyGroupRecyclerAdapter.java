package com.err.scullapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Rahul Rao on 30-04-2020.
 */
public class MyGroupRecyclerAdapter extends RecyclerView.Adapter<MyGroupRecyclerAdapter.ViewHolder> {

    private ArrayList<String> Group_Names = new ArrayList<>();
    private ArrayList<String> Group_Count = new ArrayList<>();
    private ArrayList<String> Date = new ArrayList<>();
    private ArrayList<String> Doc_Id=new ArrayList<>();
    private ArrayList<String> Creator_names=new ArrayList<>();
    private ArrayList<String> urls=new ArrayList<>();

    private ArrayList<String> group_members_doc_id=new ArrayList<>();
    private Context con;

    public MyGroupRecyclerAdapter(ArrayList<String> group_Names, ArrayList<String> group_Count, ArrayList<String> date, ArrayList<String> doc_Id, ArrayList<String> creator_names, ArrayList<String> urls, ArrayList<String> group_members_doc_id, Context con) {
        Group_Names = group_Names;
        Group_Count = group_Count;
        Date = date;
        Doc_Id = doc_Id;
        Creator_names = creator_names;
        this.urls = urls;
        this.group_members_doc_id = group_members_doc_id;
        this.con = con;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_groups,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
holder.name.setText(Group_Names.get(position));
holder.count.setText(Group_Count.get(position)+"  members");
holder.Date.setText(Date.get(position));




holder.cardView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(con, View_Group_Activity.class);
        intent.putExtra("Group_Name",Group_Names.get(position));
        intent.putExtra("Doc_Id", Doc_Id.get(position));


        SharedPreferences pref = con.getSharedPreferences("User_Details", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        pref.edit().putString("Active_Group",Group_Names.get(position)).apply();

        pref.edit().putString("Active_Group_Members_Id",group_members_doc_id.get(position)).apply();

        pref.edit().putString("Active_Group_Id",Doc_Id.get(position)).apply();
        pref.edit().putString("Active_Group_Creator",Creator_names.get(position)).apply();
        pref.edit().putString("Active_group_Pic_Url",urls.get(position)).apply();
        pref.edit().putString("Active_group_Date",Date.get(position)).apply();
        pref.edit().putString("Admin",Creator_names.get(position)).apply();
        editor.apply();


       // Toast.makeText(con, "rahul"+group_members_doc_id.get(position), Toast.LENGTH_SHORT).show();
        con.startActivity(intent);
    }
});



    }

    @Override
    public int getItemCount() {
        return Group_Names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,count,Date;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.Group_Name);
            count=itemView.findViewById(R.id.Group_Count);
           Date=itemView.findViewById(R.id.Date);

            cardView=itemView.findViewById(R.id.card);
        }
    }


}
