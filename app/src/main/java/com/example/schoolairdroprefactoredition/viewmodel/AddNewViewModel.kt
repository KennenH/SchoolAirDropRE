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

    private val submitItemLiveData = MutableLiveData<Boolean>()

    private val submitPostLiveData = MutableLiveData<Boolean>()

    private val restoredItemDraftLiveData = MutableLiveData<NewItemDraftCache>()

    private val restoredPostDraftLiveData = MutableLiveData<NewPostDraftCache>()

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
            uploadRepository.doUpload(token, picSet, ConstantUtil.UPLOAD_TYPE_GOODS) { paths ->
                if (paths != null) {
                    sellingAddNewRepository.submitNewItem(token, paths[0], paths.subList(1, paths.size).joinToString(","),
                            title, description,
                            longitude, latitude,
                            isBrandNew, isQuotable, price) {
                        submitItemLiveData.postValue(it)
                    }
                } else {
                    submitItemLiveData.postValue(false)
                }
            }

            uploadRepository.uploadImage(getApplication(), picSet, ConstantUtil.UPLOAD_TYPE_GOODS) { response ->
                if (response != null) {
                    val cov = response.data?.cover
                    val pics = response.data?.images
                    if (cov != null && pics != null) {
                        // 图片上传成功之后将服务器返回的图片路径再发送
                        sellingAddNewRepository.submitNewItem(token, cov, pics,
                                title, description,
                                longitude, latitude,
                                isBrandNew, isQuotable, price) {
                            submitItemLiveData.postValue(it)
                        }
                    } else {
                        submitItemLiveData.postValue(false)
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
        sellingAddNewRepository.submitNewPost(
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
        sellingAddNewRepository.saveItemDraft(cover, picSet, title, description, price, isQuotable, isSecondHand)
    }

    /**
     * 恢复用户物品草稿
     */
    fun restoreItemDraft(): LiveData<NewItemDraftCache> {
        viewModelScope.launch {
            sellingAddNewRepository.restoreItemDraft {
                restoredItemDraftLiveData.postValue(it)
            }
        }
        return restoredItemDraftLiveData
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
                restoredPostDraftLiveData.postValue(it)
            }
        }
        return restoredPostDraftLiveData
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