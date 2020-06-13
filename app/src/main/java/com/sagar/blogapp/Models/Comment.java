package com.sagar.blogapp.Models;

import com.google.firebase.database.ServerValue;

public class Comment {

    String uname,uid,content,uimg;
    Object Timestamp;

    public Comment(String uname, String uid, String content, String uimg) {
        this.uname = uname;
        this.uid = uid;
        this.content = content;
        this.uimg = uimg;

        this.Timestamp = ServerValue.TIMESTAMP;
    }

    public Comment() {
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public Object getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(Object timestamp) {
        Timestamp = timestamp;
    }
}
