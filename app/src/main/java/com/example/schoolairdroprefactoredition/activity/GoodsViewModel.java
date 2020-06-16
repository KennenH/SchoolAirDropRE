package com.example.schoolairdroprefactoredition.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.model.databean.TestGoodsDetailBean;

import java.util.ArrayList;
import java.util.List;

public class GoodsViewModel extends ViewModel {

    private MutableLiveData<TestGoodsDetailBean> mGoodsData;

    public GoodsViewModel() {
        getOnlineGoodsData();
    }

    public LiveData<TestGoodsDetailBean> getGoodsData() {
        return mGoodsData;
    }

    private void getOnlineGoodsData() {
        mGoodsData = new MutableLiveData<>();

        //////////////////////test/////////////////////////
        TestGoodsDetailBean data = new TestGoodsDetailBean();
        List<String> list = new ArrayList<>();
        list.add("http://image.hbwh.net/uploads/20190415/12/1555303460-uvFtcOoTLa.jpg");
        list.add("http://image.hbwh.net/uploads/20190415/12/1555303460-xDmVrXGMhu.jpg");
        list.add("http://image.hbwh.net/uploads/20190415/12/1555303462-JQVtaDECRq.jpg");
        list.add("http://image.hbwh.net/uploads/20190415/12/1555303462-ugVibQFSwY.jpeg");
        data.setImageData(list);
        data.setGoodsName("杂七杂八");
        data.setTags(new ArrayList<Integer>() {
            {
                add(0);
                add(1);
                add(2);
            }
        });
        data.setAvatar("http://image.hbwh.net/uploads/20190415/12/1555303462-ugVibQFSwY.jpeg");
        data.setComments(12332);
        data.setLikes(5463);
        data.setWatches(345889);
        data.setPrice(15923.5f);
        data.setUserName("校园空投官方");
        data.setDescription("哈哈哈哈哈哈哈哈哈哈哈哈奥奥奥哈哈哈哈哈哈或红红火火恍恍惚惚");
        //////////////////////test/////////////////////////
        mGoodsData.setValue(data);
    }

}
