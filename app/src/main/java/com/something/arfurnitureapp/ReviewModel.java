package com.something.arfurnitureapp;

public class ReviewModel {
    String review;
    String user_id;
    public  ReviewModel(){

    }

    public  ReviewModel(String review,String user_id){
        this.review=review;
        this.user_id=user_id;

    }
    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


}
