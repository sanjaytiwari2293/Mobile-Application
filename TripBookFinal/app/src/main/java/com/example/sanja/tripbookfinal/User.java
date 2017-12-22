package com.example.sanja.tripbookfinal;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sanja on 5/24/2017.
 */

public class User implements Serializable {

    String fname;
    String lname;
    String gender;
    String uname;
    String email;
    String password;
    String uid;
    String imageUrl;

    ArrayList<User> friendReq =new ArrayList<>();

    public ArrayList<User> getFriendReq() {
        return friendReq;
    }

    public void setFriendReq(ArrayList<User> friendReq) {
        this.friendReq = friendReq;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", gender='" + gender + '\'' +
                ", uname='" + uname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", uid='" + uid + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

}
