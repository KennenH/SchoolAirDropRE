package com.example.schoolairdroprefactoredition.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.schoolairdroprefactoredition.cache.NewItemDraftCache
import com.example.schoolairdroprefactoredition.cache.NewPostDraftCache
import com.example.schoolairdroprefactoredition.repository.SellingAddNewRepository
import com.example.schoolairdroprefactoredition.repository.UploadRepository
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.coroutines.launch

class AddNewViewModel(application: Application) : AndroidViewModel(application) {

    private val mSubmitItemResult = MutableLiveData<Boolean>()

    private val mSubmitPostResult = MutableLiveData<Boolean>()

    private val mRestoredItemDraft = MutableLiveData<NewItemDraftCache>()

    private val mRestoredPostDraft = MutableLiveData<NewPostDraftCache>()

    private val sellingAddNewRepository: SellingAddNewRepository = SellingAddNewRepository.getInstance()

    private val uploadRepository: UploadRepository = UploadRepository.getInstance()

    /**
     * 提交新物品
     */
    fun submitItem(token: String, cover: String, picSet: ArrayList<String>,
                   title: String, description: String,
                   longitude: Double, latitude: Double,
                   isBrandNew: Boolean, isQuotable: Boolean, price: Float): LiveData<Boolean> {
        viewModelScope.launch {
            // 将封面图片加入图片集中，为的是确保服务器收到的数组第一张图片是封面
            picSet.add(cover)
            // 先上传图片
            uploadRepository.uploadImage(getApplication(), picSet, ConstantUtil.UPLOAD_TYPE_GOODS) { success, response ->
                if (success) {
                    val cov = response?.data?.cover
                    val pics = response?.data?.images
                    if (cov != null && pics != null) {
                        // 图片上传成功之后将服务器返回的图片路径再发送
                        sellingAddNewRepository.submitNewItem(token, cov, pics,
                                title, description,
                                longitude, latitude,
                                isBrandNew, isQuotable, price) {
                            mSubmitItemResult.postValue(it)
                        }
                    } else {
                        mSubmitItemResult.postValue(false)
                    }
                } else {
                    mSubmitItemResult.postValue(false)
                }
            }
        }
        return mSubmitItemResult
    }

    /**
     * 提交帖子
     */
    fun submitPost(token: String,
                   cover: String,
                   HWRatio: Float,
                   picSet: List<String>,
                   title: String,
                   content: String,
                   longitude: Double, latitude: Double): LiveData<Boolean> {
        sellingAddNewRepository.submitNewPost(
                token, cover,
                HWRatio, picSet,
                title, content,
                longitude, latitude,
                getApplication()) {

        }
        return mSubmitPostResult
    }

    /**
     * 保存用户物品草稿
     */
    fun saveItemDraft(cover: String, picSet: List<LocalMedia>, title: String, description: String, price: String, isQuotable: Boolean, isSecondHand: Boolean) {
        sellingAddNewRepository.saveItemDraft(cover, picSet, title, description, price, isQuotable, isSecondHand)
    }

    /**
     * 恢复用户物品草稿
     */
    fun restoreItemDraft(): LiveData<NewItemDraftCache> {
        viewModelScope.launch {
            sellingAddNewRepository.restoreItemDraft {
                mRestoredItemDraft.postValue(it)
            }
        }
        return mRestoredItemDraft
    }

    /**
     * 保存用户帖子草稿
     */
    fun savePostDraft(cover: String, hwRatio: Float, picSet: List<LocalMedia>, tag: String, anonymous: Boolean, title: String, content: String) {
        viewModelScope.launch {
            sellingAddNewRepository.savePostDraft(cover, hwRatio, picSet, tag, anonymous, title, content)
        }
    }

    /**
     * 恢复用户帖子草稿
     */
    fun restorePostDraft(): LiveData<NewPostDraftCache> {
        viewModelScope.launch {
            sellingAddNewRepository.restorePostDraft {
                mRestoredPostDraft.postValue(it)
            }
        }
        return mRestoredPostDraft
    }

    /**
     * 删除物品草稿
     */
    fun deleteItemDraft() {
        sellingAddNewRepository.deleteItemDraft()
    }

    /**
     * 删除帖子草稿
     */
    fun deletePostDraft() {
        sellingAddNewRepository.deletePostDraft()
    }
}