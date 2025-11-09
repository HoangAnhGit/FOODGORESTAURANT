package com.example.foodgorestaurant.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.foodgorestaurant.Network.DTO.ApiResponse;
import com.example.foodgorestaurant.Network.DTO.OrderResponse;
import com.example.foodgorestaurant.Repository.FoodRepository;
import java.util.HashMap;
import java.util.Map;

public class OrderViewModel extends AndroidViewModel {

    private final FoodRepository foodRepository;
    private final Map<String, MutableLiveData<OrderResponse>> orderListsByStatus = new HashMap<>();
    private final MutableLiveData<ApiResponse<Object>> orderActionResponse = new MutableLiveData<>();
    private final Map<String, Integer> currentPageByStatus = new HashMap<>();
    private static final int PAGE_SIZE = 10;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        foodRepository = new FoodRepository(application);
    }

    public LiveData<OrderResponse> getOrderListByStatus(String status) {
        if (!orderListsByStatus.containsKey(status)) {
            orderListsByStatus.put(status, new MutableLiveData<>());
            currentPageByStatus.put(status, 1);
        }
        return orderListsByStatus.get(status);
    }

    public LiveData<ApiResponse<Object>> getOrderActionResponse() {
        return orderActionResponse;
    }

    public void fetchOrders(String status) {
        currentPageByStatus.put(status, 1);
        loadOrders(status);
    }

    public void fetchNextPage(String status) {
        int nextPage = getCurrentPage(status) + 1;
        currentPageByStatus.put(status, nextPage);
        loadOrders(status);
    }

    private void loadOrders(String status) {
        int page = getCurrentPage(status);
        LiveData<OrderResponse> response = foodRepository.getOrders(page, PAGE_SIZE, status);
        response.observeForever(apiResponse -> {
            if (orderListsByStatus.containsKey(status)) {
                orderListsByStatus.get(status).setValue(apiResponse);
            }
        });
    }

    public void confirmOrder(int orderId) {
        foodRepository.confirmOrder(orderId).observeForever(orderActionResponse::setValue);
    }

    public void prepareOrder(int orderId) {
        foodRepository.prepareOrder(orderId).observeForever(orderActionResponse::setValue);
    }

    public void readyOrder(int orderId) {
        foodRepository.readyOrder(orderId).observeForever(orderActionResponse::setValue);
    }

    public void cancelOrder(int orderId, String reason) {
        foodRepository.cancelOrder(orderId, reason).observeForever(orderActionResponse::setValue);
    }

    public void refresh(String status) {
        fetchOrders(status);
    }

    public int getCurrentPage(String status) {
        return currentPageByStatus.getOrDefault(status, 1);
    }
}
