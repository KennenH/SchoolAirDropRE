package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.presenter.IHomeNewsPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IHomeNewsCallback;
import com.example.schoolairdroprefactoredition.model.databean.TestNewsItemBean;

import java.util.Arrays;

public class HomeNewsImpl implements IHomeNewsPresenter {

    private IHomeNewsCallback mCallback = null;

    /**
     * 请求最新消息的数据
     */
    @Override
    public void getNews() {
//        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
//        Api api = retrofit.create(Api.class);
//        Call<DomainNews> task = api.getNews();
//        task.enqueue(new Callback<DomainNews>() {
//            @Override
//            public void onResponse(Call<DomainNews> call, Response<DomainNews> response) {
//                int code = response.code();
//                if (code == HttpURLConnection.HTTP_OK) {
//                    DomainNews info = response.body();
//                    Log.d("HomeImpl", info.toString());
//                    if (mCallback != null) {
//                        // if (data.size() != 0)
//                        mCallback.onNewsLoaded(info);
//                        //else
//                        // mCallback.onNewsEmpty();
//                    }
//                } else {
//                    Log.d("HomeImpl", "请求错误 " + code);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DomainNews> call, Throwable t) {
//                Log.e("HomeImpl", "请求失败 -- > " + t);
//            }
//        });

        // test
        TestNewsItemBean[] data = new TestNewsItemBean[12];
        for (int i = 0; i < 12; i++) {
            data[i] = new TestNewsItemBean();
            data[i].setTitle("民生大项目开盘啦！欢迎来帮我恰烂钱");
            data[i].setDay("32");
            data[i].setMonth((i + 13) + "月");
            data[i].setSender("李伟的脑子");
            data[i].setUrl("http://img.souutu.com/2020/0415/20200415123551376.jpg");
        }
        mCallback.onNewsLoaded(Arrays.asList(data));
    }

    @Override
    public void registerCallback(IHomeNewsCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterCallback(IHomeNewsCallback callback) {
        mCallback = null;
    }
}
