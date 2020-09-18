package com.example.schoolairdroprefactoredition.scene.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.presenter.callback.IHomeNewsCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.HomeNewsImpl;
import com.example.schoolairdroprefactoredition.model.databean.TestNewsItemBean;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;

import java.util.List;

public class HomeNewsFragmentViewModel extends BaseStateViewModel implements IHomeNewsCallback {

    private HomeNewsImpl mHomeImpl;

    private MutableLiveData<List<TestNewsItemBean>> mHomeNews = new MutableLiveData<>();

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
    protected void onCleared() {
        mHomeImpl.unregisterCallback(this);
    }

    @Override
    public void onError() {
        mOnRequestListener.onError();
    }
}
