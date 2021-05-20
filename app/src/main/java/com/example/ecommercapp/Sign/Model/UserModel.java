package com.example.ecommercapp.Sign.Model;

public class UserModel {

    public UserModel() {
    }

    private  String UserId;
    private  String Name;
    private  String Address;
    private  String Contact;
    private  String UType;
    private  String UImages;
    private  String UStatus;
    private  String Email;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUStatus() {
        return UStatus;
    }

    public void setUStatus(String UStatus) {
        this.UStatus = UStatus;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getUType() {
        return UType;
    }

    public void setUType(String UType) {
        this.UType = UType;
    }

    public String getUImages() {
        return UImages;
    }

    public void setUImages(String UImages) {
        this.UImages = UImages;
    }
}
