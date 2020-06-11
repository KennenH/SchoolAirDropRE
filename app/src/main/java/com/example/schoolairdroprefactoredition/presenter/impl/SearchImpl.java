package com.example.schoolairdroprefactoredition.presenter.impl;

import android.util.Log;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainSearchItems;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.presenter.ISearchPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ISearchCallback;
import com.example.schoolairdroprefactoredition.utils.RetrofitManager;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchImpl implements ISearchPresenter {

    private ISearchCallback mCallback;

    @Override
    public void getSearchResult(String key) {

        // todo 用key拼接字符串作为url的参数传入获取数据

        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainGoodsInfo> task = api.getGoodsInfo();
        task.enqueue(new Callback<DomainGoodsInfo>() {
            @Override
            public void onResponse(Call<DomainGoodsInfo> call, Response<DomainGoodsInfo> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainGoodsInfo info = response.body();

                    if (mCallback != null) {
                        mCallback.onSearchResultLoaded(info);
                    }

                    Log.d("SearchImpl", info.toString());
                } else {
                    Log.d("SearchImpl", "请求错误");
                }
            }

            @Override
            public void onFailure(Call<DomainGoodsInfo> call, Throwable t) {
                Log.e("SearchImpl", "请求失败 " + t);
            }
        });
    }

    @Override
    public void getSearchHistory() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainSearchItems> task = api.getSearchHistory();
        task.enqueue(new Callback<DomainSearchItems>() {
            @Override
            public void onResponse(Call<DomainSearchItems> call, Response<DomainSearchItems> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainSearchItems histories = response.body();
                    if (mCallback != null) {
                        mCallback.onSearchHistoryLoaded(histories);
                    }
                    Log.d("SearchImpl", histories.toString());
                } else {
                    Log.d("SearchImpl", "请求错误");
                }
            }

            @Override
            public void onFailure(Call<DomainSearchItems> call, Throwable t) {
                Log.e("SearchImpl", "请求失败 " + t);
            }
        });
    }

    @Override
    public void getSearchSuggestion(String key) {

        // todo 用key拼接字符串作为url的参数传入获取数据

        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainSearchItems> task = api.getSearchSuggestion();
        task.enqueue(new Callback<DomainSearchItems>() {
            @Override
            public void onResponse(Call<DomainSearchItems> call, Response<DomainSearchItems> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainSearchItems suggestions = response.body();
                    if (mCallback != null) {
                        mCallback.onSearchSuggestionLoaded(suggestions);
                    }
                    Log.d("SearchImpl", suggestions.toString());
                } else {
                    Log.d("SearchImpl", "请求错误");
                }
            }

            @Override
            public void onFailure(Call<DomainSearchItems> call, Throwable t) {
                Log.e("SearchImpl", "请求失败 " + t);
            }
        });
    }

    @Override
    public void registerCallback(ISearchCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(ISearchCallback callback) {
        mCallback = null;
    }
}
