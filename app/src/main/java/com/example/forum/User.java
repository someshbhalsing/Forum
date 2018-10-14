package com.example.forum;

public class User {

    String Uid;
    String name;

    public User() {
    }

    public User(String uid, String name) {
        Uid = uid;
        this.name = name;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}