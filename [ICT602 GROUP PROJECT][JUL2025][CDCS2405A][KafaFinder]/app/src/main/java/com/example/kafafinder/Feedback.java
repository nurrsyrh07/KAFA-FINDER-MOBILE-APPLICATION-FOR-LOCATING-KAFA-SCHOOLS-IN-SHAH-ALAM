package com.example.kafafinder;
public class Feedback {
    public String id;
    public String name;
    public String text;
    public float rating;

    public Feedback() {} // Needed for Firebase

    public Feedback(String id, String name, String text, float rating) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.rating = rating;
    }
}
