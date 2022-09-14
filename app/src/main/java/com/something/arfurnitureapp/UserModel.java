package com.something.arfurnitureapp;

public class UserModel {

    String name;
    String address;
    String phone_no;
    String username;
    String user_id;
    String email;
    Boolean disable;
    private  UserModel(){

    }
    private  UserModel(String name,String address,String phone_no,String username,String user_id,String email,Boolean disable){
        this.name=name;
        this.address=address;
        this.phone_no=phone_no;
        this.username=username;
        this.user_id=user_id;
        this.email=email;
        this.disable=disable;
    }

    public  Boolean getDisable(){
        return disable;
    }
    public  void setDisable(Boolean disable){
        this.disable=disable;
    }


    public  String getUser_id(){
        return user_id;
    }
    public  void setUser_id(String user_id){
        this.user_id=user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getAddresss() {
        return address;
    }

    public void setAddresss(String address) {
        this.address = address;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String model_id) {
        this.username = username;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }






}
