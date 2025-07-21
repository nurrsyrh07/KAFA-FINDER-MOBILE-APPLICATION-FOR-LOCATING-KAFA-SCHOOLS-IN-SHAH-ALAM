package com.example.kafafinder;

public class User {
    public String name;
    public String email;

    public User() {} // Required for Firebase

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}