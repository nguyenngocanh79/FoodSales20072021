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


import com.example.foodsales20072021.R;
import com.example.foodsales20072021.databinding.DialogDetailBinding;
import com.example.foodsales20072021.model.FoodModel;
import com.example.foodsales20072021.view.adapter.SliderAdapter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class DetailDialog {



    public static void createDetailDialog(Activity activity, FoodModel foodModel) {

        DialogDetailBinding mBinding;

        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBinding = DialogDetailBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(mBinding.getRoot());
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        //Hiển thị 80% chiều chao
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
        SliderAdapter sliderAdapter = new SliderAdapter(foodModel.images);
        //Slider
        sliderAdapter.setContext(activity);
        mBinding.imageSliderFood.setSliderAdapter(sliderAdapter);
        mBinding.imageSliderFood.setIndicatorAnimation(IndicatorAnimationType.WORM);
        mBinding.imageSliderFood.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        mBinding.imageSliderFood.startAutoCycle();
        //Các textview

        mBinding.tvSliderFoodName.setText(foodModel.foodName);
        NumberFormat formatter = new DecimalFormat("#,###");
        mBinding.tvSliderFoodPrice.setText(formatter.format(foodModel.price) + " đ");
        mBinding.tvSliderFoodDescription.setText(foodModel.description);

        //Hiển thị dialog
        dialog.show();
    }
}
