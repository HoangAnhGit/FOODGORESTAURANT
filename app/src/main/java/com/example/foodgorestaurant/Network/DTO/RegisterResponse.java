package com.example.foodgorestaurant.Network.DTO;

public class RegisterResponse {
    private String title;
    private String status;
    private Object errors;

    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public Object getErrors() { return errors; }
}
