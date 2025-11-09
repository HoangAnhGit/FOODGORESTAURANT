package com.example.foodgorestaurant.View;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodgorestaurant.Network.DTO.Customer;
import com.example.foodgorestaurant.Network.DTO.OrderDetail;
import com.example.foodgorestaurant.R;
import com.example.foodgorestaurant.View.Adapter.OrderDetailItemAdapter;
import com.example.foodgorestaurant.ViewModel.OrderDetailViewModel;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_ID = "extra_order_id";

    private OrderDetailViewModel viewModel;
    private TextView tvCustomerName, tvCustomerPhone, tvCustomerAddress;
    private TextView tvSubtotal, tvShippingFee, tvTotalAmount;
    private RecyclerView rvOrderItems;
    private OrderDetailItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        viewModel = new ViewModelProvider(this).get(OrderDetailViewModel.class);

        initViews();
        setupRecyclerView();

        int orderId = getIntent().getIntExtra(EXTRA_ORDER_ID, -1);
        if (orderId != -1) {
            viewModel.getOrderDetail(orderId).observe(this, this::populateUi);
        }
    }

    private void initViews() {
        tvCustomerName = findViewById(R.id.tv_detail_customer_name);
        tvCustomerPhone = findViewById(R.id.tv_detail_customer_phone);
        tvCustomerAddress = findViewById(R.id.tv_detail_customer_address);
        tvSubtotal = findViewById(R.id.tv_detail_subtotal);
        tvShippingFee = findViewById(R.id.tv_detail_shipping_fee);
        tvTotalAmount = findViewById(R.id.tv_detail_total_amount);
        rvOrderItems = findViewById(R.id.rv_order_items);
    }

    private void setupRecyclerView() {
        adapter = new OrderDetailItemAdapter();
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        rvOrderItems.setAdapter(adapter);
    }

    private void populateUi(OrderDetail orderDetail) {
        if (orderDetail == null) return;

        // Customer Info
        Customer customer = orderDetail.getCustomer();
        if (customer != null) {
            tvCustomerName.setText(String.format("Tên: %s", customer.getCustomerName()));
            tvCustomerPhone.setText(String.format("SĐT: %s", customer.getCustomerPhone()));
            tvCustomerAddress.setText(String.format("Địa chỉ: %s", customer.getDeliveryAddress()));
        }

        // Items
        if (orderDetail.getItems() != null) {
            adapter.setItems(orderDetail.getItems());
        }

        // Totals
        String subtotalStr = "0đ";
        String shippingFeeStr = "0đ";
        String totalAmountStr = "0đ";

        if (orderDetail.getSubtotal() != null) {
            subtotalStr = String.format(Locale.GERMANY, "%,.0fđ", orderDetail.getSubtotal());
        }
        if (orderDetail.getShippingFee() != null) {
            shippingFeeStr = String.format(Locale.GERMANY, "%,.0fđ", orderDetail.getShippingFee());
        }
        if (orderDetail.getTotalAmount() != null) {
            totalAmountStr = String.format(Locale.GERMANY, "%,.0fđ", orderDetail.getTotalAmount());
        }

        tvSubtotal.setText(String.format("Tạm tính: %s", subtotalStr));
        tvShippingFee.setText(String.format("Phí giao hàng: %s", shippingFeeStr));
        tvTotalAmount.setText(totalAmountStr);
    }
}
