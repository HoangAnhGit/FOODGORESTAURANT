package com.example.foodgorestaurant.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodgorestaurant.Network.DTO.OrderItem;
import com.example.foodgorestaurant.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderDetailItemAdapter extends RecyclerView.Adapter<OrderDetailItemAdapter.ViewHolder> {

    private List<OrderItem> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail_dish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuantity, tvName, tvTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuantity = itemView.findViewById(R.id.tv_item_quantity);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvTotal = itemView.findViewById(R.id.tv_item_total);
        }

        void bind(OrderItem item) {
            tvQuantity.setText(String.format(Locale.getDefault(), "%dx", item.getQuantity()));
            tvName.setText(item.getDishName());
            if (item.getTotal() != null) {
                String totalString = String.format(Locale.GERMANY, "%,.0fđ", item.getTotal());
                tvTotal.setText(totalString);
            }
        }
    }
}
