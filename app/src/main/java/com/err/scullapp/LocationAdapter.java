package com.err.scullapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Rahul Rao on 06-05-2020.
 */
class LocationAdapter   extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private ArrayList<LocationModel> addressdata;

    Context context;

    public LocationAdapter(ArrayList<LocationModel> addressdata, Context context) {
        this.addressdata = addressdata;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_items, parent, false);

        return new LocationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
holder.about_location.setText(addressdata.get(position).getAbout_Location()+".");
        holder.address_text.setText(addressdata.get(position).getAddress());
        holder.address_landmark.setText(addressdata.get(position).getLandmark()+".");


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(context,Location_Bookmark_Activity3.class);
                in.putExtra("longitude",addressdata.get(position).getLongitude());
                in.putExtra("latitude",addressdata.get(position).getLatitude());
                in.putExtra("address",addressdata.get(position).getAddress()+" "+addressdata.get(position).getLandmark());
                in.putExtra("memory",addressdata.get(position).getAbout_Location());

                context.startActivity(in);


            }
        });

    }

    @Override
    public int getItemCount() {
        return addressdata.size();
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


        TextView about_location,address_text,address_landmark;

        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            about_location=itemView.findViewById(R.id.about_location);
            address_text=itemView.findViewById(R.id.address_text);
            address_landmark=itemView.findViewById(R.id.address_landmark);
            cardView=itemView.findViewById(R.id.card);



        }
    }
}
