package com.example.foodgorestaurant.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.foodgorestaurant.Network.DTO.Order;
import com.example.foodgorestaurant.R;
import com.example.foodgorestaurant.View.Adapter.OrderAdapter;
import com.example.foodgorestaurant.ViewModel.OrderViewModel;

public class OrderListFragment extends Fragment implements OrderAdapter.OnOrderActionListener {

    private static final String ARG_STATUS = "order_status";
    private OrderViewModel orderViewModel;
    private RecyclerView rvOrderList;
    private OrderAdapter orderAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String orderStatus;

    public static OrderListFragment newInstance(String status) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderStatus = getArguments().getString(ARG_STATUS);
        }
        orderViewModel = new ViewModelProvider(requireActivity()).get(OrderViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        rvOrderList = view.findViewById(R.id.rv_order_list);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_order);

        setupRecyclerView();
        setupViewModel();
        setupSwipeRefresh();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        orderViewModel.refresh(orderStatus);
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(this, orderStatus);
        rvOrderList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrderList.setAdapter(orderAdapter);

        rvOrderList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == orderAdapter.getItemCount() - 1) {
                    orderViewModel.fetchNextPage(orderStatus);
                }
            }
        });
    }

    private void setupViewModel() {
        orderViewModel.getOrderListByStatus(orderStatus).observe(getViewLifecycleOwner(), orderResponse -> {
            swipeRefreshLayout.setRefreshing(false);
            if (orderResponse != null && orderResponse.getData() != null) {
                if (orderViewModel.getCurrentPage(orderStatus) == 1) {
                    orderAdapter.setOrders(orderResponse.getData());
                } else {
                    orderAdapter.addOrders(orderResponse.getData());
                }
            } else if (orderResponse != null) {
                Toast.makeText(getContext(), "Lỗi: " + orderResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        orderViewModel.getOrderActionResponse().observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse != null && apiResponse.isSuccess()) {
                Toast.makeText(getContext(), "Thao tác thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> orderViewModel.refresh(orderStatus));
    }

    @Override
    public void onOrderItemClick(Order order) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, order.getOrderId());
        startActivity(intent);
    }

    @Override
    public void onNextActionClick(Order order) {
        if (orderStatus == null) return;

        switch (orderStatus) {
            case "PENDING":
                orderViewModel.confirmOrder(order.getOrderId());
                break;
            case "CONFIRMED":
                orderViewModel.prepareOrder(order.getOrderId());
                break;
            case "PREPARING":
                orderViewModel.readyOrder(order.getOrderId());
                break;
        }
    }

    @Override
    public void onCancelOrderClick(Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Hủy đơn hàng");
        builder.setMessage("Nhập lý do hủy đơn hàng:");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Xác nhận hủy", (dialog, which) -> {
            String reason = input.getText().toString();
            if (reason.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập lý do hủy.", Toast.LENGTH_SHORT).show();
            } else {
                orderViewModel.cancelOrder(order.getOrderId(), reason);
            }
        });
        builder.setNegativeButton("Bỏ qua", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
