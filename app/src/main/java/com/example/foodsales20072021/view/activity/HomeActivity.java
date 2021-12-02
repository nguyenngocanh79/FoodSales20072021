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
        //Đưa adapter vào ViewPager - khi vuốt sẽ đổi Fragment
        mHomeViewPagerAdapter = new HomeViewPagerAdapter(this,mListFragment);
        mBinding.viewPager2.setAdapter(mHomeViewPagerAdapter);
        //CartBadge
//        cartBadge = mBinding.bottomHome.getOrCreateBadge(R.id.menu_item_cart);
        //Bắt sự kiện khi click Bottom menu
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
        //Bắt sự kiện khi đổi trang - đổi các Bottom menu
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

//        //Từ Launching Activity hoặc Login vào
//        Intent intent = getIntent();
//        if(intent!=null){
//            int code = intent.getIntExtra("code",0);
//            //Nếu code là 404 tức đã confirm, không có sp trong cart
//            if(code != 200) {
//                numOfProduct = 0;
//                updateCartIcon(numOfProduct);
//            }
//        }
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
        //Phòng trường hợp getTotalCount không có kết quả (e.g code 404 (sau khi confirm)...)
        updateCartIcon(numOfProduct);
        //Load TotalCount (số sản phẩm trong cart)
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