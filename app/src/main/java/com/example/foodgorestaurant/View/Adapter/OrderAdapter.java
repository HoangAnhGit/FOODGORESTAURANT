package com.example.foodgorestaurant.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodgorestaurant.Network.DTO.Order;
import com.example.foodgorestaurant.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList = new ArrayList<>();
    private final OnOrderActionListener listener;
    private final String tabStatus;

    public interface OnOrderActionListener {
        void onOrderItemClick(Order order); // For clicking the whole item
        void onNextActionClick(Order order);
        void onCancelOrderClick(Order order);
    }

    public OrderAdapter(OnOrderActionListener listener, String tabStatus) {
        this.listener = listener;
        this.tabStatus = tabStatus;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view, tabStatus);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order, listener);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void setOrders(List<Order> orders) {
        this.orderList = orders;
        notifyDataSetChanged();
    }

    public void addOrders(List<Order> orders) {
        int startPosition = orderList.size();
        orderList.addAll(orders);
        notifyItemRangeInserted(startPosition, orders.size());
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvCustomerName, tvOrderId, tvItemCount, tvDistance, tvPrice, tvStatusDisplay;
        Button btnNextAction, btnCancelOrder;

        public OrderViewHolder(@NonNull View itemView, String tabStatus) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tv_customerName);
            tvOrderId = itemView.findViewById(R.id.tv_orderId);
            tvItemCount = itemView.findViewById(R.id.tv_itemCount);
            tvDistance = itemView.findViewById(R.id.tv_distance);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvStatusDisplay = itemView.findViewById(R.id.tv_status_display);
            btnNextAction = itemView.findViewById(R.id.btn_next_action);
            btnCancelOrder = itemView.findViewById(R.id.btn_cancel_order);

            btnNextAction.setVisibility(View.VISIBLE);
            switch (tabStatus) {
                case "PENDING":
                    btnNextAction.setText("Xác nhận");
                    break;
                case "CONFIRMED":
                    btnNextAction.setText("Chuẩn bị xong");
                    break;
                case "PREPARING":
                    btnNextAction.setText("Sẵn sàng giao");
                    break;
                default:
                    btnNextAction.setVisibility(View.GONE);
                    break;
            }
        }

        void bind(final Order order, final OnOrderActionListener listener) {
            tvCustomerName.setText(order.getCustomerName());
            tvOrderId.setText(order.getOrderCode());
            tvItemCount.setText(String.valueOf(order.getItemCount()));
            tvDistance.setText(String.format(Locale.getDefault(), "%.1fkm", order.getDistance()));
            tvStatusDisplay.setText(order.getStatusDisplay());
            if (order.getTotalPrice() != null) {
                String priceString = String.format(Locale.GERMANY, "%,.0fđ", order.getTotalPrice());
                tvPrice.setText(priceString);
            }

            // Set listeners
            itemView.setOnClickListener(v -> listener.onOrderItemClick(order));
            btnNextAction.setOnClickListener(v -> listener.onNextActionClick(order));
            btnCancelOrder.setOnClickListener(v -> listener.onCancelOrderClick(order));
        }
    }
}
