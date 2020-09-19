package com.example.schoolairdroprefactoredition.presenter.impl;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.domain.DomainResult;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.ISellingAddNewPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ISellingAddNewCallback;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SellingAddNewImpl implements ISellingAddNewPresenter {

    private ISellingAddNewCallback mCallback;

    @Override
    public void submit(String token, String cover, List<String> picSet,
                       String name, String description,
                       double longitude, double latitude,
                       boolean isBrandNew, boolean isQuotable,
                       float price) {

        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);

        List<MultipartBody.Part> goodsSet = new ArrayList<>();
        int i = 0;
        for (String pic : picSet) goodsSet.add(createFileWithPath("goods_img_set" + i++, pic));

//        MultipartBody.Builder body = new MultipartBody.Builder();
//        int i = 0;
//        for (String pic : picSet) {
//            File file = new File(pic);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
//            body.addFormDataPart("goods_img_set" + i++, file.getName(), requestBody);
//        }
//        MultipartBody.Part goodsSet = MultipartBody.Part.create(body.build());

        MultipartBody.Part goodsCover = createFileWithPath("goods_img_cover", cover);
        MultipartBody.Part goodsName = MultipartBody.Part.createFormData("goods_name", name);
        MultipartBody.Part goodsDes = MultipartBody.Part.createFormData("goods_description", description);
        MultipartBody.Part goodsLongitude = MultipartBody.Part.createFormData("goods_longitude", String.valueOf(longitude));
        MultipartBody.Part goodsLatitude = MultipartBody.Part.createFormData("goods_latitude", String.valueOf(latitude));
        MultipartBody.Part goodsBrandNew = MultipartBody.Part.createFormData("goods_is_brandNew", isBrandNew ? "1" : "0");
        MultipartBody.Part goodsQuotable = MultipartBody.Part.createFormData("goods_is_quotable", isQuotable ? "1" : "0");
        MultipartBody.Part goodsPrice = MultipartBody.Part.createFormData("goods_price", String.valueOf(price));

//        LogUtils.d("token " + token + "\ncover " + cover + "\nname " + name + " des " + description + "\nlongitude " + longitude + " latitude " + latitude
//                + "\nisBrandNew " + isBrandNew + " isQuotable " + isQuotable);

        retrofit2.Call<DomainResult> task = api.postNewItem(
                token, goodsCover, goodsSet, goodsName, goodsDes, goodsLongitude, goodsLatitude, goodsQuotable, goodsBrandNew, goodsPrice);

        task.enqueue(new Callback<DomainResult>() {
            @Override
            public void onResponse(retrofit2.Call<DomainResult> call, Response<DomainResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainResult body = response.body();
                    if (body != null)

//                        try {
//                            LogUtils.d(body.string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                        mCallback.onResult(body);
                } else {
                    try {
                        LogUtils.d(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mCallback.onError();
                }
            }

            @Override
            public void onFailure(Call<DomainResult> call, Throwable t) {
                LogUtils.d(t.toString());
                mCallback.onError();
            }
        });
    }

    /**
     * 创建文件类型的Part
     */
    private MultipartBody.Part createFileWithPath(String key, String path) {
        File file = new File(path);
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData(key, file.getName(), body);
    }

    @Override
    public void registerCallback(ISellingAddNewCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(ISellingAddNewCallback callback) {
        mCallback = null;
    }
}
