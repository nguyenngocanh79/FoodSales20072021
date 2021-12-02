package com.example.foodsales20072021.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public abstract class BaseViewModel extends ViewModel {

    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private MutableLiveData<Throwable> errorLiveData = new MutableLiveData<>();

    public void setLoading(Boolean isLoading) {
        if (loadingLiveData != null){
            loadingLiveData.setValue(isLoading);
        }
    }

    public void setError(Throwable t) {
        if (errorLiveData != null){
            errorLiveData.setValue(t);
        }
    }

    public LiveData<Boolean> getLoading() {
        return loadingLiveData;
    }

    public LiveData<Throwable> getError() {
        return errorLiveData;
    }
}
