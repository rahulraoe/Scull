package com.err.scullapp;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rahul Rao on 15-05-2020.
 */
public class TodoRecyclerAdapter extends RecyclerView.Adapter<TodoRecyclerAdapter.ViewHolder> {

    ArrayList<String> title=new ArrayList<>();
    ArrayList<String> content=new ArrayList<>();
    ArrayList<String> color=new ArrayList<>();
    ArrayList<String> date=new ArrayList<>();
    ArrayList<String> timeline=new ArrayList<>();


    String collection;

    Context con;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;


    public TodoRecyclerAdapter(ArrayList<String> title, ArrayList<String> content, ArrayList<String> color, ArrayList<String> date, ArrayList<String> timeline, String collection) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.date = date;
        this.timeline = timeline;
        this.collection = collection;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.titlee.setText(title.get(position));

        holder.content.setText(content.get(position));

        if(color.get(position).equalsIgnoreCase("green")){

            holder.cardView.setBackgroundColor(Color.parseColor("#2ECC71"));

            holder.titlee.setTextColor(Color.parseColor("#FEFEFE"));
            holder.content.setTextColor(Color.parseColor("#FEFEFE"));



        }
        else if(color.get(position).equalsIgnoreCase("brown")){
            holder.cardView.setBackgroundColor(Color.parseColor("#fe9079"));
            holder.titlee.setTextColor(Color.parseColor("#FEFEFE"));
            holder.content.setTextColor(Color.parseColor("#FEFEFE"));
        }

        else if(color.get(position).equalsIgnoreCase("blue")){
            holder.cardView.setBackgroundColor(Color.parseColor("#00B2EE"));
            holder.titlee.setTextColor(Color.parseColor("#FEFEFE"));
            holder.content.setTextColor(Color.parseColor("#FEFEFE"));
        }
        else if(color.get(position).equalsIgnoreCase("pink")){
            holder.cardView.setBackgroundColor(Color.parseColor("#FFC0CB"));
            holder.titlee.setTextColor(Color.parseColor("#232333"));
            holder.content.setTextColor(Color.parseColor("#232333"));
        }
        else
        {
            holder.cardView.setBackgroundColor(Color.parseColor("#FEFEFE"));
            holder.titlee.setTextColor(Color.parseColor("#232333"));
            holder.content.setTextColor(Color.parseColor("#232333"));

        }





        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(con,Todo_View_Activity.class);
                in.putExtra("title",title.get(position));
                in.putExtra("content",content.get(position));
                in.putExtra("date",date.get(position));
                in.putExtra("color",color.get(position));
                con.startActivity(in);

            }
        });






holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View view) {



        final CharSequence[] choice = {"Delete","Change colour","send","Copy to clipboard"};

        new AlertDialog.Builder(con)
                .setSingleChoiceItems(choice, 0, null)
                .setTitle("Select Option")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        if(selectedPosition==0){
                            DeleteNote();
                        }
                        if(selectedPosition==1){
                            highlightNote();
                        }
                        if(selectedPosition==2){
                            SendNote();
                        }
                        if(selectedPosition==3){
                            copytoclipboardNote();
                        }





                    }
                })
                .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(con, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
        return false;
    }

    private void DeleteNote(){




       title.remove(position);
       content.remove(position);
       color.remove(position);
       date.remove(position);
       timeline.remove(position);


        Map<String, Object> data = new HashMap<>();
        data.put("Title", title);
        data.put("Content", content);
        data.put("Color", color);
        data.put("Date", date);
        data.put("Timeline", timeline);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(collection.equalsIgnoreCase("list")) {
            db.collection("USERS_DATA").document(user.getUid()).collection("TODO_LIST").document(user.getUid())
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(con, "Succesfull", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(con, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        else{
            db.collection("USERS_DATA").document(user.getUid()).collection("TODO_PLACES").document(user.getUid())
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(con, "Succesfull", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(con, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        }






    }

    private void SendNote() {


        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Notes+");
            String shareMessage =title.get(position)+ "\n";
            shareMessage = shareMessage +content.get(position);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            con.startActivity(Intent.createChooser(shareIntent, "Choose One"));
        } catch (Exception e) {
            Toast.makeText(con, "Error", Toast.LENGTH_SHORT).show();
        }

    }

    private void copytoclipboardNote(){


        ClipboardManager clipboard = (ClipboardManager) con.getSystemService(Context.CLIPBOARD_SERVICE);
        String shareMessage =title.get(position)+ "\n";
        shareMessage = shareMessage +content.get(position);
        ClipData clip = ClipData.newPlainText(title.get(position), shareMessage);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(con, "Text Copied", Toast.LENGTH_LONG).show();

    }





    private void highlightNote() {
        final CharSequence[] choice = {"Green","Brown","Blue","Pink","default"};
        new AlertDialog.Builder(con).setTitle("Choose colour")

                .setSingleChoiceItems(choice, 0, null)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

                        color.set(position,choice[selectedPosition].toString());

                        mAuth = FirebaseAuth.getInstance();
                        user = mAuth.getCurrentUser();

                        if(collection.equalsIgnoreCase("list")) {
                            final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid()).collection("TODO_LIST").document(user.getUid());
                            ;
                            contact.update("Color", color)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(con, "Color Changed", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(con, "error" + e, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        else
                        {
                            final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid()).collection("TODO_PLACES").document(user.getUid());
                            ;
                            contact.update("Color", color)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(con, "Color Changed", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(con, "error" + e, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }



                    }
                })
                .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(con, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

    }

});
    }



    @Override
    public int getItemCount() {
        return content.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titlee,content;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titlee=itemView.findViewById(R.id.title);
            content=itemView.findViewById(R.id.content);
            cardView=itemView.findViewById(R.id.card);

            con=itemView.getContext();
        }
    }
}
