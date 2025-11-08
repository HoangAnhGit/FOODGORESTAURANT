package com.example.foodgorestaurant.Network.DTO;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("password")
    private String password;

    @SerializedName("confirmPassword")
    private String confirmPassword;

    @SerializedName("restaurantName")
    private String restaurantName;

    @SerializedName("address")
    private String address;

    // Constructor để tạo đối tượng
    public RegisterRequest(String phoneNumber, String password, String confirmPassword, String restaurantName, String address) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.restaurantName = restaurantName;
        this.address = address;
    }

    // Getters (không bắt buộc cho request body, nhưng nên có)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getAddress() {
        return address;
    }
}
