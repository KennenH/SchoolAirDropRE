package com.example.schoolairdroprefactoredition.presenter.impl;

import android.util.Log;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.SearchHistories;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.model.databean.SearchSuggestionBean;
import com.example.schoolairdroprefactoredition.presenter.ISearchPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ISearchCallback;
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchImpl implements ISearchPresenter {

    private ISearchCallback mCallback;

    private JsonCacheUtil mJsonCacheUtil = JsonCacheUtil.newInstance();

    public static final String SEARCH_HISTORY = "search_history";
    private int historyMaxStack = 10;

    /**
     * 保存搜索历史记录
     */
    private void saveHistory(String history) {
        SearchHistories histories = mJsonCacheUtil.getValue(SEARCH_HISTORY, SearchHistories.class);

        // 如果有重复的，去掉重复的搜索关键字
        List<String> list = null;
        if (histories != null && histories.getHistoryList() != null) {
            list = histories.getHistoryList();
            if (list.contains(history)) {
                list.remove(history);
            }
        }

        if (list == null) list = new ArrayList<>();
        if (histories == null) histories = new SearchHistories();
        histories.setHistoryList(list);

        if (list.size() > historyMaxStack) {
            list = list.subList(0, historyMaxStack);
        }
        list.add(0, history);
        mJsonCacheUtil.saveCache(SEARCH_HISTORY, histories);
    }

    @Override
    public void getSearchResult(String token, double longitude, double latitude, String key, boolean isLoadMore) {
        if (!isLoadMore)
            saveHistory(key);

        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainGoodsInfo> task = api.searchGoods(token, 120.31219, 30.124445, key);
        task.enqueue(new Callback<DomainGoodsInfo>() {
            @Override
            public void onResponse(Call<DomainGoodsInfo> call, Response<DomainGoodsInfo> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainGoodsInfo info = response.body();
//                    try {
//                        Log.d("SearchImpl", "请求成功 -- > " + info.string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    if (mCallback != null) {
                        mCallback.onSearchResultLoaded(info.getData());
                    }

                } else {
                    try {
                        Log.d("SearchImpl", "请求错误 -- > " + response.errorBody().string() + " token -- > " + token);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
        SearchHistories histories = mJsonCacheUtil.getValue(SEARCH_HISTORY, SearchHistories.class);
        if (mCallback != null
                && histories != null
                && histories.getHistoryList() != null
                && histories.getHistoryList().size() > 0) {
            mCallback.onSearchHistoryLoaded(histories);
        }
    }

    @Override
    public void getSearchSuggestion(String key) {
        List<String> histories = mJsonCacheUtil.getValue(SEARCH_HISTORY, SearchHistories.class).getHistoryList();
//        mCallback.onSearchSuggestionLoaded(items);
    }

    @Override
    public void deleteHistories() {
        mJsonCacheUtil.deleteCache(SEARCH_HISTORY);
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
