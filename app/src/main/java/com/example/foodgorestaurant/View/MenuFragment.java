package com.example.foodgorestaurant.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.foodgorestaurant.Network.DTO.Food;
import com.example.foodgorestaurant.R;
import com.example.foodgorestaurant.View.Adapter.FoodAdapter;
import com.example.foodgorestaurant.ViewModel.FoodViewModel;

public class MenuFragment extends Fragment implements FoodAdapter.OnFoodItemClickListener {

    private FoodViewModel foodViewModel;
    private RecyclerView rvFoodList;
    private FoodAdapter foodAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        rvFoodList = view.findViewById(R.id.rv_food_list);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        setupRecyclerView();
        setupViewModel();
        setupSwipeRefresh();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the data every time the fragment becomes visible.
        // This solves both returning from an edit and switching tabs.
        if (foodViewModel != null) {
            foodViewModel.refresh();
        }
    }

    private void setupRecyclerView() {
        foodAdapter = new FoodAdapter(getContext(), this);
        rvFoodList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFoodList.setAdapter(foodAdapter);

        rvFoodList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == foodAdapter.getItemCount() - 1) {
                    foodViewModel.fetchNextPage();
                }
            }
        });
    }

    private void setupViewModel() {
        foodViewModel = new ViewModelProvider(requireActivity()).get(FoodViewModel.class);
        foodViewModel.getFoodListResponse().observe(getViewLifecycleOwner(), foodResponse -> {
            swipeRefreshLayout.setRefreshing(false);
            if (foodResponse != null && foodResponse.getData() != null) {
                if (foodViewModel.getCurrentPage() == 1) {
                    foodAdapter.setFoods(foodResponse.getData());
                } else {
                    foodAdapter.addFoods(foodResponse.getData());
                }
            } else if (foodResponse != null) {
                Toast.makeText(getContext(), "Lỗi: " + foodResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            foodViewModel.refresh();
        });
    }

    @Override
    public void onFoodItemClick(Food food) {
        Intent intent = new Intent(getActivity(), DetailFoodActivity.class);
        intent.putExtra("EDIT_FOOD_ITEM", food);
        // We no longer need ActivityResultLauncher, just start the activity.
        // The refresh logic is now in onResume().
        startActivity(intent);
    }
}
