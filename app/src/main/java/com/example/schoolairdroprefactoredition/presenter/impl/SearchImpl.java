package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.cache.SearchHistories;
import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.model.api.Api;
import com.example.schoolairdroprefactoredition.model.CallBackWithRetry;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.ISearchPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ISearchCallback;
import com.example.schoolairdroprefactoredition.utils.ConstantUtil;
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.schoolairdroprefactoredition.cache.SearchHistories.SEARCH_HISTORY;

public class SearchImpl implements ISearchPresenter {

    private static SearchImpl mSearchImpl = null;

    public static SearchImpl getInstance() {
        if (mSearchImpl == null) {
            mSearchImpl = new SearchImpl();
        }
        return mSearchImpl;
    }

    private ISearchCallback mCallback;

    private final JsonCacheUtil mJsonCacheUtil = JsonCacheUtil.newInstance();

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
    public void getSearchResult(int page, double longitude, double latitude, String key, boolean isLoadMore) {
        if (!isLoadMore)
            saveHistory(key);

        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<DomainGoodsInfo> task = api.searchGoods(ConstantUtil.CLIENT_ID, ConstantUtil.CLIENT_SECRET, page, longitude, latitude, key);
        task.enqueue(new CallBackWithRetry<DomainGoodsInfo>(task) {
            @Override
            public void onResponse(Call<DomainGoodsInfo> call, Response<DomainGoodsInfo> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainGoodsInfo info = response.body();

//                    try {
//                        Log.d("SearchImpl", "请求成功 -- > " + response.body().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    if (mCallback != null) {
                        if (info != null) {
                            mCallback.onSearchResultLoaded(info.getData());
                        } else {
                            mCallback.onError();
                        }
                    }

                } else {
                    if (mCallback != null)
                        mCallback.onError();
                }
            }

            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onError();
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
