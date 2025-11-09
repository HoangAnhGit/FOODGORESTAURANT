package com.example.foodgorestaurant.View.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.foodgorestaurant.View.MenuFragment;
import com.example.foodgorestaurant.View.OrderFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MenuFragment();
            case 1:
                return new OrderFragment();
            default:
                return new MenuFragment(); // Default case
        }
    }

    @Override
    public int getItemCount() {
        return 2; // We have 2 tabs
    }
}
