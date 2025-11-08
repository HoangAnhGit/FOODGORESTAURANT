package com.example.foodgorestaurant.Network;

import com.example.foodgorestaurant.Network.DTO.ApiResponse;
import com.example.foodgorestaurant.Network.DTO.LoginRequest;
import com.example.foodgorestaurant.Network.DTO.LoginResponse;
import com.example.foodgorestaurant.Network.DTO.RegisterRequest;
import com.example.foodgorestaurant.Network.DTO.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {
    @POST("/api/Auth/register/shipper")
    Call<RegisterResponse> registerRestaurant(@Body RegisterRequest request);


    @POST("/api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest body);

}
