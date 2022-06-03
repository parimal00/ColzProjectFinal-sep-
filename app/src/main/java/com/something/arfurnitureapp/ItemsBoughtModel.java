package com.something.arfurnitureapp;

public class ItemsBoughtModel {

    ItemsBoughtModel(){

    }

    String postedUserId;

    public String getPostedUserId() {
        return postedUserId;
    }

    public void setPostedUserId(String postedUserId) {
        this.postedUserId = postedUserId;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    String image_name;

    public String getDocumentRef() {
        return documentRef;
    }

    public void setDocumentRef(String documentRef) {
        this.documentRef = documentRef;
    }

    String documentRef;




    String price;
    String productName;

    public String getOrdered_quantity() {
        return ordered_quantity;
    }

    public void setOrdered_quantity(String ordered_quantity) {
        this.ordered_quantity = ordered_quantity;
    }

    String ordered_quantity;


    public ItemsBoughtModel(String image_name, String price, String productName, String documentRef, String postedUserId, String ordered_quantity) {
        this.image_name = image_name;
        this.price = price;
        this.productName = productName;
        this.documentRef = documentRef;
        this.postedUserId = postedUserId;
        this.ordered_quantity = ordered_quantity;

    }



    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
