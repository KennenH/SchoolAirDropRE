package com.example.schoolairdroprefactoredition.scene.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.presenter.callback.IHomeNewsCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.HomeNewsImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.example.schoolairdroprefactoredition.ui.components.BaseHomeNewsEntity;

import java.util.List;

public class HomePlaygroundFragmentViewModel extends BaseStateViewModel implements IHomeNewsCallback {
    private int page;
    private double longitude;
    private double latitude;
    private final HomeNewsImpl mHomeImpl;

    private final MutableLiveData<List<BaseHomeNewsEntity>> mHomeNews = new MutableLiveData<>();

    public HomePlaygroundFragmentViewModel() {
        mHomeImpl = new HomeNewsImpl();
        mHomeImpl.registerCallback(this);
    }

    public LiveData<List<BaseHomeNewsEntity>> getHomeNews(double longitude, double latitude) {
        page = 0;
        this.latitude = latitude;
        this.longitude = longitude;

        mHomeImpl.getNews(page++);
        return mHomeNews;
    }

    public LiveData<List<BaseHomeNewsEntity>> getHomeNews() {
        mHomeImpl.getNews(page++);
        return mHomeNews;
    }

//    public LiveData<List<TestNewsItemBean>> getHomeNews() {
//
//    }

    @Override
    public void onNewsLoaded(List<BaseHomeNewsEntity> data) {
        mHomeNews.setValue(data);
    }

    @Override
    protected void onCleared() {
        mHomeImpl.unregisterCallback(this);
    }

    @Override
    public void onError() {
        mOnRequestListener.onError();
    }
}
