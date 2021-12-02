package com.example.foodsales20072021.viewmodel;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodsales20072021.base.BaseViewModel;
import com.example.foodsales20072021.model.ApiResponse;
import com.example.foodsales20072021.model.CartModel;
import com.example.foodsales20072021.model.ConfirmModel;
import com.example.foodsales20072021.model.FoodModel;
import com.example.foodsales20072021.model.OrderModel;
import com.example.foodsales20072021.model.OrderedItemModel;
import com.example.foodsales20072021.repository.FoodRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodViewModel extends BaseViewModel {
    private FoodRepository foodRepository;
    private MutableLiveData<List<FoodModel>> foodsModel = new MutableLiveData<>();
    private MutableLiveData<FoodModel> foodModelLiveData = new MutableLiveData<>();
    private MutableLiveData<OrderModel> orderModelLiveData = new MutableLiveData<>();
    private MutableLiveData<OrderModel> totalCountLiveData = new MutableLiveData<>();
    private MutableLiveData<CartModel> cartModelLiveData = new MutableLiveData<>();
    private MutableLiveData<String> updateResultLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> responseCodeLiveData = new MutableLiveData<>();
    private MutableLiveData<String> confirmLiveData = new MutableLiveData<>();

//    //Context cho SessionManager
//    private Context context;

//    private SessionManager sessionManager;
//
//    public void setContextAndSession(Context context) {
//        this.context = context;
//        sessionManager = new SessionManager(context);
//    }

    public void updateFoodRepository(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public LiveData<List<FoodModel>> getFoodsModel() {
        return foodsModel;
    }

    public LiveData<FoodModel> getFoodLiveDataModel() {return foodModelLiveData;}

    public LiveData<OrderModel> getTotalCount(){
        return totalCountLiveData;
    }

    public LiveData<OrderModel> getOrderModelLiveData() {
        return orderModelLiveData;
    }

    public LiveData<CartModel> getCartModelLiveData(){
        return cartModelLiveData;
    }

    public LiveData<String> getUpdateResultLiveData(){
        return updateResultLiveData;
    }

    public LiveData<Integer> getResponseCodeLiveData(){
        return responseCodeLiveData;
    }

    public LiveData<String> getConfirmLiveData(){return  confirmLiveData;}

    public void fetchFoodsModel() {
        setLoading(true);
        foodRepository.getFoodsModel()
                .enqueue(new Callback<ApiResponse<List<FoodModel>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<FoodModel>>> call, Response<ApiResponse<List<FoodModel>>> response) {
                        if (response.body() != null) {
                            ApiResponse<List<FoodModel>> data = response.body();
                            foodsModel.setValue(data.getData());
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                String message = jsonObject.getString("message");
                                setError(new Throwable(message));
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                setError(new Throwable(e.getMessage()));
                            }
                        }
                        setLoading(false);
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<FoodModel>>> call, Throwable t) {
                        setError(t);
                        setLoading(false);
                    }
                });
    }

    public void fetchFoodModel(String foodId) {
        setLoading(true);
        foodRepository.getFoodModel(foodId)
                .enqueue(new Callback<ApiResponse<FoodModel>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<FoodModel>> call, Response<ApiResponse<FoodModel>> response) {
                        if (response.body() != null) {
                            ApiResponse<FoodModel> data = response.body();
                            foodModelLiveData.setValue(data.getData());
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                String message = jsonObject.getString("message");
                                setError(new Throwable(message));
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                setError(new Throwable(e.getMessage()));
                            }
                        }
                        setLoading(false);
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<FoodModel>> call, Throwable t) {
                        setError(t);
                        setLoading(false);
                    }
                });
    }

    public void fetchTotalCount() {
        if (foodRepository != null) {
            setLoading(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    foodRepository.getTotalCount()
                            .enqueue(new Callback<ApiResponse<OrderModel>>() {
                                @Override
                                public void onResponse(Call<ApiResponse<OrderModel>> call, Response<ApiResponse<OrderModel>> response) {
                                    if (response.body() != null) {
                                        //Có kết quả trả về thành công (code 200-300), không bao gồm trường hợp trả về lỗi
                                        ApiResponse<OrderModel> data = response.body();
                                        totalCountLiveData.setValue(data.getData());
                                        responseCodeLiveData.setValue(data.getCode());
                                    } else {
                                        //Có kết quả trả về nhưng có lỗi như 401, 500...
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                            //Phải để trước message vì có thể sẽ không gửi message
                                            responseCodeLiveData.setValue(jsonObject.getInt("code"));
                                            String message = jsonObject.getString("message");

                                            setError(new Throwable(message));
                                        } catch (JSONException | IOException e) {
                                            e.printStackTrace();
                                            setError(new Throwable(e.getMessage()));
                                        }
                                    }
                                    setLoading(false);
                                }

                                @Override
                                public void onFailure(Call<ApiResponse<OrderModel>> call, Throwable t) {
                                    Log.d("BBB", t.getMessage());
                                    setError(t);
                                    setLoading(false);
                                }
                            });

                }
            }, 500);

        }

    }

    public void fetchOrderModel(FoodModel foodModel) {
        if (foodRepository != null) {
            setLoading(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    foodRepository.getOrderModel( new FoodModel(foodModel.foodId))
                            .enqueue(new Callback<ApiResponse<OrderModel>>() {
                                @Override
                                public void onResponse(Call<ApiResponse<OrderModel>> call, Response<ApiResponse<OrderModel>> response) {
                                    if (response.body() != null) {
                                        ApiResponse<OrderModel> data = response.body();
                                        orderModelLiveData.setValue(data.getData());

                                    } else {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                            String message = jsonObject.getString("message");
                                            setError(new Throwable(message));
                                        } catch (JSONException | IOException e) {
                                            e.printStackTrace();
                                            setError(new Throwable(e.getMessage()));
                                        }
                                    }
                                    setLoading(false);
                                }

                                @Override
                                public void onFailure(Call<ApiResponse<OrderModel>> call, Throwable t) {
                                    Log.d("BBB", t.getMessage());
                                    setError(t);
                                    setLoading(false);
                                }
                            });

                }
            }, 500);

        }

    }

    public void fetchCartModel(){
        if (foodRepository != null) {
            setLoading(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    foodRepository.getCartModel()
                            .enqueue(new Callback<ApiResponse<CartModel>>() {
                                @Override
                                public void onResponse(Call<ApiResponse<CartModel>> call, Response<ApiResponse<CartModel>> response) {
                                    if (response.body() != null) {
                                        ApiResponse<CartModel> data = response.body();
                                        cartModelLiveData.setValue(data.getData());
                                    } else {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                            String message = jsonObject.getString("message");
                                            setError(new Throwable(message));
                                        } catch (JSONException | IOException e) {
                                            e.printStackTrace();
                                            setError(new Throwable(e.getMessage()));
                                        }
                                    }
                                    setLoading(false);
                                }

                                @Override
                                public void onFailure(Call<ApiResponse<CartModel>> call, Throwable t) {
                                    Log.d("BBB", t.getMessage());
                                    setError(t);
                                    setLoading(false);
                                }
                            });

                }
            }, 500);

        }
    }

    public void fetchUpdateResult( OrderedItemModel orderedItemModel){
        if (foodRepository != null) {
            setLoading(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    foodRepository.updateCart(orderedItemModel)
                            .enqueue(new Callback<ApiResponse<String>>() {
                                @Override
                                public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                                    if (response.body() != null) {
                                        ApiResponse<String> data = response.body();
                                        updateResultLiveData.setValue(data.getData());
                                    } else {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                            String message = jsonObject.getString("message");
                                            setError(new Throwable(message));
                                        } catch (JSONException | IOException e) {
                                            e.printStackTrace();
                                            setError(new Throwable(e.getMessage()));
                                        }
                                    }
                                    setLoading(false);
                                }

                                @Override
                                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                                    Log.d("BBB", t.getMessage());
                                    setError(t);
                                    setLoading(false);
                                }
                            });

                }
            }, 500);

        }
    }

    public void fetchConfirm(ConfirmModel confirmModel){
        if (foodRepository != null) {
            setLoading(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    foodRepository.confirm(confirmModel)
                            .enqueue(new Callback<ApiResponse<String>>() {
                                @Override
                                public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                                    if (response.body() != null) {
                                        ApiResponse<String> data = response.body();
                                        confirmLiveData.setValue(data.getData());
                                    } else {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                            String message = jsonObject.getString("message");
                                            setError(new Throwable(message));
                                        } catch (JSONException | IOException e) {
                                            e.printStackTrace();
                                            setError(new Throwable(e.getMessage()));
                                        }
                                    }
                                    setLoading(false);
                                }

                                @Override
                                public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                                    Log.d("BBB", t.getMessage());
                                    setError(t);
                                    setLoading(false);
                                }
                            });

                }
            }, 500);

        }
    }
}