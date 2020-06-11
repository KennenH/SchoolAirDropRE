package com.example.schoolairdroprefactoredition.fragment.home;

import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.schoolairdroprefactoredition.domain.DomainGoodsInfo;
import com.example.schoolairdroprefactoredition.domain.DomainNews;
import com.example.schoolairdroprefactoredition.presenter.callback.IHomeCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.HomeImpl;
import com.example.schoolairdroprefactoredition.ui.components.RecyclerItemData;
import com.example.schoolairdroprefactoredition.ui.components.Tags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeViewModel extends ViewModel implements IHomeCallback {
    private HomeImpl mHomeImpl;
    private MutableLiveData<List<RecyclerItemData>> mRecyclerOnlineData;

    public LiveData<List<RecyclerItemData>> getRecyclerData() {
        getRecyclerOnlineData();
        mHomeImpl.getNearbyGoodsInfo();
        return mRecyclerOnlineData;
    }


    public HomeViewModel() {
        mHomeImpl = new HomeImpl();
        mHomeImpl.registerCallback(this);
        mHomeImpl.getNearbyGoodsInfo();
        getRecyclerOnlineData();
    }


    private void getRecyclerOnlineData() {
        mRecyclerOnlineData = new MutableLiveData<>();
        RecyclerItemData[] data = new RecyclerItemData[40];
        for (int i = 0; i < data.length; i++) {
            data[i] = new RecyclerItemData();
            data[i].setTitle("计算机网络原理 第五版 低价出");
            data[i].setPrice(20.99f);

            List<Integer> tags = new ArrayList<Integer>() {
                {
                    add(Tags.ADVERTISEMENT);
                    add(Tags.SECOND_HAND);
                    add(Tags.NEGOTIABLE);
                }
            };

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tags.sort((a, b) -> a - b);
            }

            data[i].setTags(tags);
        }

        data[0].setImageUrl("http://img.souutu.com/2020/0415/20200415123551376.jpg");
        data[1].setImageUrl("http://img.souutu.com/2020/0415/20200415123552406.jpg");
        data[2].setImageUrl("https://uploadfile.bizhizu.cn/2015/0411/20150411021434461.jpg");
        data[3].setImageUrl("http://img.souutu.com/2020/0415/20200415123553836.jpg");
        data[4].setImageUrl("http://img.souutu.com/2020/0415/20200415123554328.jpg");
        data[5].setImageUrl("http://img.souutu.com/2020/0415/20200415123555406.jpg");
        data[6].setImageUrl("http://img.souutu.com/2020/0415/20200415123555195.jpg");
        data[7].setImageUrl("https://uploadfile.bizhizu.cn/2015/0411/20150411021441243.jpg");
        data[8].setImageUrl("https://uploadfile.bizhizu.cn/2015/0411/20150411021423685.jpg");
        data[9].setImageUrl("http://image.hbwh.net/uploads/20190415/12/1555303458-qglokzLRTn.jpg");
        data[10].setImageUrl("http://image.hbwh.net/uploads/20190415/12/1555303458-bKhpqIMkzc.jpeg");
        data[11].setImageUrl("http://image.hbwh.net/uploads/20190415/12/1555303459-GarCYUIgDO.jpg");
        data[12].setImageUrl("http://image.hbwh.net/uploads/20190415/12/1555303460-uvFtcOoTLa.jpg");
        data[13].setImageUrl("http://image.hbwh.net/uploads/20190415/12/1555303460-xDmVrXGMhu.jpg");
        data[14].setImageUrl("http://image.hbwh.net/uploads/20190415/12/1555303462-JQVtaDECRq.jpg");
        data[15].setImageUrl("http://image.hbwh.net/uploads/20190415/12/1555303462-ugVibQFSwY.jpeg");
        data[16].setImageUrl("http://image.hbwh.net/uploads/20190415/12/1555303462-upWkTenNJP.jpeg");
        data[17].setImageUrl("http://bizhi.qqju.com/pic/bz/bz202_0.jpg");
        data[18].setImageUrl("http://bizhi.qqju.com/pic/bz/bz140_0.jpg");
        data[19].setImageUrl("http://img.mm4000.com/file/a/e9/0f86fd964c_1044.jpg");
        data[20].setImageUrl("http://img.mm4000.com/file/a/e9/23e8d37234_1044.jpg");
        data[21].setImageUrl("http://img.mm4000.com/file/a/e9/84006d1f96_1044.jpg");
        data[22].setImageUrl("http://img.mm4000.com/file/a/e9/9c70b5b881_1044.jpg");
        data[23].setImageUrl("http://img.mm4000.com/file/a/e9/2e07a8bc8f_1044.jpg");
        data[24].setImageUrl("http://img.mm4000.com/file/a/e9/2d67f2e226_1044.jpg");
        data[25].setImageUrl("http://img.mm4000.com/file/a/e9/759df42642_1044.jpg");
        data[26].setImageUrl("http://img.mm4000.com/file/a/e9/9d295bba84_1044.jpg");
        data[27].setImageUrl("http://img.mm4000.com/file/a/e9/cdc75ab825_1044.jpg");
        data[28].setImageUrl("http://img.souutu.com/2020/0415/20200415123748388.jpg");
        data[29].setImageUrl("http://img.souutu.com/2020/0415/20200415123748959.jpg");
        data[30].setImageUrl("http://img.souutu.com/2020/0415/20200415123749899.jpg");
        data[31].setImageUrl("http://img.souutu.com/2020/0415/20200415123744165.jpg");
        data[32].setImageUrl("http://img.souutu.com/2020/0415/20200415123745630.jpg");
        data[33].setImageUrl("http://img.souutu.com/2020/0415/20200415123746935.jpg");
        data[34].setImageUrl("http://img.souutu.com/2020/0415/20200415123746257.jpg");
        data[35].setImageUrl("http://img.souutu.com/2020/0415/20200415123747765.jpg");
        data[36].setImageUrl("http://img.souutu.com/2020/0415/20200415123747357.jpg");
        data[37].setImageUrl("http://img.souutu.com/2020/0415/20200415123550885.jpg");
        data[38].setImageUrl("http://img.souutu.com/2020/0415/20200415123550781.jpg");
        data[39].setImageUrl("http://img.souutu.com/2020/0415/20200415123551874.jpg");

        mRecyclerOnlineData.setValue(Arrays.asList(data));
    }

    /**
     * 附近在售数据从这里出来
     *
     * @param domainGoodsInfo 附近商品信息
     */
    @Override
    public void onGoodsInfoLoaded(DomainGoodsInfo domainGoodsInfo) {
        //todo setValue 或 postValue来更新ui
    }

    /**
     * 新闻数据从这里出来
     *
     * @param domainNews 新闻数据
     */
    @Override
    public void onNewsLoaded(DomainNews domainNews) {
        //todo setValue 或 postValue来更新ui
    }

    @Override
    protected void onCleared() {
        mHomeImpl.unregisterCallback(this);
    }
}