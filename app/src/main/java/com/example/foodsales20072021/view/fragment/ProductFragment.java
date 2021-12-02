package com.example.foodsales20072021.view.fragment;

import androidx.fragment.app.Fragment;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsales20072021.MyApplication;
import com.example.foodsales20072021.R;
import com.example.foodsales20072021.api.RetrofitClient;
import com.example.foodsales20072021.databinding.ActivityHomeBinding;
import com.example.foodsales20072021.databinding.FragmentProductBinding;
import com.example.foodsales20072021.model.FoodModel;
import com.example.foodsales20072021.model.OrderModel;
import com.example.foodsales20072021.repository.AuthenRepository;
import com.example.foodsales20072021.repository.FoodRepository;
import com.example.foodsales20072021.utils.NavBadge;
import com.example.foodsales20072021.utils.TokenManager;
import com.example.foodsales20072021.view.adapter.FoodAdapter;
import com.example.foodsales20072021.view.dialog.DetailDialog;
import com.example.foodsales20072021.viewmodel.AuthenViewModel;
import com.example.foodsales20072021.viewmodel.FoodViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

    //Menu
    private int numOfProduct = 0;
    //Binding
    FragmentProductBinding mBinding;
    //Model
    FoodViewModel mFoodViewModel;

    //Recyclerview
    RecyclerView mRcvFood;
    List<FoodModel> mListFood;
    FoodAdapter mFoodAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentProductBinding.inflate(inflater,container, false);
        View view = mBinding.getRoot();

        initData();
        observerData();
        event();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Đặt phần giỏ hàng trong đây, nếu không khi thay đổi giỏ hàng ở CartActivity,
        //Back về thì giỏ hàng không được cập nhật. Khi startAcitvity thì HomeActivity chỉ bị stop,
        //Back về thì vào onStart
        //Hoặc: có thể truyền biến numOfProduct từ CartActivity về bằng Intent

        //Observe Total count cho giỏ hàng lần đầu
        mFoodViewModel.getTotalCount().observe(this, new Observer<OrderModel>() {
            @Override
            public void onChanged(OrderModel orderModel) {
                numOfProduct = orderModel.total;
                updateCartIcon(numOfProduct);
            }
        });
        //Load TotalCount (số sản phẩm trong cart)
        mFoodViewModel.fetchTotalCount();

    }

    private void initData(){
        //Menu
//        prepareOptionsMenu();

        //ViewModel
        mFoodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        mFoodViewModel.updateFoodRepository(((MyApplication)getActivity().getApplication()).foodRepository);

        //Recyclerview
        mRcvFood = mBinding.recyclerView;
        mFoodAdapter = new FoodAdapter();
        //Có thể bỏ dòng này do mặc định là LinearLayout
        mRcvFood.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvFood.setHasFixedSize(true);
        mRcvFood.setAdapter(mFoodAdapter);
    }

    private void event() {
        //Load data
        mFoodViewModel.fetchFoodsModel();

        //Event khi click Recyclerview
        mFoodAdapter.setOnFoodListener(new FoodAdapter.OnFoodListener() {
            @Override
            public void setOnFoodClickListener(int position, int type) {
                switch (type){
                    case 1: //Click vào item
//                        Intent intent = new Intent(HomeActivity.this,DetailActivity.class);
//                        intent.putExtra("food", mListFood.get(position));
//                        startActivity(intent);

                        DetailDialog.createDetailDialog(getActivity(), mListFood.get(position));
                        Toast.makeText(getContext(), "Open Product Detail", Toast.LENGTH_SHORT).show();
                        break;
                    case 2: //Click vào nút "Add to cart"
                        mFoodViewModel.fetchOrderModel(mListFood.get(position));
                        break;
                }

            }
        });
    }

    private void observerData() {
        //Observe List các sản phẩm
        mFoodViewModel.getFoodsModel().observe(getViewLifecycleOwner(), new Observer<List<FoodModel>>() {
            @Override
            public void onChanged(List<FoodModel> foodModels) {
                mFoodAdapter.updateListFoodModels(foodModels);
                mListFood = foodModels;
            }
        });


        //Observe Order object để cập nhật giỏ hàng
        mFoodViewModel.getOrderModelLiveData().observe(getViewLifecycleOwner(), new Observer<OrderModel>() {
            @Override
            public void onChanged(OrderModel orderModel) {
                numOfProduct = orderModel.total;
                updateCartIcon(numOfProduct);
            }
        });

        mFoodViewModel.getLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    mBinding.containerLoading.layoutLoading.setVisibility(View.VISIBLE);

                } else {
                    mBinding.containerLoading.layoutLoading.setVisibility(View.GONE);
                }
            }
        });

        mFoodViewModel.getError().observe(getViewLifecycleOwner(), new Observer<Throwable>() {
            @Override
            public void onChanged(Throwable throwable) {
                if (throwable != null){
                    Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateCartIcon(int product) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomHome);
        NavBadge.updateBadge(bottomNavigationView,R.id.menu_item_cart,product);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}

