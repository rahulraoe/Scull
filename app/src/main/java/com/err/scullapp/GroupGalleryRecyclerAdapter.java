package com.err.scullapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Rahul Rao on 04-05-2020.
 */
public class GroupGalleryRecyclerAdapter extends RecyclerView.Adapter<GroupGalleryRecyclerAdapter.ViewHolder> {
    private ArrayList<String> date= new ArrayList<>();
    private ArrayList<String> pic_url= new ArrayList<>();
    private ArrayList<String> Creator_names=new ArrayList<>();
    private ArrayList<String> memory=new ArrayList<>();

    Context con;

    String decider;

    public GroupGalleryRecyclerAdapter(ArrayList<String> date, ArrayList<String> pic_url, ArrayList<String> creator_names, ArrayList<String> memory,String decider) {
        this.date = date;
        this.pic_url = pic_url;
        Creator_names = creator_names;
        this.memory = memory;
        this.decider=decider;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_groups_gallery,parent,false);
        return new GroupGalleryRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(con)
                .load(pic_url.get(position))
                .into(holder.Group_Gallery_Image);


        holder.Group_Gallery_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(con,View_Gallery_Item_Activity.class);
                String str="Memory created by "+Creator_names.get(position)+" on "+date.get(position)+" .";
                in.putExtra("Pic_Url",pic_url.get(position));
                in.putExtra("memory",memory.get(position));
                in.putExtra("text",str);
                con.startActivity(in);
            }
        });


    }

    @Override
    public int getItemCount() {
        return pic_url.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView Group_Gallery_Image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Group_Gallery_Image=(ImageView)itemView.findViewById(R.id.Group_Gallery_Image);
            con=itemView.getContext();


        }
    }



}
