package com.example.foodgorestaurant.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.foodgorestaurant.Network.DTO.FoodResponse;
import com.example.foodgorestaurant.Repository.FoodRepository;

public class FoodViewModel extends AndroidViewModel {

    private final FoodRepository foodRepository;
    private final MutableLiveData<FoodResponse> foodListResponse = new MutableLiveData<>();
    private int currentPage = 1;
    private static final int PAGE_SIZE = 10;

    public FoodViewModel(@NonNull Application application) {
        super(application);
        foodRepository = new FoodRepository(application);
    }

    public LiveData<FoodResponse> getFoodListResponse() {
        return foodListResponse;
    }

    public void fetchFoods() {
        // Reset page to 1 when fetching from start
        currentPage = 1;
        loadFoods();
    }

    public void fetchNextPage() {
        currentPage++;
        loadFoods();
    }

    private void loadFoods() {
        LiveData<FoodResponse> response = foodRepository.getFoods(currentPage, PAGE_SIZE, null);
        response.observeForever(apiResponse -> {
            foodListResponse.setValue(apiResponse);
        });
    }

    public void refresh() {
        fetchFoods();
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
