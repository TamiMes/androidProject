package com.example.androidproject_tamara_hen.models;

public class User {

    private String email;
    private String phone;

    public User(String email, String phone)
    {
        this.email = email;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
