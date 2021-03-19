package com.example.schoolairdroprefactoredition.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.R
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
                   isBrandNew: Boolean, isQuotable: Boolean, price: Float): LiveData<Triple<Boolean, Pair<Int, Boolean>, Boolean>> {
        // Triple中 第一个bool为上一步是否成功，第二个string为tip的res id，第三个bool为总流程是否成功完成
        // Pair中 第一个Int为tip本身，Boolean为是否是res id，若为false则表示第一个Int是纯数字，此时需要
        // 使用 正在上传第%d张图片 然后填入改数字，若为true则外部直接使用getString将其转换为文字
        val submitItemLiveData = MutableLiveData<Triple<Boolean, Pair<Int, Boolean>, Boolean>>()
        viewModelScope.launch {
            // 将封面图片加入图片集中，为的是确保服务器收到的数组第一张图片是封面
            picSet.add(0, cover)
            // 上传图片
            uploadRepository.upload(
                    token,
                    picSet,
                    ConstantUtil.UPLOAD_TYPE_GOODS,
                    isSubscribeProgress = true) { success, tip, taskAndKeys, allSuccess ->
                // 上一步完成
                if (success) {
                    // 所有步骤都完成
                    if (allSuccess) {
                        // 返回出来的结果为空
                        if (taskAndKeys != null) {
                            submitItemLiveData.postValue(Triple(true, Pair(R.string.requestingServer, true), false))
                            // 上传物品
                            addNewRepository.submitNewItem(
                                    token, taskAndKeys.taskId,
                                    taskAndKeys.keys[0], taskAndKeys.keys.subList(1, taskAndKeys.keys.size).joinToString(","),
                                    title, description,
                                    longitude, latitude,
                                    isBrandNew, isQuotable, price) {
                                submitItemLiveData.postValue(Triple(true, Pair(R.string.uploadSuccess, true), it))
                            }
                        } else {
                            submitItemLiveData.postValue(Triple(false, Pair(-1, false), false))
                        }
                    } else {
                        // 这里出来是订阅的每一步流程，需要外部显示tip
                        submitItemLiveData.postValue(Triple(true, tip, false))
                    }
                } else {
                    // 上一步出错，流程终止
                    submitItemLiveData.postValue(Triple(false, Pair(-1, false), false))
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