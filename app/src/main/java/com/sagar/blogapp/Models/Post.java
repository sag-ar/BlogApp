package com.sagar.blogapp.Models;

import com.google.firebase.database.ServerValue;

public class Post {

    String postKey;
    String Title;
    String Discription;
    String Picture;
    String Userid;
    String UserPhoto;
    Object timestamp;

    public Post(String title, String discription, String picture, String userid, String userPhoto) {
        Title = title;
        Discription = discription;
        Picture = picture;
        Userid = userid;
        UserPhoto = userPhoto;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public Post(){

    }
    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return Title;
    }

    public String getDiscription() {
        return Discription;
    }

    public String getPicture() {
        return Picture;
    }

    public String getUserid() {
        return Userid;
    }

    public String getUserPhoto() {
        return UserPhoto;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public void setUserPhoto(String userPhoto) {
        UserPhoto = userPhoto;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
