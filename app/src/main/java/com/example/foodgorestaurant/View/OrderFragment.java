package com.example.foodgorestaurant.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.foodgorestaurant.R;
import com.example.foodgorestaurant.View.Adapter.OrderViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrderFragment extends Fragment {

    // Removed the "Sẵn sàng giao" tab
    private static final String[] TAB_TITLES = {"Chờ xác nhận", "Đã xác nhận", "Đang chuẩn bị"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        TabLayout tabLayout = view.findViewById(R.id.order_tab_layout);
        ViewPager2 viewPager = view.findViewById(R.id.order_view_pager);

        OrderViewPagerAdapter adapter = new OrderViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(TAB_TITLES[position]);
        }).attach();

        return view;
    }
}
