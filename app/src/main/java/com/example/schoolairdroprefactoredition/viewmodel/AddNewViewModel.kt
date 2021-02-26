package com.example.schoolairdroprefactoredition.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.cache.NewItemDraftCache
import com.example.schoolairdroprefactoredition.cache.NewPostDraftCache
import com.example.schoolairdroprefactoredition.repository.AddNewRepository
import com.example.schoolairdroprefactoredition.repository.UploadRepository
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.luck.picture.lib.entity.LocalMedia
import com.qiniu.android.utils.LogUtil
import kotlinx.coroutines.launch

class AddNewViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * 恢复上传物品页面草稿 结果
     */
    private val restoredItemDraftLiveData = MutableLiveData<NewItemDraftCache>()

    /**
     * 恢复上传帖子页面草稿 结果
     */
    private val restoredPostDraftLiveData = MutableLiveData<NewPostDraftCache>()

    private val addNewRepository: AddNewRepository = AddNewRepository.getInstance()

    private val uploadRepository: UploadRepository = UploadRepository.getInstance()

    /**
     * 提交新物品
     */
    fun submitItem(token: String,
                   cover: String, picSet: ArrayList<String>,
                   title: String, description: String,
                   longitude: Double, latitude: Double,
                   isBrandNew: Boolean, isQuotable: Boolean, price: Float): LiveData<Boolean> {
        val submitItemLiveData = MutableLiveData<Boolean>()
        viewModelScope.launch {
            // 将封面图片加入图片集中，为的是确保服务器收到的数组第一张图片是封面
            picSet.add(0, cover)
            // 上传图片
            uploadRepository.upload(token, picSet, ConstantUtil.UPLOAD_TYPE_GOODS) { taskAndKeys ->
                if (taskAndKeys != null) {
                    // 上传物品
                    addNewRepository.submitNewItem(
                            token, taskAndKeys.taskId,
                            taskAndKeys.keys[0], taskAndKeys.keys.joinToString(","),
                            title, description,
                            longitude, latitude,
                            isBrandNew, isQuotable, price) {
                        submitItemLiveData.postValue(it)
                    }
                } else {
                    submitItemLiveData.postValue(false)
                }
            }
        }
        return submitItemLiveData
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
        val submitPostLiveData = MutableLiveData<Boolean>()
        addNewRepository.submitNewPost(
                token, cover,
                HWRatio, picSet,
                title, content,
                longitude, latitude,
                getApplication()) {
        }
        return submitPostLiveData
    }

    /**
     * 保存用户物品草稿
     */
    fun saveItemDraft(cover: String, picSet: List<LocalMedia>, title: String, description: String, price: String, isQuotable: Boolean, isSecondHand: Boolean) {
        addNewRepository.saveItemDraft(cover, picSet, title, description, price, isQuotable, isSecondHand)
    }

    /**
     * 恢复用户物品草稿
     */
    fun restoreItemDraft(): LiveData<NewItemDraftCache> {
        restoredItemDraftLiveData.value = addNewRepository.restoreItemDraft()
        return restoredItemDraftLiveData
    }

    /**
     * 保存用户帖子草稿
     */
    fun savePostDraft(cover: String, hwRatio: Float, picSet: List<LocalMedia>, tag: String, anonymous: Boolean, title: String, content: String) {
        addNewRepository.savePostDraft(cover, hwRatio, picSet, tag, anonymous, title, content)
    }

    /**
     * 恢复用户帖子草稿
     */
    fun restorePostDraft(): LiveData<NewPostDraftCache> {
        restoredPostDraftLiveData.value = addNewRepository.restorePostDraft()
        return restoredPostDraftLiveData
    }

    /**
     * 删除物品草稿
     */
    fun deleteItemDraft() {
        addNewRepository.deleteItemDraft()
    }

    /**
     * 删除帖子草稿
     */
    fun deletePostDraft() {
        addNewRepository.deletePostDraft()
    }
}