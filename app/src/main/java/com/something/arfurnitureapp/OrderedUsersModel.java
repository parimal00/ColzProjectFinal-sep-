package com.something.arfurnitureapp;

public class OrderedUsersModel {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderedUsersModel(String name,String address,String phoneNo,String username,String image_name, String ordered_quantity, String product_doc_ref, String documentRef) {
        this.name = name;
        this.address = address;
        this.phoneNo = phoneNo;
        this.username = username;
        this.image_name =image_name;
        this.ordered_quantity = ordered_quantity;
        this.product_doc_ref = product_doc_ref;
        this.documentRef = documentRef;

    }
    public  OrderedUsersModel(){

    }
    String name;
    String address;
    String phoneNo;
    String username;
    String image_name;
    String ordered_quantity;

    public String getDocumentRef() {
        return documentRef;
    }

    public void setDocumentRef(String documentRef) {
        this.documentRef = documentRef;
    }

    String documentRef;

    public String getProduct_doc_ref() {
        return product_doc_ref;
    }

    public void setProduct_doc_ref(String product_doc_ref) {
        this.product_doc_ref = product_doc_ref;
    }



    String product_doc_ref;


    public String getOrdered_quantity() {
        return ordered_quantity;
    }

    public void setOrdered_quantity(String ordered_quantity) {
        this.ordered_quantity = ordered_quantity;
    }



    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
