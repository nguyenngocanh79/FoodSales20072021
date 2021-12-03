package com.example.foodsales20072021.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodsales20072021.MyApplication;
import com.example.foodsales20072021.databinding.FragmentCartBinding;
import com.example.foodsales20072021.model.CartModel;
import com.example.foodsales20072021.model.ConfirmModel;
import com.example.foodsales20072021.model.Data;
import com.example.foodsales20072021.model.FoodModel;
import com.example.foodsales20072021.model.Notification;
import com.example.foodsales20072021.model.OrderedItemModel;
import com.example.foodsales20072021.model.Sender;
import com.example.foodsales20072021.model.Token;
import com.example.foodsales20072021.utils.NavBadge;
import com.example.foodsales20072021.utils.TokenManager;
import com.example.foodsales20072021.view.adapter.CartAdapter;
import com.example.foodsales20072021.view.dialog.DetailDialog;
import com.example.foodsales20072021.viewmodel.FoodViewModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import com.example.foodsales20072021.R;
import com.example.foodsales20072021.viewmodel.NotificationViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

public class CartFragment extends Fragment {
    //Binding
    FragmentCartBinding mBinding;
    //Model
    FoodViewModel mFoodViewModel;
    NotificationViewModel mNotificationViewModel;
    //Recyclerview
    RecyclerView mRcvCart;
    List<OrderedItemModel> mListOrderItemModel;
    CartAdapter mCartAdapter;

    //Quantity
    int orderedItemQuantity;
    OrderedItemModel orderedItemModel;


    //Firestore
    FirebaseFirestore db;
    CartModel mCartModel;
    List<CartModel> mCartModelList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentCartBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        //Firestore
        db = FirebaseFirestore.getInstance();
        mCartModelList = new ArrayList<>();

        //ViewModel
        mFoodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        mFoodViewModel.updateFoodRepository(((MyApplication) getActivity().getApplication()).foodRepository);
        mNotificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        mNotificationViewModel.setNotificationRepository(((MyApplication) getActivity().getApplication()).notificationRepository);


        //Recyclerview
        mRcvCart = mBinding.recyclerView;
        mListOrderItemModel = new ArrayList<>();
        mCartAdapter = new CartAdapter();
        mCartAdapter.updateListOrderedItemModels(mListOrderItemModel);
        mRcvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvCart.setHasFixedSize(true);
        mRcvCart.setAdapter(mCartAdapter);

