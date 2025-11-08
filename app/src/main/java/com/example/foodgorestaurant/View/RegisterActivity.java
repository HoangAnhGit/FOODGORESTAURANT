package com.example.foodgorestaurant.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodgorestaurant.ViewModel.AuthViewModel;
import com.example.foodgorestaurant.databinding.ActivityRegisterBinding;


public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Khởi tạo ViewBinding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 2. Khởi tạo AuthViewModel
        // (ViewModel này được tạo ở các bước trước)
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // 3. Cài đặt các sự kiện click
        setupClickListeners();
    }

    private void setupClickListeners() {
        // Xử lý click nút "Đăng ký"
        binding.btnRegister.setOnClickListener(v -> handleRegister());

        // Xử lý click chữ "Đăng nhập ngay"
        binding.tvLoginNow.setOnClickListener(v -> {
            // Quay lại màn hình Đăng nhập
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Đóng Activity hiện tại
        });
    }

    /**
     * Xử lý logic khi bấm nút Đăng ký
     */
    private void handleRegister() {
        // Lấy dữ liệu từ 5 EditText
        String phone = binding.edtPhone.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();
        String confirmPassword = binding.edtPasswordAgain.getText().toString().trim();
        String restaurantName = binding.edtRestaurantName.getText().toString().trim();
        String address = binding.edtAddress.getText().toString().trim();

        // 4. Kiểm tra (Validate) dữ liệu
        if (!validateInput(phone, password, confirmPassword, restaurantName, address)) {
            return; // Dừng lại nếu có lỗi
        }

        // 5. Hiển thị trạng thái đang tải
        setLoading(true);

        // 6. Gọi ViewModel để đăng ký (sử dụng hàm registerRestaurant)
        authViewModel.registerRestaurant(phone, password, confirmPassword, restaurantName, address)
                .observe(this, result -> {
                    if (result == null) return;

                    switch (result.status) {
                        case LOADING:
                            // Đã xử lý ở setLoading(true)
                            break;
                        case SUCCESS:
                            setLoading(false);
                            Toast.makeText(this, "Đăng ký nhà hàng thành công!", Toast.LENGTH_SHORT).show();

                            // Chuyển về màn hình Đăng nhập
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            // Xóa tất cả Activity cũ
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            break;
                        case ERROR:
                            setLoading(false);
                            // Hiển thị lỗi (ví dụ: "SĐT đã tồn tại", "Lỗi 500")
                            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
    }

    /**
     * Hàm kiểm tra 5 trường dữ liệu đầu vào
     * @return true nếu tất cả hợp lệ, false nếu có lỗi
     */
    private boolean validateInput(String phone, String password, String confirm, String restaurantName, String address) {
        if (TextUtils.isEmpty(phone)) {
            binding.edtPhone.setError("Vui lòng nhập số điện thoại");
            binding.edtPhone.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            binding.edtPassword.setError("Vui lòng nhập mật khẩu");
            binding.edtPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            binding.edtPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            binding.edtPassword.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(confirm)) {
            binding.edtPasswordAgain.setError("Vui lòng xác nhận mật khẩu");
            binding.edtPasswordAgain.requestFocus();
            return false;
        }

        if (!password.equals(confirm)) {
            binding.edtPasswordAgain.setError("Mật khẩu không khớp");
            binding.edtPasswordAgain.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(restaurantName)) {
            binding.edtRestaurantName.setError("Vui lòng nhập tên nhà hàng");
            binding.edtRestaurantName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            binding.edtAddress.setError("Vui lòng nhập địa chỉ");
            binding.edtAddress.requestFocus();
            return false;
        }

        return true; // Tất cả đều hợp lệ
    }

    /**
     * Hàm phụ trợ để quản lý trạng thái Tải (Loading)
     * @param isLoading true nếu đang tải, false nếu đã tải xong
     */
    private void setLoading(boolean isLoading) {
        if (isLoading) {
            binding.btnRegister.setEnabled(false); // Khóa nút
            binding.includeLoading.getRoot().setVisibility(View.VISIBLE); // Hiển thị ProgressBar
        } else {
            binding.btnRegister.setEnabled(true); // Mở khóa nút
            binding.includeLoading.getRoot().setVisibility(View.GONE); // Ẩn ProgressBar
        }
    }
}