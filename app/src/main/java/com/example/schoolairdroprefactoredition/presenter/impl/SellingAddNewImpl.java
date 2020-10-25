package com.example.schoolairdroprefactoredition.presenter.impl;

import android.content.Context;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.example.schoolairdroprefactoredition.domain.DomainResult;
import com.example.schoolairdroprefactoredition.model.Api;
import com.example.schoolairdroprefactoredition.model.CallBackWithRetry;
import com.example.schoolairdroprefactoredition.model.RetrofitManager;
import com.example.schoolairdroprefactoredition.presenter.ISellingAddNewPresenter;
import com.example.schoolairdroprefactoredition.presenter.callback.ISellingAddNewCallback;
import com.example.schoolairdroprefactoredition.scene.addnew.AddNewDraftCache;
import com.example.schoolairdroprefactoredition.utils.FileUtil;
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil;
import com.luck.picture.lib.entity.LocalMedia;

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

import static com.example.schoolairdroprefactoredition.utils.FileUtil.createFileWithPath;

public class SellingAddNewImpl implements ISellingAddNewPresenter {

    private ISellingAddNewCallback mCallback;
    private JsonCacheUtil mJsonCacheUtil = JsonCacheUtil.newInstance();

    @Override
    public void submit(String token, String cover, List<String> picSet,
                       String name, String description,
                       double longitude, double latitude,
                       boolean isBrandNew, boolean isQuotable,
                       float price, Context context) {

        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);

        List<MultipartBody.Part> goodsSet = new ArrayList<>();
        int i = 0;
        for (String pic : picSet)
            goodsSet.add(createFileWithPath(context, "goods_img_set" + i++, pic, true));

        MultipartBody.Part goodsCover = createFileWithPath(context, "goods_img_cover", cover, true);
        if (goodsCover == null && mCallback != null)
            mCallback.onAddNewItemError();

        MultipartBody.Part goodsName = MultipartBody.Part.createFormData("goods_name", name);
        MultipartBody.Part goodsDes = MultipartBody.Part.createFormData("goods_description", description);

        MultipartBody.Part goodsLongitude = MultipartBody.Part.createFormData("goods_longitude", String.valueOf(longitude));
        MultipartBody.Part goodsLatitude = MultipartBody.Part.createFormData("goods_latitude", String.valueOf(latitude));
//        MultipartBody.Part goodsLongitude = MultipartBody.Part.createFormData("goods_longitude", String.valueOf(120.36055));
//        MultipartBody.Part goodsLatitude = MultipartBody.Part.createFormData("goods_latitude", String.valueOf(30.31747));

        MultipartBody.Part goodsBrandNew = MultipartBody.Part.createFormData("goods_is_brandNew", isBrandNew ? "1" : "0");
        MultipartBody.Part goodsQuotable = MultipartBody.Part.createFormData("goods_is_quotable", isQuotable ? "1" : "0");
        MultipartBody.Part goodsPrice = MultipartBody.Part.createFormData("goods_price", String.valueOf(price));

        retrofit2.Call<DomainResult> task = api.postNewItem(
                token, goodsCover, goodsSet, goodsName, goodsDes, goodsLongitude, goodsLatitude, goodsQuotable, goodsBrandNew, goodsPrice);

        task.enqueue(new CallBackWithRetry<DomainResult>(task) {
            @Override
            public void onResponse(retrofit2.Call<DomainResult> call, Response<DomainResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    DomainResult body = response.body();

//                    try {
//                        LogUtils.d(response.body().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    if (mCallback != null)
                        if (body != null) {
                            mCallback.onSubmitResult(body);
                        } else {
                            mCallback.onAddNewItemError();
                        }
                } else {
                    try {
                        LogUtils.d(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (mCallback != null)
                        mCallback.onAddNewItemError();
                }
            }

            @Override
            public void onFailureAllRetries() {
                if (mCallback != null)
                    mCallback.onAddNewItemError();
            }
        });
    }

    @Override
    public void save(String cover, List<LocalMedia> picSet, String title, String description, String price, boolean isQuotable, boolean isSecondHand) {
        AddNewDraftCache draft = mJsonCacheUtil.getValue(AddNewDraftCache.ADD_NEW_DRAFT, AddNewDraftCache.class);
        if (draft == null) draft = new AddNewDraftCache();
        draft.setCover(cover);
        draft.setPicSet(picSet);
        draft.setTitle(title);
        draft.setDescription(description);
        draft.setPrice(price);
        draft.setNegotiable(isQuotable);
        draft.setSecondHand(isSecondHand);
        mJsonCacheUtil.saveCache(AddNewDraftCache.ADD_NEW_DRAFT, draft);
    }

    @Override
    public void deleteDraft() {
        mJsonCacheUtil.saveCache(AddNewDraftCache.ADD_NEW_DRAFT, null);
    }

    @Override
    public void recoverDraft() {
        AddNewDraftCache draft = mJsonCacheUtil.getValue(AddNewDraftCache.ADD_NEW_DRAFT, AddNewDraftCache.class);
        mCallback.onDraftRecovered(draft);
    }

//    /**
//     * 创建文件类型的Part
//     */
//    private MultipartBody.Part createFileWithPath(String key, String path) {
//        File file = new File(path);
//        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
//        return MultipartBody.Part.createFormData(key, file.getName(), body);
//    }

    @Override
    public void registerCallback(ISellingAddNewCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterCallback(ISellingAddNewCallback callback) {
        mCallback = null;
    }
}
