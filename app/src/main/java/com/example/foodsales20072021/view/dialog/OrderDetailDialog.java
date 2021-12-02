package com.example.foodsales20072021.view.dialog;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsales20072021.R;
import com.example.foodsales20072021.databinding.DialogDetailBinding;
import com.example.foodsales20072021.databinding.DialogOrderDetailBinding;
import com.example.foodsales20072021.model.CartModel;
import com.example.foodsales20072021.model.FoodModel;
import com.example.foodsales20072021.model.OrderedItemModel;
import com.example.foodsales20072021.view.adapter.CartModelAdapter;
import com.example.foodsales20072021.view.adapter.OrderedItemAdapter;
import com.example.foodsales20072021.view.adapter.SliderAdapter;
import com.example.foodsales20072021.viewmodel.FoodViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDialog {

    public static void createOrderDetailDialog(Activity activity, List<OrderedItemModel>  lstOrderedItemModel) {

        DialogOrderDetailBinding mBinding;

        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBinding = DialogOrderDetailBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(mBinding.getRoot());
//        dialog.setContentView(R.layout.dialog_detail);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        //Hiển thị 80% chiều cao
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int dispayHeight = (int) Math.round(height * 0.8);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, dispayHeight);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;

        //Nút Close
        mBinding.tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //Hiển thị nội dung
        //Recyclerview
        RecyclerView recyclerView= mBinding.recyclerView;
        OrderedItemAdapter orderedItemAdapter = new OrderedItemAdapter();
        orderedItemAdapter.updateListOrderedItemModel(lstOrderedItemModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(orderedItemAdapter);


        //Hiển thị dialog
        dialog.show();
    }

}
