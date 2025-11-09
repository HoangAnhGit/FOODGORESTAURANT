package com.example.foodgorestaurant.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.foodgorestaurant.Network.DTO.Food;
import com.example.foodgorestaurant.R;
import com.example.foodgorestaurant.ViewModel.DetailFoodViewModel;

public class DetailFoodActivity extends AppCompatActivity {

    private DetailFoodViewModel viewModel;
    private ImageView imgDish;
    private EditText etDishName, etDescription, etPrice, etQuantity;
    private Switch switchIsAvailable;
    private Button btnDelete;
    private TextView tvTitle;
    private boolean isEditMode = false;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    viewModel.setImageUri(selectedImageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);

        viewModel = new ViewModelProvider(this).get(DetailFoodViewModel.class);

        initViews();
        setupObservers();
        setupClickListeners();

        handleIntent();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tv_title);
        imgDish = findViewById(R.id.img_dish);
        etDishName = findViewById(R.id.et_dishName);
        etDescription = findViewById(R.id.et_description);
        etPrice = findViewById(R.id.et_price);
        etQuantity = findViewById(R.id.et_quantity);
        switchIsAvailable = findViewById(R.id.switch_isAvailable);
        btnDelete = findViewById(R.id.btn_delete);
    }

    private void handleIntent() {
        if (getIntent().hasExtra("EDIT_FOOD_ITEM")) {
            isEditMode = true;
            Food foodToEdit = (Food) getIntent().getSerializableExtra("EDIT_FOOD_ITEM");
            viewModel.loadFood(foodToEdit);
        }

        if (isEditMode) {
            tvTitle.setText("Chỉnh sửa món ăn");
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setText("Thêm món ăn");
            btnDelete.setVisibility(View.GONE);
        }
    }

    private void populateUI(Food food) {
        etDishName.setText(food.getDishName());
        etDescription.setText(food.getDescription());
        if (food.getPrice() != null) {
            etPrice.setText(food.getPrice().toPlainString());
        }
        switchIsAvailable.setChecked(food.isAvailable());

        Glide.with(this)
                .load(food.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(imgDish);
    }

    private void setupObservers() {
        viewModel.getCurrentFood().observe(this, this::populateUI);

        viewModel.getImageUri().observe(this, uri -> {
            if (uri != null) {
                imgDish.setImageURI(uri);
            }
        });

        viewModel.getSaveResponse().observe(this, apiResponse -> {
            if (apiResponse != null) {
                if (apiResponse.isSuccess()) {
                    Toast.makeText(this, isEditMode ? "Cập nhật thành công!" : "Tạo món ăn thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Lỗi khi lưu: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getDeleteResponse().observe(this, apiResponse -> {
            if (apiResponse != null) {
                if (apiResponse.isSuccess()) {
                    Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Notify the list to refresh
                    finish();
                } else {
                    Toast.makeText(this, "Lỗi khi xóa: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupClickListeners() {
        findViewById(R.id.btn_chooseImage).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        findViewById(R.id.btn_save).setOnClickListener(v -> {
            String name = etDishName.getText().toString();
            String description = etDescription.getText().toString();
            String price = etPrice.getText().toString();
            boolean isAvailable = switchIsAvailable.isChecked();
            viewModel.saveFood(name, description, price, isAvailable);
        });

        btnDelete.setOnClickListener(v -> {
            showDeleteConfirmationDialog();
        });
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa món ăn này không?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    viewModel.deleteFood();
                })
                .setNegativeButton("Hủy", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
