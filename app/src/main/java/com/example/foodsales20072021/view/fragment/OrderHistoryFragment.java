package com.example.foodsales20072021.view.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodsales20072021.MyApplication;
import com.example.foodsales20072021.R;
import com.example.foodsales20072021.databinding.FragmentCartBinding;
import com.example.foodsales20072021.databinding.FragmentOrderHistoryBinding;
import com.example.foodsales20072021.model.CartModel;
import com.example.foodsales20072021.model.OrderedItemModel;
import com.example.foodsales20072021.utils.TokenManager;
import com.example.foodsales20072021.view.adapter.CartAdapter;
import com.example.foodsales20072021.view.adapter.CartModelAdapter;
import com.example.foodsales20072021.view.dialog.OrderDetailDialog;
import com.example.foodsales20072021.viewmodel.FoodViewModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryFragment extends Fragment {

    //Binding
    FragmentOrderHistoryBinding mBinding;
    //Model
    FoodViewModel mFoodViewModel;
    //Recyclerview
    RecyclerView mRcvCartModel;
    List<CartModel> mCartModelList;
    CartModelAdapter mCartModelAdapter;

    //Firestore
    FirebaseFirestore db;
    CartModel mCartModel;
    ListenerRegistration listenerRegistration;
//    List<CartModel> mCartModelList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentOrderHistoryBinding.inflate(inflater,container, false);
        View view = mBinding.getRoot();

        //Firestore
        db = FirebaseFirestore.getInstance();
        mCartModelList = new ArrayList<>();

        //ViewModel
        mFoodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        mFoodViewModel.updateFoodRepository(((MyApplication)getActivity().getApplication()).foodRepository);

        //Recyclerview
        mRcvCartModel= mBinding.recyclerView;
        mCartModelList = new ArrayList<>();
        mCartModelAdapter = new CartModelAdapter();
        mCartModelAdapter.updateListCartModel(mCartModelList);
        mRcvCartModel.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvCartModel.setHasFixedSize(true);
        mRcvCartModel.setAdapter(mCartModelAdapter);

        observerData();
        event();

        return view;

    }

    private void observerData(){

    }

    private void event(){
        //Listener cho click vào thẻ
        mCartModelAdapter.setOnOrderClickListener(new CartModelAdapter.OnCartModelListener() {
            @Override
            public void setOnCartModelClickListener(int position) {
                Toast.makeText(getContext(), "OrderItem Clicked", Toast.LENGTH_SHORT).show();
                OrderDetailDialog.createOrderDetailDialog(getActivity(),mCartModelList.get(position).items);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //Download về list các cartmodel trong OrderHistory trên Firestore
        TokenManager tokenManager = TokenManager.getInstance();
        String userId = tokenManager.getUserId();
        Query query= db.collection("OrderHistory")
                .whereEqualTo("userId",userId);
        listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("BBB", "OrderHistory Listen failed.", error);
                            return;
                        }
                        mCartModelList.clear();
                        int total = 0;
                        for (QueryDocumentSnapshot doc : value) {
                            //value là toàn bộ những document trong table OrderHistory
                            //Nếu muốn có query thì xem thêm doc của Firebase https://firebase.google.com/docs/firestore/query-data/listen#listen_to_multiple_documents_in_a_collection
                            CartModel cartModel = doc.toObject(CartModel.class);
                            mCartModelList.add(cartModel);
                            total += cartModel.total;

                        }
                        //Hiển thị mCartModelList ở Recyclerview
                        mCartModelAdapter.updateListCartModel(mCartModelList);
                        //Tổng tiền
                        String totalString = new DecimalFormat("#,###").format(total);
                        mBinding.textviewTotalAmount.setText("Total: "+ totalString + "VNĐ");

                    }
                });

    }

    @Override
    public void onPause() {
        super.onPause();
        if(listenerRegistration!=null){
            listenerRegistration.remove();
        }
    }
}