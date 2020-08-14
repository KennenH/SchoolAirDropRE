package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.model.databean.SearchSuggestionBean;
import com.example.schoolairdroprefactoredition.model.databean.TestGoodsItemBean;
import com.example.schoolairdroprefactoredition.presenter.ISearchPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ISearchCallback;

import java.util.ArrayList;
import java.util.Arrays;
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

        DomainGoodsInfo.GoodsInfoBean[] data = new DomainGoodsInfo.GoodsInfoBean[5];
        for (int i = 0; i < 5; i++) data[i] = new DomainGoodsInfo.GoodsInfoBean();
        data[0].setIstender("1");
        data[0].setIsPrice("1");
        data[0].setTitle("红红火火恍恍惚惚很好");
        data[0].setPrice("234.12");
        data[0].setCredit_num("4");
        data[0].setUname("hhkkkknnnnn");
        data[0].setDescription("还好是大家快来看监控网络日哦克拉斯电脑阿斯顿自行车");

        data[1].setIstender("0");
        data[1].setIsPrice("1");
        data[1].setTitle("哇塞什么东西会变质的吗");
        data[1].setPrice("5123.12");
        data[1].setCredit_num("3");
        data[1].setUname("ssskskskslslsl");
        data[1].setDescription("好吧如果真的是这样的话那么我们昨天就巴哈韩国了去");

        data[2].setIstender("1");
        data[2].setIsPrice("0");
        data[2].setTitle("牛排有牛油腻歪脖子病根是什么");
        data[2].setPrice("23.52");
        data[2].setCredit_num("5");
        data[2].setUname("oooooooeoooeoeoee");
        data[2].setDescription("我们要是有什么这样的浪迹卡不住我们的脚步的");

        data[3].setIstender("1");
        data[3].setIsPrice("0");
        data[3].setTitle("阿斯顿好地方但是我们没有去过那个地方在哪里");
        data[3].setPrice("12302.1");
        data[3].setCredit_num("5");
        data[3].setUname("ashqwhehkkcmmc");
        data[3].setDescription("阿斯顿我去恶趣味如图一投入");

        data[4].setIstender("0");
        data[4].setIsPrice("0");
        data[4].setTitle("就好像呼吁好像的时样子的瓦林卡看来撒旦");
        data[4].setPrice("213.22");
        data[4].setCredit_num("1");
        data[4].setUname("hhhsgsssppps");
        data[4].setDescription("接口真的好大的啊怎么回事啊咋回事啊我和你们一起去把");

        mCallback.onSearchResultLoaded(Arrays.asList(data));

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
