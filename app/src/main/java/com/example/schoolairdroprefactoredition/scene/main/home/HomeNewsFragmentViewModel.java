package com.example.schoolairdroprefactoredition.scene.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.presenter.callback.IHomeNewsCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.HomeNewsImpl;
import com.example.schoolairdroprefactoredition.model.databean.TestNewsItemBean;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

import java.util.List;

public class HomeNewsFragmentViewModel extends BaseStateViewModel implements IHomeNewsCallback {
    private int page;
    private double longitude;
    private double latitude;
    private HomeNewsImpl mHomeImpl;

    private MutableLiveData<List<TestNewsItemBean>> mHomeNews = new MutableLiveData<>();

    public HomeNewsFragmentViewModel() {
        mHomeImpl = new HomeNewsImpl();
        mHomeImpl.registerCallback(this);
    }

    public LiveData<List<TestNewsItemBean>> getHomeNews(double longitude, double latitude) {
        page = 0;
        this.latitude = latitude;
        this.longitude = longitude;

        mHomeImpl.getNews(page++);
        return mHomeNews;
    }

    public LiveData<List<TestNewsItemBean>> getHomeNews() {
        mHomeImpl.getNews(page++);
        return mHomeNews;
    }

//    public LiveData<List<TestNewsItemBean>> getHomeNews() {
//
//    }

    @Override
    public void onNewsLoaded(List<TestNewsItemBean> data) {
        mHomeNews.postValue(data);
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
