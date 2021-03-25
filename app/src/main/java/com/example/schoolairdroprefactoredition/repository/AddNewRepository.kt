package com.example.schoolairdroprefactoredition.repository

import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.cache.NewItemDraftCache
import com.example.schoolairdroprefactoredition.cache.NewInquiryDraftCache
import com.example.schoolairdroprefactoredition.domain.DomainResult
import com.example.schoolairdroprefactoredition.cache.util.JsonCacheUtil
import com.example.schoolairdroprefactoredition.utils.AppConfig
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.MyUtil
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
     * 上传物品表单
     *
     * 不包含图片上传，仅与服务器交互部分
     *
     * @param coverKey 封面图片在七牛云上的路径，即整个上传流程中从上一步获取的cover key
     * @param picSetKeys 图片集在七牛云上的路径，以逗号分隔，同上
     */
    fun submitItem(
            token: String, taskID: String,
            coverKey: String, picSetKeys: String,
            title: String, content: String,
            longitude: Double, latitude: Double,
            isBrandNew: Boolean, isQuotable: Boolean, price: Float,
            onResult: (success: Boolean) -> Unit) {
        RetrofitClient.goodsApi.postNewItem(
                MyUtil.bearerToken(token), taskID,
                coverKey, picSetKeys,
                title, content,
                longitude.toString(),
                latitude.toString(),
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
     * 修改物品信息
     *
     * 仅包含与服务器交互部分，同[submitItem]
     */
    fun modifyGoodsInfo(token: String, taskID: String,
                        imagesToDelete: String, picSetKeys: String,
                        goodsID: Int, title: String, content: String,
                        longitude: Double, latitude: Double,
                        isBrandNew: Boolean, isQuotable: Boolean, price: Float,
                        onResult: (success: Boolean) -> Unit) {
        RetrofitClient.goodsApi.modifyGoodsInfo(
                MyUtil.bearerToken(token), taskID,
                imagesToDelete, picSetKeys,
                goodsID, title, content,
                if (AppConfig.IS_DEBUG) AppConfig.DEBUG_LONGITUDE else longitude,
                if (AppConfig.IS_DEBUG) AppConfig.DEBUG_LATITUDE else latitude,
                if (isQuotable) 1 else 0, if (!isBrandNew) 1 else 0,
                price.toString()).apply {
            enqueue(object : CallBackWithRetry<DomainResult>(this@apply) {
                override fun onResponse(call: Call<DomainResult>, response: Response<DomainResult>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        val body = response.body()
                        onResult(body?.code == ConstantUtil.HTTP_OK)
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
     * 上传求购
     *
     * 仅包含与服务器交互部分，同[submitItem]
     */
    fun submitNewInquiry(
            token: String, cover: String,
            hwRatio: Float, picSet: List<String>,
            title: String, content: String,
            longitude: Double, latitude: Double,
            onResult: (success: Boolean) -> Unit) {
        // TODO: 2021/3/19 上传新求购
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
        var draft = mJsonCacheUtil.getCache(NewInquiryDraftCache.KEY, NewInquiryDraftCache::class.java)
        if (draft == null) draft = NewInquiryDraftCache()
        draft.cover = cover
        draft.hwRatio = hwRatio
        draft.picSet = picSet
        draft.tag = tag
        draft.isAnonymous = anonymous
        draft.title = title
        draft.content = content
        mJsonCacheUtil.saveCache(NewInquiryDraftCache.KEY, draft)
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
        mJsonCacheUtil.saveCache(NewInquiryDraftCache.KEY, null)
    }

    /**
     * 恢复物品页面草稿
     */
    fun restoreItemDraft(): NewItemDraftCache? {
        return mJsonCacheUtil.getCache(NewItemDraftCache.KEY, NewItemDraftCache::class.java)
    }

    /**
     * 恢复求购页面草稿
     */
    fun restoreInquiryDraft(): NewInquiryDraftCache? {
        return mJsonCacheUtil.getCache(NewInquiryDraftCache.KEY, NewInquiryDraftCache::class.java)
    }
}