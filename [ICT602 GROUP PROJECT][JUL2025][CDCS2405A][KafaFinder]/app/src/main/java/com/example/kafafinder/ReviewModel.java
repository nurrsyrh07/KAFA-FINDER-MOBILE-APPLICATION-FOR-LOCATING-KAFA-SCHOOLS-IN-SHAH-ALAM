package com.example.kafafinder;

public class ReviewModel {
    private String feedback;
    private int rating;

    // A need for Firebase
    public ReviewModel() {
    }

    public ReviewModel(String feedback, int rating) {
        this.feedback = feedback;
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

