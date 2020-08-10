package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.model.databean.SearchSuggestionBean;
import com.example.schoolairdroprefactoredition.model.databean.TestGoodsItemBean;
import com.example.schoolairdroprefactoredition.presenter.ISearchPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ISearchCallback;

import java.util.ArrayList;
import java.util.List;

public class SearchImpl implements ISearchPresenter {

    private ISearchCallback mCallback;

    @Override
    public void getSearchResult(String key) {

        // todo 用key拼接字符串作为url的参数传入获取数据

//        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
//        Api api = retrofit.create(Api.class);
//        Call<GoodsData> task = api.getGoodsInfo();
//        task.enqueue(new Callback<GoodsData>() {
//            @Override
//            public void onResponse(Call<GoodsData> call, Response<GoodsData> response) {
//                int code = response.code();
//                if (code == HttpURLConnection.HTTP_OK) {
//                    GoodsData info = response.body();
//
//                    if (mCallback != null) {
//                        mCallback.onSearchResultLoaded(info);
//                    }
//
//                    Log.d("SearchImpl", info.toString());
//                } else {
//                    Log.d("SearchImpl", "请求错误");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GoodsData> call, Throwable t) {
//                Log.e("SearchImpl", "请求失败 " + t);
//            }
//        });

        List<DomainGoodsInfo.GoodsInfoBean> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            DomainGoodsInfo.GoodsInfoBean item = new DomainGoodsInfo.GoodsInfoBean();
            item.setTital(key);
            item.setPrice("199.99");
            list.add(item);
        }
        mCallback.onSearchResultLoaded(list);

    }

    @Override
    public void getSearchHistory() {
        // todo 本地记录为主
    }

    @Override
    public void getSearchSuggestion(String key) {
        // todo 用key拼接字符串作为url的参数传入获取数据,分历史记录和

//        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
//        Api api = retrofit.create(Api.class);
//        Call<DomainSearchItems> task = api.getSearchSuggestion();
//        task.enqueue(new Callback<DomainSearchItems>() {
//            @Override
//            public void onResponse(Call<DomainSearchItems> call, Response<DomainSearchItems> response) {
//                int code = response.code();
//                if (code == HttpURLConnection.HTTP_OK) {
//                    DomainSearchItems suggestions = response.body();
//                    if (mCallback != null) {
//                        mCallback.onSearchSuggestionLoaded(suggestions);
//                    }
//                    Log.d("SearchImpl", suggestions.toString());
//                } else {
//                    Log.d("SearchImpl", "请求错误");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DomainSearchItems> call, Throwable t) {
//                Log.e("SearchImpl", "请求失败 " + t);
//            }
//        });

        SearchSuggestionBean items = new SearchSuggestionBean();
        items.setTitle(key);
        mCallback.onSearchSuggestionLoaded(items);
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
