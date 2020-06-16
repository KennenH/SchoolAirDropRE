package com.example.schoolairdroprefactoredition.presenter.impl;

import com.example.schoolairdroprefactoredition.presenter.IHomeGoodsInfoPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.IHomeGoodsInfoCallback;
import com.example.schoolairdroprefactoredition.model.databean.TestGoodsItemBean;

import java.util.Arrays;

public class HomeGoodsInfoImpl implements IHomeGoodsInfoPresenter {

    private IHomeGoodsInfoCallback mCallback = null;

    /**
     * 请求附近在售的数据
     *
     * @param size 请求的item条数
     */
    @Override
    public void getNearbyGoodsInfo(int size) {
//        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
//        Api api = retrofit.create(Api.class);
//        Call<DomainGoodsInfo> task = api.getGoodsInfo();
//        task.enqueue(new Callback<DomainGoodsInfo>() {
//            @Override
//            public void onResponse(Call<DomainGoodsInfo> call, Response<DomainGoodsInfo> response) {
//                int code = response.code();
//                if (code == HttpURLConnection.HTTP_OK) {
//                    DomainGoodsInfo info = response.body();
//                    Log.d("HomeImpl", info.toString());
//                    if (mCallback != null) {
//                        // if (data.size() != 0)
//                        mCallback.onGoodsInfoLoaded(info);
//                        //else
//                        // mCallback.onGoodsInfoEmpty();
//                    }
//                } else {
//                    Log.d("HomeImpl", "请求错误 " + code);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DomainGoodsInfo> call, Throwable t) {
//                Log.e("HomeImpl", "请求失败 -- > " + t);
//            }
//        });

        // test
        TestGoodsItemBean[] data = new TestGoodsItemBean[size];
        for (int i = 0; i < data.length; i++) {
            data[i] = new TestGoodsItemBean();
            data[i].setPrice(20000.99f);
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

        mCallback.onGoodsInfoLoaded(Arrays.asList(data));
    }

    @Override
    public void registerCallback(IHomeGoodsInfoCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterCallback(IHomeGoodsInfoCallback callback) {
        mCallback = null;
    }
}
