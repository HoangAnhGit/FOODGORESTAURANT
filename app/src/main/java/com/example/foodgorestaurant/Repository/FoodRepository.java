package com.example.foodgorestaurant.Repository;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.foodgorestaurant.Network.Api;
import com.example.foodgorestaurant.Network.ApiClient;
import com.example.foodgorestaurant.Network.DTO.ApiResponse;
import com.example.foodgorestaurant.Network.DTO.FoodResponse;
import com.example.foodgorestaurant.Network.DTO.OrderDetail;
import com.example.foodgorestaurant.Network.DTO.OrderResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodRepository {
    private final Api api;
    private final Context context;

    public FoodRepository(Context context) {
        this.context = context.getApplicationContext();
        this.api = ApiClient.getClientAuth(this.context).create(Api.class);
    }

    // ... (existing methods) ...
    public LiveData<FoodResponse> getFoods(int pageNumber, int pageSize, Boolean isAvailable) {
        MutableLiveData<FoodResponse> result = new MutableLiveData<>();
        api.getFoods(pageNumber, pageSize, isAvailable).enqueue(new Callback<FoodResponse>() {
            @Override
            public void onResponse(Call<FoodResponse> call, Response<FoodResponse> response) {
                result.setValue(response.isSuccessful() ? response.body() : new FoodResponse());
            }
            @Override
            public void onFailure(Call<FoodResponse> call, Throwable t) {
                result.setValue(new FoodResponse());
            }
        });
        return result;
    }

    public LiveData<OrderResponse> getOrders(int pageNumber, int pageSize, String status) {
        MutableLiveData<OrderResponse> result = new MutableLiveData<>();
        api.getOrders(pageNumber, pageSize, status).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                } else {
                    OrderResponse errorResponse = new OrderResponse();
                    errorResponse.setMessage(response.message());
                    result.setValue(errorResponse);
                }
            }
            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                OrderResponse errorResponse = new OrderResponse();
                errorResponse.setMessage(t.getMessage());
                result.setValue(errorResponse);
            }
        });
        return result;
    }

    public LiveData<OrderDetail> getOrderDetail(int orderId) {
        MutableLiveData<OrderDetail> result = new MutableLiveData<>();
        api.getOrderDetail(orderId).enqueue(new Callback<OrderDetail>() {
            @Override
            public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {
                if (response.isSuccessful()) {
                    result.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<OrderDetail> call, Throwable t) {
                // Handle failure
            }
        });
        return result;
    }

    private LiveData<ApiResponse<Object>> handleOrderAction(Call<ApiResponse<Object>> call) {
        MutableLiveData<ApiResponse<Object>> result = new MutableLiveData<>();
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                result.setValue(new ApiResponse<>(response.isSuccessful(), response.message(), null));
            }
            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                result.setValue(new ApiResponse<>(false, t.getMessage(), null));
            }
        });
        return result;
    }

    public LiveData<ApiResponse<Object>> confirmOrder(int orderId) {
        return handleOrderAction(api.confirmOrder(orderId));
    }

    public LiveData<ApiResponse<Object>> prepareOrder(int orderId) {
        return handleOrderAction(api.prepareOrder(orderId));
    }

    public LiveData<ApiResponse<Object>> readyOrder(int orderId) {
        return handleOrderAction(api.readyOrder(orderId));
    }

    public LiveData<ApiResponse<Object>> cancelOrder(int orderId, String reason) {
        return handleOrderAction(api.cancelOrder(orderId, reason));
    }

    public LiveData<ApiResponse<Object>> deleteFood(int dishId) {
        MutableLiveData<ApiResponse<Object>> result = new MutableLiveData<>();
        api.deleteFood(dishId).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful()) {
                    result.setValue(new ApiResponse<>(true, "Xóa thành công", null));
                } else {
                    result.setValue(new ApiResponse<>(false, response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                result.setValue(new ApiResponse<>(false, t.getMessage(), null));
            }
        });
        return result;
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private File getFileFromUri(Uri uri) {
        String tempFileName = "upload_" + System.currentTimeMillis();
        File file = new File(context.getCacheDir(), tempFileName);
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri); FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    public LiveData<ApiResponse<Object>> createFood(String dishName, String description, String price, boolean isAvailable, Uri imageUri) {
        return saveFood(null, dishName, description, price, isAvailable, imageUri);
    }

    public LiveData<ApiResponse<Object>> updateFood(int dishId, String dishName, String description, String price, boolean isAvailable, Uri imageUri) {
        return saveFood(dishId, dishName, description, price, isAvailable, imageUri);
    }

    private LiveData<ApiResponse<Object>> saveFood(Integer dishId, String dishName, String description, String price, boolean isAvailable, Uri imageUri) {
        MutableLiveData<ApiResponse<Object>> result = new MutableLiveData<>();
        RequestBody dishNamePart = RequestBody.create(MultipartBody.FORM, dishName);
        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM, description);
        RequestBody pricePart = RequestBody.create(MultipartBody.FORM, price);
        RequestBody isAvailablePart = RequestBody.create(MultipartBody.FORM, String.valueOf(isAvailable));
        MultipartBody.Part imagePart = null;
        if (imageUri != null) {
            File file = getFileFromUri(imageUri);
            String fileName = getFileName(imageUri);
            if (file != null && file.exists()) {
                String mimeType = context.getContentResolver().getType(imageUri);
                RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
                imagePart = MultipartBody.Part.createFormData("ImageUrl", fileName, requestFile);
            }
        }

        Call<ApiResponse<Object>> call = (dishId == null)
                ? api.createFood(dishNamePart, descriptionPart, pricePart, isAvailablePart, imagePart)
                : api.updateFood(dishId, dishNamePart, descriptionPart, pricePart, isAvailablePart, imagePart);

        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<Object> successResponse = response.body();
                    if (successResponse == null) {
                        successResponse = new ApiResponse<>(true, "Thao tác thành công", null);
                    } else {
                        successResponse.setSuccess(true);
                    }
                    result.setValue(successResponse);
                } else {
                    result.setValue(new ApiResponse<>(false, response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                result.setValue(new ApiResponse<>(false, t.getMessage(), null));
            }
        });
        return result;
    }

}
