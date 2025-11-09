package com.example.foodgorestaurant.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.foodgorestaurant.Network.DTO.OrderDetail;
import com.example.foodgorestaurant.Repository.FoodRepository;

public class OrderDetailViewModel extends AndroidViewModel {

    private final FoodRepository foodRepository;

    public OrderDetailViewModel(@NonNull Application application) {
        super(application);
        foodRepository = new FoodRepository(application);
    }

    public LiveData<OrderDetail> getOrderDetail(int orderId) {
        return foodRepository.getOrderDetail(orderId);
    }
}
