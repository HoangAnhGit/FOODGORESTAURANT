package com.example.foodgorestaurant.Repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodgorestaurant.Network.Api;
import com.example.foodgorestaurant.Network.ApiClient;
import com.example.foodgorestaurant.Network.DTO.LoginRequest;
import com.example.foodgorestaurant.Network.DTO.LoginResponse;
import com.example.foodgorestaurant.Network.DTO.RegisterRequest;
import com.example.foodgorestaurant.Network.DTO.RegisterResponse;
import com.example.foodgorestaurant.Network.TokenManager;
import com.example.foodgorestaurant.Utils.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final Api authApi;

    public AuthRepository() {
        authApi = ApiClient.getClient().create(Api.class);
    }

    /**
     * Hàm xử lý Đăng nhập
     */
    public LiveData<Result<LoginResponse>> login(Context context, String phone, String password) {
        MutableLiveData<Result<LoginResponse>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        LoginRequest request = new LoginRequest(phone, password);

        authApi.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse data = response.body();
                    // Lưu token khi đăng nhập thành công
                    TokenManager.getInstance(context).saveToken(data.getToken(), data.getUserType());
                    resultLiveData.setValue(Result.success(response.body()));
                } else {
                    resultLiveData.setValue(Result.error("Sai số điện thoại hoặc mật khẩu"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                resultLiveData.setValue(Result.error("Lỗi kết nối: " + t.getMessage()));
            }
        });

        return resultLiveData;
    }


    public LiveData<Result<RegisterResponse>> registerRestaurant(
            String phone,
            String password,
            String confirm,
            String restaurantName,
            String address
    ) {
        MutableLiveData<Result<RegisterResponse>> resultRegister = new MutableLiveData<>();
        resultRegister.setValue(Result.loading());

        RegisterRequest request = new RegisterRequest(
                phone, password, confirm, restaurantName, address
        );

        // Gọi hàm API mới: registerRestaurant
        authApi.registerRestaurant(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultRegister.setValue(Result.success(response.body()));
                } else {
                    // Log lỗi chi tiết (giúp bạn debug lỗi 500, 400)
                    String errorMsg = "Đăng ký thất bại (Mã: " + response.code() + ")";
                    Log.e("AuthRepository", "Lỗi đăng ký nhà hàng: " + response.code() + " - " + response.message());
                    resultRegister.setValue(Result.error(errorMsg));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                Log.e("AuthRepository", "Lỗi kết nối đăng ký", t);
                resultRegister.setValue(Result.error("Lỗi kết nối: " + t.getMessage()));
            }
        });

        return resultRegister;
    }
    public void logout(Context context) {
        TokenManager.getInstance(context).clear();
    }

}
