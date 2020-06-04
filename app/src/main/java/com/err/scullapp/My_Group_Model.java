package com.err.scullapp;

/**
 * Created by Rahul Rao on 30-04-2020.
 */
public class My_Group_Model {

    String Group_Name;
      String Group_Count;
    String Creator_name;
    String Date;
    String Doc_Id;
    String Group_Pic_url;

    String GROUP_MEMBERS_ID;


    //for my group activity

    String date_acti;
    String text_acti;


    ///////////for gallery activity
    String Date_gallery;
    String pic_url;
    String memory;
    String creator;

    public My_Group_Model(String date_gallery, String pic_url, String memory, String creator) {
        Date_gallery = date_gallery;
        this.pic_url = pic_url;
        this.memory = memory;
        this.creator = creator;
    }


    public String getDate_gallery() {
        return Date_gallery;
    }

    public void setDate_gallery(String date_gallery) {
        Date_gallery = date_gallery;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }









    //////////////gallery end



    public String getDate_acti() {
        return date_acti;
    }

    public void setDate_acti(String date_acti) {
        this.date_acti = date_acti;
    }

    public String getText_acti() {
        return text_acti;
    }

    public void setText_acti(String text_acti) {
        this.text_acti = text_acti;
    }

    public My_Group_Model(String date_acti, String text_acti) {

        this.date_acti = date_acti;
        this.text_acti = text_acti;
    }

    public String getGROUP_MEMBERS_ID() {
        return GROUP_MEMBERS_ID;
    }

    public void setGROUP_MEMBERS_ID(String GROUP_MEMBERS_ID) {
        this.GROUP_MEMBERS_ID = GROUP_MEMBERS_ID;
    }

    public My_Group_Model(String group_Name, String group_Count, String creator_name, String date, String doc_Id, String group_Pic_url, String GROUP_MEMBERS_ID) {
        Group_Name = group_Name;
        Group_Count = group_Count;
        Creator_name = creator_name;
        Date = date;
        Doc_Id = doc_Id;
        Group_Pic_url = group_Pic_url;
        this.GROUP_MEMBERS_ID = GROUP_MEMBERS_ID;
    }

    public My_Group_Model(){

    }

    public String getGroup_Name() {
        return Group_Name;
    }

    public void setGroup_Name(String group_Name) {
        Group_Name = group_Name;
    }

    public String getGroup_Count() {
        return Group_Count;
    }

    public void setGroup_Count(String group_Count) {
        Group_Count = group_Count;
    }

    public String getCreator_name() {
        return Creator_name;
    }

    public void setCreator_name(String creator_name) {
        Creator_name = creator_name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDoc_Id() {
        return Doc_Id;
    }

    public void setDoc_Id(String doc_Id) {
        Doc_Id = doc_Id;
    }

    public String getGroup_Pic_url() {
        return Group_Pic_url;
    }

    public void setGroup_Pic_url(String group_Pic_url) {
        Group_Pic_url = group_Pic_url;
    }

    public My_Group_Model(String group_Name, String group_Count, String creator_name, String date, String doc_Id, String group_Pic_url) {
        Group_Name = group_Name;
        Group_Count = group_Count;
        Creator_name = creator_name;
        Date = date;
        Doc_Id = doc_Id;
        Group_Pic_url = group_Pic_url;
    }
}
