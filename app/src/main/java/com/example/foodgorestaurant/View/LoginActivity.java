package com.example.foodgorestaurant.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodgorestaurant.ViewModel.AuthViewModel;
import com.example.foodgorestaurant.databinding.ActivityLoginBinding;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Khởi tạo ViewBinding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 2. Khởi tạo AuthViewModel
        // (ViewModel này được tạo ở các bước trước)
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // 3. Cài đặt các sự kiện click
        setupClickListeners();
    }

    private void setupClickListeners() {
        // Xử lý click nút "Đăng nhập"
        binding.btnLogin.setOnClickListener(v -> handleLogin());

        // Xử lý click chữ "Đăng ký ngay"
        binding.tvRegisterNow.setOnClickListener(v -> {
            // Mở màn hình Đăng ký
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Xử lý logic khi bấm nút Đăng nhập
     */
    private void handleLogin() {
        // Lấy dữ liệu từ EditText
        String phone = binding.edtPhone.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        // Kiểm tra (Validate)
        if (TextUtils.isEmpty(phone)) {
            binding.edtPhone.setError("Vui lòng nhập số điện thoại");
            binding.edtPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.edtPassword.setError("Vui lòng nhập mật khẩu");
            binding.edtPassword.requestFocus();
            return;
        }

        // Hiển thị trạng thái đang tải
        setLoading(true);

        // Gọi ViewModel để đăng nhập
        authViewModel.login(phone, password).observe(this, result -> {
            if (result == null) return;

            switch (result.status) {
                case LOADING:
                    // Đã xử lý ở setLoading(true)
                    break;
                case SUCCESS:
                    setLoading(false);
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình chính (MainActivity)
                    Intent intent = new Intent(this, MainActivity.class); // <-- Đổi tên nếu cần
                    // Xóa tất cả Activity cũ, không cho back lại màn hình Login
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    break;
                case ERROR:
                    setLoading(false);
                    // Hiển thị lỗi (ví dụ: "Sai số điện thoại hoặc mật khẩu")
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    /**
     * Hàm phụ trợ để quản lý trạng thái Tải (Loading)
     * @param isLoading true nếu đang tải, false nếu đã tải xong
     */
    private void setLoading(boolean isLoading) {
        if (isLoading) {
            binding.btnLogin.setEnabled(false); // Khóa nút
            binding.includeLoading.getRoot().setVisibility(View.VISIBLE); // Hiển thị ProgressBar
        } else {
            binding.btnLogin.setEnabled(true); // Mở khóa nút
            binding.includeLoading.getRoot().setVisibility(View.GONE); // Ẩn ProgressBar
        }
    }
}