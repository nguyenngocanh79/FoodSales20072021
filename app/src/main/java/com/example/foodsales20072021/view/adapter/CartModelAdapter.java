package com.example.foodsales20072021.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsales20072021.databinding.ItemCartModelBinding;
import com.example.foodsales20072021.model.CartModel;
import com.example.foodsales20072021.model.OrderedItemModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;


public class CartModelAdapter extends RecyclerView.Adapter<CartModelAdapter.CartModelViewHolder>{

    private List<CartModel> lstCartModel;
    private OnCartModelListener onCartModelListener;

    public CartModelAdapter(){

    }

    public void updateListCartModel(List<CartModel> lstCartModel){
        this.lstCartModel = lstCartModel;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCartModelBinding itemCartModelBinding = ItemCartModelBinding.inflate(layoutInflater,parent,false);
        return new CartModelAdapter.CartModelViewHolder(itemCartModelBinding);
    }

    @Override
    public int getItemCount() {
        return lstCartModel.size();
    }

    @Override
    public void onBindViewHolder(@NonNull CartModelViewHolder holder, int position) {
        ((CartModelAdapter.CartModelViewHolder)holder).bind(holder.itemView,lstCartModel.get(position));
    }

    class CartModelViewHolder extends RecyclerView.ViewHolder{
        //Binding
        ItemCartModelBinding mBinding;

        public CartModelViewHolder(ItemCartModelBinding itemCartModelBinding){
            super(itemCartModelBinding.getRoot());
            mBinding = itemCartModelBinding;

        }
        void bind(View itemView, CartModel cartModel){
            //Các label
            mBinding.tvDateLabel.setText("Confirmed Date:");
            mBinding.tvProductLabel.setText("Number of Product:");
            mBinding.tvTotalLabel.setText("Total:");
            //Ngày confirm
            String confirmedDate = new SimpleDateFormat("yyyy, MMM dd - HH:mm:ss")
                        .format(cartModel.confirmedAt);
            mBinding.tvDate.setText(confirmedDate);

            //Số sản phẩm
            int product = 0;
            for(OrderedItemModel item:cartModel.items){
                product = product + item.quantity;
            }
            mBinding.tvProduct.setText(String.valueOf(product));

            //Số tiền
            NumberFormat formatter = new DecimalFormat("#,###");
            mBinding.tvTotal.setText(formatter.format(cartModel.total) + "đ");

            //Click vào thẻ Cart
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCartModelListener.setOnCartModelClickListener(getAbsoluteAdapterPosition());
                }
            });

//
//
//            mBinding.btnMinus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onCartListener.setOnOrderedItemClickListener(getAdapterPosition(),1);
//
//                }
//            });
//
//            mBinding.btnPlus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onCartListener.setOnOrderedItemClickListener(getAdapterPosition(),2);
//                }
//            });
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onCartListener.setOnOrderedItemClickListener(getAbsoluteAdapterPosition(),3);
//                }
//            });
        }
    }

    public interface OnCartModelListener {
        public void setOnCartModelClickListener(int position);
    }

    public void setOnOrderClickListener(OnCartModelListener onCartModelListener) {
        this.onCartModelListener = onCartModelListener;
    }

}
