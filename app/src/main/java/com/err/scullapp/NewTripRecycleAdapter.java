package com.err.scullapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.err.scullapp.R;

import java.util.ArrayList;

public class NewTripRecycleAdapter extends RecyclerView.Adapter<NewTripRecycleAdapter.ViewHolder> {

    private ArrayList<String> names = new ArrayList<>();
    Context con;

    public NewTripRecycleAdapter(ArrayList<String> names) {
        this.names = names;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newtrip_items, parent, false);
        NewTripRecycleAdapter.ViewHolder holder = new NewTripRecycleAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(names.get(position));
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str=names.get(position);
                Toast.makeText(con,"Removed"+str,Toast.LENGTH_LONG);
                names.remove(position);
                notifyDataSetChanged();

            }
        });


    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            remove=(ImageView)itemView.findViewById(R.id.remove);
            con=itemView.getContext();
        }
    }

}

