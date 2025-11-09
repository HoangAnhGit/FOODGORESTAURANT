package com.example.foodgorestaurant.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.foodgorestaurant.Network.DTO.Food;
import com.example.foodgorestaurant.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<Food> foodList = new ArrayList<>();
    private final Context context;
    private final OnFoodItemClickListener listener;

    public interface OnFoodItemClickListener {
        void onFoodItemClick(Food food);
    }

    public FoodAdapter(Context context, OnFoodItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.bind(food, listener);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void setFoods(List<Food> foods) {
        this.foodList = foods;
        notifyDataSetChanged();
    }

    public void addFoods(List<Food> foods) {
        int startPosition = foodList.size();
        foodList.addAll(foods);
        notifyItemRangeInserted(startPosition, foods.size());
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoodImage;
        TextView tvFoodName, tvFoodDescription, tvFoodPrice;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoodImage = itemView.findViewById(R.id.iv_food_image);
            tvFoodName = itemView.findViewById(R.id.tv_food_name);
            tvFoodDescription = itemView.findViewById(R.id.tv_food_description);
            tvFoodPrice = itemView.findViewById(R.id.tv_food_price);
        }

        void bind(final Food food, final OnFoodItemClickListener listener) {
            tvFoodName.setText(food.getDishName());
            tvFoodDescription.setText(food.getDescription());
            if (food.getPrice() != null) {
                String priceString = String.format(Locale.GERMANY, "%,.0f VNĐ", food.getPrice());
                tvFoodPrice.setText(priceString);
            }

            Glide.with(context)
                    .load(food.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(ivFoodImage);

            itemView.setOnClickListener(v -> listener.onFoodItemClick(food));
        }
    }
}
