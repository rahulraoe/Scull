package com.err.scullapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rahul Rao on 01-05-2020.
 */
public class NewExpenseRecycleAdapter   extends RecyclerView.Adapter<NewExpenseRecycleAdapter.ViewHolder> {
    private ArrayList<String> names=new ArrayList<>();
    private ArrayList<String> credit=new ArrayList<>();
    private ArrayList<String> debit=new ArrayList<>();
    private ArrayList<String> new_credit=new ArrayList<>();
    private String Total_Amount="0";
    private Context con;

    double sum=0;



    private ArrayList<String> Amount_Due=new ArrayList<>();


    private ArrayList<String> new_debit_for_db=new ArrayList<>();

    private ArrayList<String> new_credit_for_db=new ArrayList<>();


    String Amount_For_String;

    private ProgressDialog myProgressDialog;

    private ArrayList<String> Number_Of=new ArrayList<>();





    public NewExpenseRecycleAdapter(ArrayList<String> names, ArrayList<String> credit, ArrayList<String> debit, Context con, ArrayList<String> amount_Due, ArrayList<String> new_credit,String Amount_For_String, ArrayList<String> Number_of) {
        this.names = names;
        this.credit = credit;
        this.debit = debit;
        this.con = con;
        Amount_Due = amount_Due;
        this.new_credit=new_credit;

        new_debit_for_db=amount_Due;
        new_credit_for_db=new_credit;

        this.Amount_For_String=Amount_For_String;

        this.Number_Of=Number_of;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_expense,parent,false);
        return new NewExpenseRecycleAdapter.ViewHolder(view);
    }




    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {

        holder.name.setText(names.get(position));

        holder.Amount_Spent_For.setText(Amount_Due.get(position));





















    }

    private void yourMethodName(ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return names.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        CardView cardView;

        EditText Amount_Spent_By,Amount_Spent_For;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.Group_Name);
            Amount_Spent_By=itemView.findViewById(R.id.Amount_Spent_By);
            Amount_Spent_For=itemView.findViewById(R.id.Amount_Spent_For);



            cardView=itemView.findViewById(R.id.card);

            Amount_Spent_For.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {



                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.toString().isEmpty()) {
                        Amount_Due.set(getAdapterPosition(), "0");

                    }
                    else
                        Amount_Due.set(getAdapterPosition(),s.toString());











                }
            });


            Amount_Spent_By.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {



                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.toString().isEmpty()) {
                       new_credit.set(getAdapterPosition(), "0");

                    }
                    else
                        new_credit.set(getAdapterPosition(),s.toString());











                }
            });


        }
    }


    public void ChangeEditText(String string,ArrayList<String> amount_Due,ArrayList<String> credit){
        this.sum=0;
        this.Total_Amount=string;
        this.Amount_Due=amount_Due;
        this.new_credit=credit;
        notifyDataSetChanged();
    }


    public void AddExpense(){



        myProgressDialog = ProgressDialog.show(con,
                "HOLD ON...", "Adding new expense of "+Total_Amount, true);


        sum=0;

        for(int i=0;i<names.size();i++){
            sum=sum+Double.parseDouble(Amount_Due.get(i));
        }



        double diff=Double.parseDouble(Total_Amount)-sum;

        double credit_sum=0;
        for(int i=0;i<new_credit.size();i++){

            credit_sum=credit_sum+Double.parseDouble(new_credit.get(i));

        }



        double credit_diff=Double.parseDouble(Total_Amount)-credit_sum;


        if(diff>1||diff<-1){

            AlertDialog.Builder builder=new AlertDialog.Builder(con);


            builder.setMessage("Total amount "+Total_Amount+" is not equal to due amount"+sum+".Please add correctly.")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alertDialog=builder.create();
            alertDialog.setTitle("ERROR");
            alertDialog.show();
            myProgressDialog.dismiss();

        }
        else if(credit_diff>1||credit_diff<-1)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(con);


            builder.setMessage("Total amount "+Total_Amount+" is not equal to spent amount"+credit_sum+".Please add correctly.")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alertDialog=builder.create();
            alertDialog.setTitle("ERROR");
            alertDialog.show();
            myProgressDialog.dismiss();
        }
        else if(Amount_For_String.isEmpty()){
            AlertDialog.Builder builder=new AlertDialog.Builder(con);


            builder.setMessage("Enter Expense title")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alertDialog=builder.create();
            alertDialog.setTitle("ERROR");
            alertDialog.show();
            myProgressDialog.dismiss();
        }

        else if(!isOnline()){



            String str="";

            for(int i=0;i<names.size();i++){

                double a=Double.parseDouble(Amount_Due.get(i))+Double.parseDouble(debit.get(i));
                str=str+String.valueOf(a)+" ";
                new_debit_for_db.set(i,String.valueOf(a));

            }



            for(int i=0;i<names.size();i++){

                double a=Double.parseDouble(new_credit.get(i))+Double.parseDouble(credit.get(i));
                new_credit_for_db.set(i,String.valueOf(a));

            }


            for(int i=0;i<Number_Of.size();i++){
                if(Double.parseDouble(new_credit_for_db.get(i))>0){
                    int st= Integer.parseInt(Number_Of.get(i));
                    st=st+1;
                    Number_Of.set(i,String.valueOf(st));
                }
            }


            SharedPreferences preferences=con.getSharedPreferences("User_Details",0);

            final String Doc_id=preferences.getString("Active_Group_Id","Empty");

            final String group_doc_id=preferences.getString("Active_Group_Members_Id","Empty");
            final String User_Name=preferences.getString("User_Name","User");


            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Toast.makeText(con, "hello"+Doc_id+" "+group_doc_id, Toast.LENGTH_SHORT).show();

            final DocumentReference contact = db.collection("GROUPS").document(Doc_id).collection("GROUP_MEMBERS").document(group_doc_id);;
            contact.update("Credit_Amount", new_credit_for_db);
            contact.update("Number_Of", Number_Of);
            contact.update("Debit_Amount", new_debit_for_db)
                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(con, "error"+e, Toast.LENGTH_SHORT).show();
                }
            });


            DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
            final String date = dateFormat2.format(new Date()).toString();

            DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
            final String timeline = dateFormat222.format(new Date()).toString();

            final Map<String, Object> data = new HashMap<>();
            data.put("Expense_Amount", Total_Amount);
            data.put("Date",date);
            data.put("Added_By",User_Name);
            data.put("TimeLine",timeline);
            data.put("Expense_For",Amount_For_String);
            data.put("text",User_Name+" added new expense of "+Total_Amount+" for "+Amount_For_String+"." );

            db.collection("GROUPS").document(Doc_id).collection("GROUP_EXPENSES")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {





                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });



            db.collection("GROUPS").document(Doc_id).collection("GROUP_ACTIVITY")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {








                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });



            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            final FirebaseUser user = mAuth.getCurrentUser();
            String coins;
            SharedPreferences pref = con.getSharedPreferences("User_Details", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            coins=pref.getString("coins","0");

            int c=Integer.parseInt(coins)-50;
            pref.edit().putString("coins",String.valueOf(c)).apply();
            editor.apply();

            final DocumentReference contact1 = db.collection("USERS_DATA").document(user.getUid());
            contact1.update("Coins",String.valueOf(c))
                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                        @Override
                        public void onSuccess(Void aVoid) {



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            myProgressDialog.dismiss();



            androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(con);
            builder.setMessage("Expense added.Your device is offline expense will reflect to others only after you are online.")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            ((Activity)con).finish();
                        }
                    });

            androidx.appcompat.app.AlertDialog alertDialog=builder.create();
            alertDialog.setTitle("ERROR");
            alertDialog.show();

        }
        else
        {
            String str="";

            for(int i=0;i<names.size();i++){

               double a=Double.parseDouble(Amount_Due.get(i))+Double.parseDouble(debit.get(i));
               str=str+String.valueOf(a)+" ";
                  new_debit_for_db.set(i,String.valueOf(a));

            }



            for(int i=0;i<names.size();i++){

                double a=Double.parseDouble(new_credit.get(i))+Double.parseDouble(credit.get(i));
                new_credit_for_db.set(i,String.valueOf(a));

            }


            for(int i=0;i<Number_Of.size();i++){
                if(Double.parseDouble(new_credit_for_db.get(i))>0){
                    int st= Integer.parseInt(Number_Of.get(i));
                    st=st+1;
                    Number_Of.set(i,String.valueOf(st));
                }
            }


            SharedPreferences preferences=con.getSharedPreferences("User_Details",0);

            final String Doc_id=preferences.getString("Active_Group_Id","Empty");

            final String group_doc_id=preferences.getString("Active_Group_Members_Id","Empty");
            final String User_Name=preferences.getString("User_Name","User");


           final FirebaseFirestore db = FirebaseFirestore.getInstance();

           // Toast.makeText(con, "hello"+Doc_id+" "+group_doc_id, Toast.LENGTH_SHORT).show();

            final DocumentReference contact = db.collection("GROUPS").document(Doc_id).collection("GROUP_MEMBERS").document(group_doc_id);;
            contact.update("Credit_Amount", new_credit_for_db);
            contact.update("Number_Of", Number_Of);
            contact.update("Debit_Amount", new_debit_for_db)
                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(con, "error"+e, Toast.LENGTH_SHORT).show();
                }
            });


            DateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
            final String date = dateFormat2.format(new Date()).toString();

            DateFormat dateFormat222 = new SimpleDateFormat("yyyyMMddHHmm aa");
            final String timeline = dateFormat222.format(new Date()).toString();

            final Map<String, Object> data = new HashMap<>();
            data.put("Expense_Amount", Total_Amount);
            data.put("Date",date);
            data.put("Added_By",User_Name);
            data.put("TimeLine",timeline);
            data.put("Expense_For",Amount_For_String);
            data.put("text",User_Name+" added new expense of "+Total_Amount+" for "+Amount_For_String+"." );

            db.collection("GROUPS").document(Doc_id).collection("GROUP_EXPENSES")
                    .add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            db.collection("GROUPS").document(Doc_id).collection("GROUP_ACTIVITY")
                                    .add(data)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                            FirebaseAuth mAuth=FirebaseAuth.getInstance();
                                            final FirebaseUser user = mAuth.getCurrentUser();
String coins;
                                            SharedPreferences pref = con.getSharedPreferences("User_Details", 0); // 0 - for private mode
                                            SharedPreferences.Editor editor = pref.edit();
                                            coins=pref.getString("coins","0");

                                            int c=Integer.parseInt(coins)-50;
                                            pref.edit().putString("coins",String.valueOf(c)).apply();
                                            editor.apply();

                                            final DocumentReference contact = db.collection("USERS_DATA").document(user.getUid());
                                            contact.update("Coins",String.valueOf(c))
                                                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {


                                                            myProgressDialog.dismiss();


                                                            AlertDialog.Builder builder=new AlertDialog.Builder(con);


                                                            builder.setMessage("New expense added successfully")
                                                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            ((Activity)con).finish();
                                                                        }
                                                                    });
                                                            AlertDialog alertDialog=builder.create();
                                                            alertDialog.setTitle("ERROR");
                                                            alertDialog.show();

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });











        }

    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void ChangeEditTextFor(String toString) {
        this.Amount_For_String=toString;
    }


}
