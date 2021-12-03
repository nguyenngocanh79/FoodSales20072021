package com.example.foodsales20072021.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.foodsales20072021.R;
import com.example.foodsales20072021.model.ImageModel;
import com.smarteist.autoimageslider.SliderViewAdapter;


import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder>{

    List<ImageModel> imgModels;
    public SliderAdapter(List<ImageModel> imgModels){
        this.imgModels = imgModels;
    }

    Context context;
    public void setContext(Context context){
        this.context = context;
    }



    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {

//        viewHolder.imageView.setImageResource(images.get(position));
        Glide.with(viewHolder.itemView)
                .load(imgModels.get(position).imageUrl)
                .fitCenter()
                .into(viewHolder.imageView);

    }

    @Override
    public int getCount() {
//        return images.size();
        return imgModels.size();
    }

    public class Holder extends ViewHolder{
        ImageView imageView;
        public Holder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
