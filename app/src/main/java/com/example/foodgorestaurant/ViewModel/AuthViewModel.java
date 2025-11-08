package com.example.foodgorestaurant.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.foodgorestaurant.Network.DTO.LoginResponse;
import com.example.foodgorestaurant.Network.DTO.RegisterResponse;
import com.example.foodgorestaurant.Repository.AuthRepository;
import com.example.foodgorestaurant.Utils.Result;


public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository();
    }


    public LiveData<Result<LoginResponse>> login(String phone, String password) {
        Context appContext = getApplication().getApplicationContext();
        return authRepository.login(appContext, phone, password);
    }

    public LiveData<Result<RegisterResponse>> registerRestaurant(
            String phone,
            String password,
            String confirm,
            String restaurantName,
            String address
    ) {
        return authRepository.registerRestaurant(phone, password, confirm, restaurantName, address);
    }
}