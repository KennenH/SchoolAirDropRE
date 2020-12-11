package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.presenter.IHomePostPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IHomeNewsCallback;
import com.example.schoolairdroprefactoredition.ui.adapter.HomePostsRecyclerAdapter;
import com.example.schoolairdroprefactoredition.ui.components.BaseHomeNewsEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class HomePostImpl implements IHomePostPresenter {

    private IHomeNewsCallback mCallback = null;

    /**
     * 请求最新消息的数据
     */
    @Override
    public void getNews(int page) {
        BaseHomeNewsEntity[] data = new BaseHomeNewsEntity[12];

        ArrayList<String> list = new ArrayList<>();
        list.add("https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/iphone-12-pro-max-gold-hero?wid=940&hei=1112&fmt=png-alpha&qlt=80&.v=1604021660000");
        list.add("https://dss0.bdstatic.com/-0U0bnSm1A5BphGlnYG/tam-ogel/920152b13571a9a38f7f3c98ec5a6b3f_122_122.jpg");
        list.add("https://dss0.bdstatic.com/-0U0bnSm1A5BphGlnYG/tam-ogel/6bfe4659c6f3715ad5fa3ebde90ac123_259_194.jpg");
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606042040652&di=38cf0771633e1235ad6ce15ababccf93&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201410%2F22%2F20141022073033_nBdWH.jpeg");
        list.add("http://106.54.110.46:8000/assets/goods/img/UmMHekJlINW5BYhL_1.jpg");
        list.add("http://106.54.110.46:8000/assets/goods/img/UmMHekJlINW5BYhL_2.jpg");
        list.add("http://106.54.110.46:8000/assets/goods/img/UmMHekJlINW5BYhL_3.jpg");

        for (int i = 0; i < 12; i++) {
            data[i] = new BaseHomeNewsEntity();
            Random random = new Random();
            if (i % 4 == 0) {
                data[i].setType(HomePostsRecyclerAdapter.TYPE_TWO);
                data[i].setTitle("#测试话题#");
            } else {
                data[i].setType(HomePostsRecyclerAdapter.TYPE_ONE);
                data[i].setTitle("校园空投开始校园内测啦,速戳宇宙最全攻略!");
            }
            data[i].setUrl(list.get(random.nextInt(list.size())));
        }

        if (mCallback != null) {
            mCallback.onNewsLoaded(Arrays.asList(data));
        }
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
