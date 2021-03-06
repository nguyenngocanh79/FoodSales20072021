package com.example.foodsales20072021.view.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.foodsales20072021.MyApplication;
import com.example.foodsales20072021.R;
import com.example.foodsales20072021.databinding.ActivityHomeBinding;
import com.example.foodsales20072021.model.OrderModel;
import com.example.foodsales20072021.utils.NavBadge;
import com.example.foodsales20072021.utils.TokenManager;
import com.example.foodsales20072021.view.adapter.HomeViewPagerAdapter;
import com.example.foodsales20072021.view.fragment.CartFragment;
import com.example.foodsales20072021.view.fragment.OrderHistoryFragment;
import com.example.foodsales20072021.view.fragment.ProductFragment;
import com.example.foodsales20072021.viewmodel.FoodViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding mBinding;
    List<Fragment> mListFragment;
    HomeViewPagerAdapter mHomeViewPagerAdapter;

//    BadgeDrawable cartBadge;
    private int numOfProduct = 0;
    //Model
    FoodViewModel mFoodViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //ViewModel
        mFoodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        mFoodViewModel.updateFoodRepository(((MyApplication) getApplication()).foodRepository);

        //Toobar
        mBinding.toolbarHome.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return menuItemSelected(item);
            }
        });

        //Bottom menu
        mListFragment = new ArrayList<>();
        mListFragment.add(new ProductFragment());
        mListFragment.add(new CartFragment());
        mListFragment.add(new OrderHistoryFragment());
        //????a adapter v??o ViewPager - khi vu???t s??? ?????i Fragment
        mHomeViewPagerAdapter = new HomeViewPagerAdapter(this,mListFragment);
        mBinding.viewPager2.setAdapter(mHomeViewPagerAdapter);
        //CartBadge
//        cartBadge = mBinding.bottomHome.getOrCreateBadge(R.id.menu_item_cart);
        //B???t s??? ki???n khi click Bottom menu
        mBinding.bottomHome.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_item_product:
                        mBinding.viewPager2.setCurrentItem(0);
                        break;
                    case R.id.menu_item_cart:
                        mBinding.viewPager2.setCurrentItem(1);
                        break;
                    case R.id.menu_item_orderhistory:
                        mBinding.viewPager2.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
        //B???t s??? ki???n khi ?????i trang - ?????i c??c Bottom menu
        mBinding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mBinding.bottomHome.getMenu().findItem(R.id.menu_item_product).setChecked(true);
                        break;
                    case 1:
                        mBinding.bottomHome.getMenu().findItem(R.id.menu_item_cart).setChecked(true);
                        break;
                    case 2:
                        mBinding.bottomHome.getMenu().findItem(R.id.menu_item_orderhistory).setChecked(true);
                        break;
                }
                super.onPageSelected(position);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        //?????t ph???n gi??? h??ng trong ????y, n???u kh??ng khi thay ?????i gi??? h??ng ??? CartActivity,
        //Back v??? th?? gi??? h??ng kh??ng ???????c c???p nh???t. Khi startAcitvity th?? HomeActivity ch??? b??? stop,
        //Back v??? th?? v??o onStart
        //Ho???c: c?? th??? truy???n bi???n numOfProduct t??? CartActivity v??? b???ng Intent

        //Observe Total count cho gi??? h??ng l???n ?????u
        mFoodViewModel.getTotalCount().observe(this, new Observer<OrderModel>() {
            @Override
            public void onChanged(OrderModel orderModel) {
                numOfProduct = orderModel.total;
                updateCartIcon(numOfProduct);
            }
        });
        //Ph??ng tr?????ng h???p getTotalCount kh??ng c?? k???t qu??? (e.g code 404 (sau khi confirm)...)
        updateCartIcon(numOfProduct);
        //Load TotalCount (s??? s???n ph???m trong cart)
        mFoodViewModel.fetchTotalCount();

    }

    private void updateCartIcon(int product) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomHome);
        NavBadge.updateBadge(bottomNavigationView,R.id.menu_item_cart,product);
    }

    //Select Toolbar
    public boolean menuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.product_menu_log_out:
                Toast.makeText(HomeActivity.this, "Log out Clicked", Toast.LENGTH_SHORT).show();
                TokenManager tokenManager = TokenManager.getInstance();
                tokenManager.removeAuthToken(tokenManager.getUserTokenKey());
                finishAffinity();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (mBinding.viewPager2.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, to Product.
            mBinding.viewPager2.setCurrentItem(0);
        }
    }

}