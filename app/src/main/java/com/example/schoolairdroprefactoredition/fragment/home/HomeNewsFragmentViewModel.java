package com.example.schoolairdroprefactoredition.fragment.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.presenter.callback.IHomeNewsCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.HomeNewsImpl;
import com.example.schoolairdroprefactoredition.model.databean.TestNewsItemBean;

import java.util.ArrayList;
import java.util.List;

public class HomeNewsFragmentViewModel extends ViewModel implements IHomeNewsCallback {

    private HomeNewsImpl mHomeImpl;

    private MutableLiveData<List<TestNewsItemBean>> mHomeNews;

    public HomeNewsFragmentViewModel() {
        mHomeImpl = new HomeNewsImpl();
        mHomeImpl.registerCallback(this);
    }

    public LiveData<List<TestNewsItemBean>> getHomeNews() {
        mHomeImpl.getNews();
        return mHomeNews;
    }

    @Override
    public void onNewsLoaded(List<TestNewsItemBean> data) {
        if (mHomeNews == null)
            mHomeNews = new MutableLiveData<>();

        mHomeNews.setValue(data);
    }

    @Override
    public void onNewsLoading() {

    }

    @Override
    public void onNewsEmpty() {
        if (mHomeNews == null)
            mHomeNews = new MutableLiveData<>();

        mHomeNews.setValue(new ArrayList<>());
    }

    @Override
    protected void onCleared() {
        mHomeImpl.unregisterCallback(this);
    }
}