        getAndSaveToken();
        observerData();
        event();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Load Cart
        mFoodViewModel.fetchCartModel();
    }

    private void getAndSaveToken() {
        //Lấy và lưu Token cho Firebase Message
        //Khi User vào màn hình Cart, Lấy Token và lưu trên server
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show();
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        updateToken(token);
                    }
                });


    }

    private void updateToken(String token) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Lấy UserId từ bộ nhớ
        TokenManager tokenManager = TokenManager.getInstance();
        String userId = tokenManager.getUserId();
        Token token1 = new Token(token);
        db.collection("Tokens")
                .document(userId)
                .set(token1);
    }

    private void event() {
        //Test các loại notification (notification, data, both)
        mBinding.textviewTotalAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                testNotification();
            }
        });
        //Nút confirm
        mBinding.buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCartModel != null) {
                    //Gọi hàm fetchConfirm, xem kết quả ở getConfirmLiveData
                    ConfirmModel confirmModel = new ConfirmModel(mCartModel.items.get(0).orderId, "CONFIRM");
                    mFoodViewModel.fetchConfirm(confirmModel);
                }
            }
        });
        //Event khi click Recyclerview
        mCartAdapter.setOnCartListener(new CartAdapter.OnCartListener() {
            @Override
            public void setOnOrderedItemClickListener(int position, int type) {
                switch (type) {
                    case 1: //Click vào nút "-"
                        //Còn thiếu trường hợp số sp ==0, hình như server không trả về

                        if (mListOrderItemModel.get(position).quantity > 1) {
                            orderedItemModel = mListOrderItemModel.get(position);
                            orderedItemModel.quantity = mListOrderItemModel.get(position).quantity - 1;
                            orderedItemModel.orderId = mListOrderItemModel.get(position).orderId;
                            orderedItemModel.foodId = mListOrderItemModel.get(position).foodId;
                            mFoodViewModel.fetchUpdateResult(orderedItemModel);
                        }
                        break;
                    case 2: //Click vào nút "+"
                        orderedItemModel = mListOrderItemModel.get(position);
                        orderedItemModel.quantity = mListOrderItemModel.get(position).quantity + 1;
                        orderedItemModel.orderId = mListOrderItemModel.get(position).orderId;
                        orderedItemModel.foodId = mListOrderItemModel.get(position).foodId;
                        mFoodViewModel.fetchUpdateResult(orderedItemModel);
                        break;
                    case 3: //Click vào thẻ
                        //Tải sản phẩm có id của thẻ
                        orderedItemModel = mListOrderItemModel.get(position);
                        mFoodViewModel.fetchFoodModel(orderedItemModel.foodId);
                        break;
                }
            }
        });
    }

    private void observerData() {
        //Observe list các ordered items
        mFoodViewModel.getCartModelLiveData().observe(getViewLifecycleOwner(), new Observer<CartModel>() {
            @Override
            public void onChanged(CartModel cartModel) {
                int total = 0;
                if (cartModel != null) {
                    mCartModel = cartModel;
                    mListOrderItemModel = cartModel.items;
                    mCartAdapter.updateListOrderedItemModels(cartModel.items);
                    //Label tổng tiền
                    NumberFormat formatter = new DecimalFormat("#,###");
                    mBinding.textviewTotalAmount.setText("Tổng tiền: " + formatter.format(cartModel.total) + "đ");

                    for (OrderedItemModel orderedItem : cartModel.items) {
                        total = total + orderedItem.quantity;
                    }
                } else {
                    mBinding.textviewTotalAmount.setText("Tổng tiền: 0đ");
                    mCartAdapter.updateListOrderedItemModels(new ArrayList<>());
                }
                //Cập nhật cart
                updateCartIcon(total);

            }
        });
        //Observe kết quả update
        mFoodViewModel.getUpdateResultLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    if (s.equals("OK")) {
                        mFoodViewModel.fetchCartModel();
                    }
                }
            }
        });
        //observe sản phẩm có id của thẻ
        mFoodViewModel.getFoodLiveDataModel().observe(getViewLifecycleOwner(), new Observer<FoodModel>() {
            @Override
            public void onChanged(FoodModel foodModel) {
                //Hiển thị dialog sản phẩm
                DetailDialog.createDetailDialog(getActivity(), foodModel);
            }
        });

        //Observe kết quả confirm
        mFoodViewModel.getConfirmLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {//Nếu có kết quả trả về
                    if (s.equals("OK")) {
                        //Uploade đơn hàng thành công (cartmodel) lên Firestore
                        String userid = "";
                        userid = TokenManager.getInstance().getUserId();
                        if (!userid.equals("")) { //Nếu có Userid
                            //--Thêm field userId để lọc user khi hiển thị trên từng account
                            mCartModel.userId = userid;
                            //--Thêm field confirmedAt để hiển thị
                            mCartModel.confirmedAt = new Date();
                            String confirmedDate = new SimpleDateFormat("yyyy, MMM dd - HH:mm:ss")
                                    .format(mCartModel.confirmedAt);

                            db.collection("OrderHistory")
                                    .document(userid + "-" + confirmedDate)
                                    .set(mCartModel)
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getContext(), "Add Firestore thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            //Cập nhật lại cart và trang cart
                            mFoodViewModel.fetchCartModel();

                            //Gửi notification lên Firebase Messaging để gửi Message về chính máy này
                            Data data = new Data(userid, R.drawable.ic_email,
                                    "Đơn hàng " + confirmedDate + " thanh toán thành công", "New Message", userid);
                            //Lấy token của người nhận (chính máy này) trên Firestore
                            db.collection("Tokens").document(userid)
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Token token = documentSnapshot.toObject(Token.class);
                                    Sender sender = new Sender(data, token.getToken());
                                    mNotificationViewModel.sendNotification(sender);
                                }
                            });

                        } else {//Nếu không có Userid, khả năng là đã bị xóa cùng token
                            Toast.makeText(getContext(), "UserId có thể đã bị xóa cùng token", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //Nếu message != null, nhưng khác "OK"
                        Toast.makeText(getContext(), "Kết quả: " + s + ". Confirm không thành công", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //Nếu không có message trả về
                    Toast.makeText(getContext(), "Confirm không thành công", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void updateCartIcon(int product) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomHome);
        NavBadge.updateBadge(bottomNavigationView, R.id.menu_item_cart, product);
    }

    private void testNotification() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Uploade đơn hàng thành công (cartmodel) lên Firestore
                Toast.makeText(getContext(), "bắt đầu gửi Notification", Toast.LENGTH_SHORT).show();
                String userid = "";
                userid = TokenManager.getInstance().getUserId();
                if (!userid.equals("")) { //Nếu có Userid
                    //Gửi notification lên Firebase Messaging để gửi Message về chính máy này
                    Data data = new Data(userid, R.drawable.ic_email,
                            "Đơn hàng thanh toán thành công", "New Message", userid);
                    Notification notification = new Notification("New Fire Message",
                            "Firebase has new message for you");

                    //Lấy token của người nhận (chính máy này) trên Firestore
                    db.collection("Tokens").document(userid)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Token token = documentSnapshot.toObject(Token.class);
                            Sender sender = new Sender(data, token.getToken());
                            Sender sender1 = new Sender(notification, token.getToken());
                            Sender sender2 = new Sender(notification, data, token.getToken());
                            mNotificationViewModel.sendNotification(sender2);
                        }
                    });

                } else {//Nếu không có Userid, khả năng là đã bị xóa cùng token
                    Toast.makeText(getContext(), "UserId có thể đã bị xóa cùng token", Toast.LENGTH_SHORT).show();
                }
            }
        }, 3000);

    }

}