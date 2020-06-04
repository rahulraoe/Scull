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
 * Created by Rahul Rao on 01-05-2020.
 */
public class ShareExpenseRecycleAdapter extends RecyclerView.Adapter<ShareExpenseRecycleAdapter.ViewHolder> {

    private ArrayList<String> credit = new ArrayList<>();
    private ArrayList<String> debit = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();
    private ArrayList<String> Number_Of = new ArrayList<>();
    Context con;

    public ShareExpenseRecycleAdapter(ArrayList<String> credit, ArrayList<String> debit, ArrayList<String> names, ArrayList<String> date,ArrayList<String> Number_Of ){
        this.credit = credit;
        this.debit = debit;
        this.names = names;
        this.date = date;
        this.Number_Of=Number_Of;
    }

    @NonNull
    @Override
    public ShareExpenseRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.share_expense_items,parent,false);
        return new ShareExpenseRecycleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareExpenseRecycleAdapter.ViewHolder holder, final int position) {
        holder.Name.setText(names.get(position));
        holder.credit.setText(credit.get(position));
        holder.debit.setText(debit.get(position));


      double d=Double.parseDouble(credit.get(position))-Double.parseDouble(debit.get(position));


        String str=String.format("%.02f",d);
        holder.total.setText(str);


        holder.cardddd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(con, View_Expense_Activity.class);
                intent.putExtra("total_credit",credit.get(position));
                intent.putExtra("number", Number_Of.get(position));
                intent.putExtra("total_debit", debit.get(position));
                intent.putExtra("date", date.get(position));
                intent.putExtra("name", names.get(position));
                con.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name,credit,debit,total;
        CardView cardddd;


        public ViewHolder(View itemView) {
            super(itemView);
           Name=itemView.findViewById(R.id.Name);
            credit=itemView.findViewById(R.id.Credit);
            debit=itemView.findViewById(R.id.Debit);
            total=itemView.findViewById(R.id.Total);

            cardddd=itemView.findViewById(R.id.ocard);
con=itemView.getContext();








        }
    }
}
