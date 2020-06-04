package com.err.scullapp;

/**
 * Created by Rahul Rao on 06-05-2020.
 */
class LocationModel {


    private String address;
    private Double longitude;
    private Double latitude;
    private String Landmark;
    private String About_Location;
    private String pincode;
    private String Locality;

    private String Date;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public LocationModel(String address, Double longitude, Double latitude, String landmark, String about_Location, String pincode, String locality, String date) {
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        Landmark = landmark;
        About_Location = about_Location;
        this.pincode = pincode;
        Locality = locality;
        Date = date;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String landmark) {
        Landmark = landmark;
    }

    public String getAbout_Location() {
        return About_Location;
    }

    public void setAbout_Location(String about_Location) {
        About_Location = about_Location;
    }

    public LocationModel(String address, Double longitude, Double latitude, String landmark, String about_Location, String pincode, String locality) {
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        Landmark = landmark;
        About_Location = about_Location;
        this.pincode = pincode;
        Locality = locality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }


}
