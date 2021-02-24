package com.example.schoolairdroprefactoredition.repository

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.cache.NewItemDraftCache
import com.example.schoolairdroprefactoredition.cache.NewPostDraftCache
import com.example.schoolairdroprefactoredition.domain.DomainResult
import com.example.schoolairdroprefactoredition.utils.AppConfig
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil
import com.luck.picture.lib.entity.LocalMedia
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*

class AddNewRepository {

    private val mJsonCacheUtil = JsonCacheUtil.getInstance()

    companion object {
        private var INSTANCE: AddNewRepository? = null
        fun getInstance() = INSTANCE
                ?: AddNewRepository().also {
                    INSTANCE = it
                }
    }

    /**
     * 上传物品
     *
     * 不包含图片上传
     */
    fun submitNewItem(token: String, taskID: String,
                      coverKey: String, picSetKeys: String,
                      title: String, content: String,
                      longitude: Double, latitude: Double,
                      isBrandNew: Boolean, isQuotable: Boolean, price: Float,
                      onResult: (success: Boolean) -> Unit) {
        RetrofitClient.goodsApi.postNewItem(
                token, taskID,
                coverKey, picSetKeys,
                title, content,
                if (AppConfig.IS_DEBUG) AppConfig.DEBUG_LONGITUDE else longitude.toString(),
                if (AppConfig.IS_DEBUG) AppConfig.DEBUG_LATITUDE else latitude.toString(),
                if (isQuotable) 1 else 0, if (!isBrandNew) 1 else 0,
                price.toString()).apply {
            enqueue(object : CallBackWithRetry<DomainResult>(this@apply) {
                override fun onResponse(call: Call<DomainResult>, response: Response<DomainResult>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val body = response.body()
                        if (body != null && body.isSuccess) {
                            onResult(true)
                        } else {
                            onResult(false)
                        }
                    } else {
                        LogUtils.d(response.errorBody()?.string())
                        onResult(false)
                    }
                }

                override fun onFailureAllRetries() {
                    onResult(false)
                }
            })
        }
    }

    /**
     * 上传帖子
     *
     * 不包含图片上传
     */
    fun submitNewPost(
            token: String, cover: String,
            hwRatio: Float, picSet: List<String>,
            title: String, content: String,
            longitude: Double, latitude: Double,
            context: Context,
            onResult: (success: Boolean) -> Unit) {

    }

    /**
     * 保存物品页面的草稿
     */
    fun saveItemDraft(cover: String, picSet: List<LocalMedia>, title: String, description: String, price: String, isQuotable: Boolean, isSecondHand: Boolean) {
        var draft = mJsonCacheUtil.getCache(NewItemDraftCache.KEY, NewItemDraftCache::class.java)
        if (draft == null) {
            draft = NewItemDraftCache()
        }
        draft.cover = cover
        draft.picSet = picSet
        draft.title = title
        draft.description = description
        draft.price = price
        draft.isNegotiable = isQuotable
        draft.isSecondHand = isSecondHand
        mJsonCacheUtil.saveCache(NewItemDraftCache.KEY, draft)
    }

    /**
     * 保存帖子页面的草稿
     */
    fun savePostDraft(cover: String, hwRatio: Float, picSet: List<LocalMedia>, tag: String, anonymous: Boolean, title: String, content: String) {
        var draft = mJsonCacheUtil.getCache(NewPostDraftCache.KEY, NewPostDraftCache::class.java)
        if (draft == null) draft = NewPostDraftCache()
        draft.cover = cover
        draft.hwRatio = hwRatio
        draft.picSet = picSet
        draft.tag = tag
        draft.isAnonymous = anonymous
        draft.title = title
        draft.content = content
        mJsonCacheUtil.saveCache(NewPostDraftCache.KEY, draft)
    }

    /**
     * 删除物品页面草稿
     */
    fun deleteItemDraft() {
        mJsonCacheUtil.saveCache(NewItemDraftCache.KEY, null)
    }

    /**
     * 删除帖子页面草稿
     */
    fun deletePostDraft() {
        mJsonCacheUtil.saveCache(NewPostDraftCache.KEY, null)
    }

    /**
     * 恢复物品页面草稿
     */
    fun restoreItemDraft(): NewItemDraftCache? {
        return mJsonCacheUtil.getCache(NewItemDraftCache.KEY, NewItemDraftCache::class.java)
    }

    /**
     * 恢复帖子页面草稿
     */
    fun restorePostDraft(): NewPostDraftCache? {
        return mJsonCacheUtil.getCache(NewPostDraftCache.KEY, NewPostDraftCache::class.java)
    }
}