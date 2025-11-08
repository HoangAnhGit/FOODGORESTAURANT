package com.example.foodgorestaurant.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodgorestaurant.databinding.FragmentFoodBinding;
import com.google.android.material.tabs.TabLayoutMediator;


public class FoodFragment extends Fragment {

    private FragmentFoodBinding binding; // ✅ ViewBinding thay thế findViewById

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = FragmentFoodBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        OrdersPagerAdapter adapter = new OrdersPagerAdapter(requireActivity());
        binding.viewPagerOrders.setAdapter(adapter);


        new TabLayoutMediator(binding.tabLayoutOrders, binding.viewPagerOrders, (tab, position) -> {
            if (position == 0) {
                tab.setText("Đơn hàng");
            } else {
                tab.setText("Thực đơn");
            }
        }).attach();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}