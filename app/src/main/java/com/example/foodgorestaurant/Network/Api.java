package com.example.foodgorestaurant.Network;

import com.example.foodgorestaurant.Network.DTO.ApiResponse;
import com.example.foodgorestaurant.Network.DTO.FoodResponse;
import com.example.foodgorestaurant.Network.DTO.LoginRequest;
import com.example.foodgorestaurant.Network.DTO.LoginResponse;
import com.example.foodgorestaurant.Network.DTO.OrderDetail;
import com.example.foodgorestaurant.Network.DTO.OrderResponse;
import com.example.foodgorestaurant.Network.DTO.RegisterRequest;
import com.example.foodgorestaurant.Network.DTO.RegisterResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    // ... (existing methods)

    @POST("/api/Auth/register/shipper")
    Call<RegisterResponse> registerRestaurant(@Body RegisterRequest request);

    @POST("/api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest body);

    @Multipart
    @POST("/api/Restaurant/foods")
    Call<ApiResponse<Object>> createFood(
            @Part("DishName") RequestBody dishName,
            @Part("Description") RequestBody description,
            @Part("Price") RequestBody price,
            @Part("IsAvailable") RequestBody isAvailable,
            @Part MultipartBody.Part image
    );

    @GET("/api/Restaurant/foods")
    Call<FoodResponse> getFoods(
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize,
            @Query("isAvailable") Boolean isAvailable
    );

    @Multipart
    @PUT("/api/Restaurant/foods/{dishId}")
    Call<ApiResponse<Object>> updateFood(
            @Path("dishId") int dishId,
            @Part("DishName") RequestBody dishName,
            @Part("Description") RequestBody description,
            @Part("Price") RequestBody price,
            @Part("IsAvailable") RequestBody isAvailable,
            @Part MultipartBody.Part image
    );

    @DELETE("/api/Restaurant/foods/{dishId}")
    Call<ApiResponse<Object>> deleteFood(@Path("dishId") int dishId);

    @GET("/api/Restaurant/orders")
    Call<OrderResponse> getOrders(
            @Query("pageNumber") int pageNumber,
            @Query("pageSize") int pageSize,
            @Query("status") String status
    );

    @POST("/api/Restaurant/orders/{orderId}/confirm")
    Call<ApiResponse<Object>> confirmOrder(@Path("orderId") int orderId);

    @POST("/api/Restaurant/orders/{orderId}/prepare")
    Call<ApiResponse<Object>> prepareOrder(@Path("orderId") int orderId);

    @POST("/api/Restaurant/orders/{orderId}/ready")
    Call<ApiResponse<Object>> readyOrder(@Path("orderId") int orderId);

    @POST("/api/Restaurant/orders/{orderId}/cancel")
    Call<ApiResponse<Object>> cancelOrder(@Path("orderId") int orderId, @Body String reason);

    @GET("/api/Restaurant/orders/{orderId}")
    Call<OrderDetail> getOrderDetail(@Path("orderId") int orderId);
}
