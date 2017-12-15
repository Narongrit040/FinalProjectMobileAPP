package com.example.database.database;

/**
 * Created by Pkaypreak on 11/21/2017.
 */

public class UserInformation {

    public String name;
    public String address;
    public String number;
    public String age;
   // public String image;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public UserInformation(String address,String age, String name, String number){
        this.name = name;
        this.address= address;
        this.number= number;
        this.age=age;
        //this.age=image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
