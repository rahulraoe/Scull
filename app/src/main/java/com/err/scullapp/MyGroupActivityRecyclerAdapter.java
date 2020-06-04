package com.err.scullapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Rahul Rao on 02-05-2020.
 */
public class MyGroupActivityRecyclerAdapter extends RecyclerView.Adapter<MyGroupActivityRecyclerAdapter.ViewHolder>  {

    private ArrayList<String> Date = new ArrayList<>();
    private ArrayList<String> text=new ArrayList<>();

    Context con;

    public MyGroupActivityRecyclerAdapter(ArrayList<String> date, ArrayList<String> text, Context con) {
        Date = date;
        this.text = text;
        this.con = con;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return Date.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_groups_activity,parent,false);
        return new MyGroupActivityRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.text.setText(text.get(position));
        holder.Date.setText(Date.get(position));


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView text,Date;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text=itemView.findViewById(R.id.text);
            Date=itemView.findViewById(R.id.Date);

            cardView=itemView.findViewById(R.id.card);
        }
    }


}
