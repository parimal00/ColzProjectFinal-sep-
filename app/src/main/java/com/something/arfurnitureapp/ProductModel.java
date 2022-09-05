package com.something.arfurnitureapp;

public  class ProductModel {

    String title;
    String price;
    String userID;
    String model_id;
    String addresss;
    String phone_no;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    String quantity;

    public String getAddresss() {
        return addresss;
    }

    public void setAddresss(String addresss) {
        this.addresss = addresss;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getmodel_id() {
        return model_id;
    }

    public void setmodel_id(String model_id) {
        this.model_id = model_id;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    String image_name;

    public String getProduct_doc_ref() {
        return product_doc_ref;
    }

    public void setProduct_doc_ref(String product_doc_ref) {
        this.product_doc_ref = product_doc_ref;
    }

    String product_doc_ref;

    private  ProductModel(){

    }
    private  ProductModel(String title, String  price, String image_name, String userID,String model_id,String addresss,String quantity,String product_doc_ref, String phone_no){
        this.price=price;
        this.title=title;
        this.image_name=image_name;
        this.userID=userID;
        this.model_id = model_id;
        this.addresss=addresss;
        this.phone_no=phone_no;
        this.quantity = quantity;
        this.product_doc_ref = product_doc_ref;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }





}
