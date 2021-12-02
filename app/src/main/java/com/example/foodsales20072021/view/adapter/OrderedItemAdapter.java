package com.example.foodsales20072021.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodsales20072021.R;
import com.example.foodsales20072021.databinding.ItemOrderBinding;
import com.example.foodsales20072021.model.OrderedItemModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class OrderedItemAdapter extends RecyclerView.Adapter<OrderedItemAdapter.OrderedItemViewHolder>{

    private List<OrderedItemModel> orderedItemModels;

    public OrderedItemAdapter(){

    }

    public void updateListOrderedItemModel(List<OrderedItemModel> lstOrderedItemModel){
        this.orderedItemModels = lstOrderedItemModel;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemOrderBinding itemOrderBinding = ItemOrderBinding.inflate(layoutInflater,parent,false);
        return new OrderedItemAdapter.OrderedItemViewHolder(itemOrderBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderedItemViewHolder holder, int position) {
        ((OrderedItemAdapter.OrderedItemViewHolder) holder).bind(orderedItemModels.get(position));
    }

    @Override
    public int getItemCount() {
        return orderedItemModels.size();
    }


    class OrderedItemViewHolder extends RecyclerView.ViewHolder {
        //Binding
        ItemOrderBinding mBinding;

        public OrderedItemViewHolder(ItemOrderBinding itemOrderBinding) {
            super(itemOrderBinding.getRoot());
            mBinding = itemOrderBinding;

        }

        void bind(OrderedItemModel orderedItemModel) {
            //Image
            //mBinding.getRoot: itemview (item_food)
            Glide.with(mBinding.getRoot().getContext())
                    .load(orderedItemModel.images.get(0).imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(mBinding.imageView);

            //Các label
            mBinding.textViewName.setText(orderedItemModel.foodName);
            NumberFormat formatter = new DecimalFormat("#,###");
            mBinding.textViewPrice.setText("Price: " + formatter.format(orderedItemModel.price) + "VND");
            mBinding.textviewQuantity.setText("Quantity: " + String.valueOf(orderedItemModel.quantity) );
            mBinding.textviewTotalAmount.setText("Total: " + formatter.format(orderedItemModel.price * orderedItemModel.quantity) + "VND");
        }
    }
}
