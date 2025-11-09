package com.example.foodgorestaurant.ViewModel;

import android.app.Application;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.foodgorestaurant.Network.DTO.ApiResponse;
import com.example.foodgorestaurant.Network.DTO.Food;
import com.example.foodgorestaurant.Repository.FoodRepository;

public class DetailFoodViewModel extends AndroidViewModel {

    private final FoodRepository foodRepository;
    private final MutableLiveData<Food> currentFood = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse<Object>> saveResponse = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse<Object>> deleteResponse = new MutableLiveData<>(); // For delete operations
    private final MutableLiveData<Uri> imageUri = new MutableLiveData<>();

    public DetailFoodViewModel(@NonNull Application application) {
        super(application);
        foodRepository = new FoodRepository(application);
    }

    // LiveData for observing
    public LiveData<Food> getCurrentFood() {
        return currentFood;
    }

    public LiveData<ApiResponse<Object>> getSaveResponse() {
        return saveResponse;
    }

    public LiveData<ApiResponse<Object>> getDeleteResponse() {
        return deleteResponse;
    }

    public LiveData<Uri> getImageUri() {
        return imageUri;
    }

    // Methods to interact with ViewModel
    public void loadFood(Food food) {
        currentFood.setValue(food);
    }

    public void setImageUri(Uri uri) {
        imageUri.setValue(uri);
    }

    public void saveFood(String name, String description, String priceStr, boolean isAvailable) {
        Food foodToSave = currentFood.getValue();
        Uri newImageUri = imageUri.getValue();
        Integer dishId = (foodToSave != null) ? foodToSave.getDishId() : null;

        LiveData<ApiResponse<Object>> response = (dishId == null)
                ? foodRepository.createFood(name, description, priceStr, isAvailable, newImageUri)
                : foodRepository.updateFood(dishId, name, description, priceStr, isAvailable, newImageUri);

        response.observeForever(apiResponse -> saveResponse.setValue(apiResponse));
    }

    public void deleteFood() {
        Food foodToDelete = currentFood.getValue();
        if (foodToDelete != null) {
            LiveData<ApiResponse<Object>> response = foodRepository.deleteFood(foodToDelete.getDishId());
            response.observeForever(apiResponse -> deleteResponse.setValue(apiResponse));
        }
    }
}
