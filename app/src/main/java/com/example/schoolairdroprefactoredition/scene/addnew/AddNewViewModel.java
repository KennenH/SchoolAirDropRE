package com.example.schoolairdroprefactoredition.scene.addnew;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.schoolairdroprefactoredition.cache.NewItemDraftCache;
import com.example.schoolairdroprefactoredition.cache.NewPostDraftCache;
import com.example.schoolairdroprefactoredition.domain.DomainResult;
import com.example.schoolairdroprefactoredition.presenter.callback.ISellingAddNewCallback;
import com.example.schoolairdroprefactoredition.presenter.impl.SellingAddNewImpl;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

public class AddNewViewModel extends AndroidViewModel implements ISellingAddNewCallback {

    private final MutableLiveData<DomainResult> mSubmitItemResult = new MutableLiveData<>();
    private final MutableLiveData<DomainResult> mSubmitPostResult = new MutableLiveData<>();

    private final MutableLiveData<NewItemDraftCache> mRestoredItemDraft = new MutableLiveData<>();
    private final MutableLiveData<NewPostDraftCache> mRestoredPostDraft = new MutableLiveData<>();

    private final SellingAddNewImpl sellingAddNewImpl;

    private OnRequestListener mOnRequestListener;

    public AddNewViewModel(@NonNull Application application) {
        super(application);
        sellingAddNewImpl = SellingAddNewImpl.getInstance();
        sellingAddNewImpl.registerCallback(this);
    }

    /**
     * 提交新物品
     */
    public LiveData<DomainResult> submitItem(String token, String cover, List<String> picSet,
                                             String title, String description,
                                             double longitude, double latitude,
                                             boolean isBrandNew, boolean isQuotable, float price) {
        sellingAddNewImpl.submitNewItem(token, cover, picSet,
                title, description,
                longitude, latitude,
                isBrandNew, isQuotable, price, getApplication());
        return mSubmitItemResult;
    }

    /**
     * 提交帖子
     */
    public LiveData<DomainResult> submitPost(String token,
                                             String cover,
                                             float HWRatio,
                                             List<String> picSet,
                                             String title,
                                             String content,
                                             double longitude, double latitude) {
        sellingAddNewImpl.submitNewPost(token, cover,
                HWRatio, picSet,
                title, content,
                longitude, latitude,
                getApplication());
        return mSubmitPostResult;
    }

    /**
     * 保存用户物品草稿
     */
    public void saveItemDraft(String cover, List<LocalMedia> picSet, String title, String description, String price, boolean isQuotable, boolean isSecondHand) {
        sellingAddNewImpl.saveItemDraft(cover, picSet, title, description, price, isQuotable, isSecondHand);
    }

    /**
     * 恢复用户物品草稿
     */
    public LiveData<NewItemDraftCache> restoreItemDraft() {
        sellingAddNewImpl.restoreItemDraft();
        return mRestoredItemDraft;
    }

    /**
     * 保存用户帖子草稿
     */
    public void savePostDraft(String cover, float hwRatio, List<LocalMedia> picSet, String title, String content) {
        sellingAddNewImpl.savePostDraft(cover, hwRatio, picSet, title, content);
    }

    /**
     * 恢复用户帖子草稿
     */
    public LiveData<NewPostDraftCache> restorePostDraft() {
        sellingAddNewImpl.restorePostDraft();
        return mRestoredPostDraft;
    }

    /**
     * 删除物品草稿
     */
    public void deleteItemDraft() {
        sellingAddNewImpl.deleteItemDraft();
    }

    /**
     * 删除帖子草稿
     */
    public void deletePostDraft() {
        sellingAddNewImpl.deletePostDraft();
    }

    @Override
    public void onSubmitItemResult(DomainResult result) {
        mSubmitItemResult.postValue(result);
    }

    @Override
    public void onItemDraftRestored(NewItemDraftCache draftCache) {
        mRestoredItemDraft.postValue(draftCache);
    }

    @Override
    public void onSubmitPostItemResult(DomainResult result) {
        mSubmitPostResult.postValue(result);
    }

    @Override
    public void onPostDraftRestored(NewPostDraftCache draftCache) {
        mRestoredPostDraft.postValue(draftCache);
    }

    @Override
    public void onAddNewItemError() {
        if (mOnRequestListener != null) {
            mOnRequestListener.onAddNewItemError();
        }
    }

    @Override
    public void onModifyInfoError() {
        if (mOnRequestListener != null) {
            mOnRequestListener.onModifyInfoError();
        }
    }

    @Override
    public void onAddNewPostError() {
        if (mOnRequestListener != null) {
            mOnRequestListener.onAddNewPostError();
        }
    }

    @Override
    public void onOtherError() {
        if (mOnRequestListener != null) {
            mOnRequestListener.onOtherError();
        }
    }

    public interface OnRequestListener {
        void onAddNewItemError();

        void onModifyInfoError();

        void onAddNewPostError();

        void onOtherError();
    }

    public void setOnRequestListener(OnRequestListener listener) {
        mOnRequestListener = listener;
    }

}
