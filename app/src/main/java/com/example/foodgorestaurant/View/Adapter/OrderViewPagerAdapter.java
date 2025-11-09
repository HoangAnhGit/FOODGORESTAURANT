package com.example.foodgorestaurant.View.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.foodgorestaurant.View.OrderListFragment;

public class OrderViewPagerAdapter extends FragmentStateAdapter {

    // Removed the "READY" status
    private static final String[] TAB_STATUSES = {"PENDING", "CONFIRMED", "PREPARING"};

    public OrderViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return OrderListFragment.newInstance(TAB_STATUSES[position]);
    }

    @Override
    public int getItemCount() {
        return TAB_STATUSES.length;
    }
}
