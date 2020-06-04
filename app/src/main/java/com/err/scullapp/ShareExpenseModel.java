package com.err.scullapp;

import java.util.ArrayList;

/**
 * Created by Rahul Rao on 01-05-2020.
 *
 */

public class ShareExpenseModel {
ArrayList<String> Credit_Amount;
    ArrayList<String> Debit_Amount;
    ArrayList<String> Name;
    ArrayList<String> Date;



public ShareExpenseModel(){

    }

    public ArrayList<String> getCredit_Amount() {
        return Credit_Amount;
    }

    public void setCredit_Amount(ArrayList<String> credit_Amount) {
        Credit_Amount = credit_Amount;
    }

    public ArrayList<String> getDebit_Amount() {
        return Debit_Amount;
    }

    public void setDebit_Amount(ArrayList<String> debit_Amount) {
        Debit_Amount = debit_Amount;
    }

    public ArrayList<String> getName() {
        return Name;
    }

    public void setName(ArrayList<String> name) {
        Name = name;
    }

    public ArrayList<String> getDate() {
        return Date;
    }

    public void setDate(ArrayList<String> date) {
        Date = date;
    }

    public ShareExpenseModel(ArrayList<String> credit_Amount, ArrayList<String> debit_Amount, ArrayList<String> name, ArrayList<String> date) {
        Credit_Amount = credit_Amount;
        Debit_Amount = debit_Amount;
        Name = name;
        Date = date;
    }
}
