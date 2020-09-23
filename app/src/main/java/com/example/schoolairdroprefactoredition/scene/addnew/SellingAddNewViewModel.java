package com.example.schoolairdroprefactoredition.scene.addnew;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.domain.DomainResult;
import com.example.schoolairdroprefactoredition.presenter.callback.ISellingAddNewCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SellingAddNewImpl;
import com.example.schoolairdroprefactoredition.scene.main.base.BaseStateViewModel;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

public class SellingAddNewViewModel extends BaseStateViewModel implements ISellingAddNewCallback {

    private MutableLiveData<DomainResult> mSubmitResult = new MutableLiveData<>();
    private MutableLiveData<AddNewDraftCache> mRecoveredDraft = new MutableLiveData<>();

    private SellingAddNewImpl sellingAddNewImpl;

    public SellingAddNewViewModel() {
        sellingAddNewImpl = new SellingAddNewImpl();
        sellingAddNewImpl.registerCallback(this);
    }

    /**
     * 提交新物品
     */
    public LiveData<DomainResult> submit(String token, String cover, List<String> picSet,
                                         String name, String description,
                                         double longitude, double latitude,
                                         boolean isBrandNew, boolean isQuotable, float price) {
        sellingAddNewImpl.submit(token, cover, picSet,
                name, description,
                longitude, latitude,
                isBrandNew, isQuotable, price);
        return mSubmitResult;
    }

    /**
     * 保存用户草稿
     */
    public void save(String cover, List<LocalMedia> picSet, String title, String description, String price, boolean isQuotable, boolean isSecondHand) {
        sellingAddNewImpl.save(cover, picSet, title, description, price, isQuotable, isSecondHand);
    }

    /**
     * 恢复用户草稿
     */
    public LiveData<AddNewDraftCache> recoverDraft() {
        sellingAddNewImpl.recoverDraft();
        return mRecoveredDraft;
    }

    /**
     * 删除草稿
     */
    public void deleteDraft() {
        sellingAddNewImpl.deleteDraft();
    }

    @Override
    public void onSubmitResult(DomainResult result) {
        mSubmitResult.postValue(result);
    }

    @Override
    public void onDraftRecovered(AddNewDraftCache draftCache) {
        mRecoveredDraft.postValue(draftCache);
    }

    @Override
    public void onError() {
        mOnRequestListener.onError();
    }
}
