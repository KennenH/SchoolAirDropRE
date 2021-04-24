package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.api.base.CallbackResultOrNull
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.cache.NewIWantDraftCache
import com.example.schoolairdroprefactoredition.cache.NewItemDraftCache
import com.example.schoolairdroprefactoredition.cache.util.JsonCacheUtil
import com.example.schoolairdroprefactoredition.domain.DomainIWantTags
import com.example.schoolairdroprefactoredition.domain.DomainResult
import com.example.schoolairdroprefactoredition.utils.AppConfig
import com.luck.picture.lib.entity.LocalMedia

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
            onResult: (success: DomainResult?) -> Unit) {
        RetrofitClient.goodsApi.postNewItem(
                token, taskID,
                coverKey, picSetKeys,
                title, content,
                longitude.toString(),
                latitude.toString(),
                if (isQuotable) 1 else 0, if (!isBrandNew) 1 else 0,
                price.toString()).apply {
            enqueue(CallbackResultOrNull(this, onResult))
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
                        onResult: (response: DomainResult?) -> Unit) {
        RetrofitClient.goodsApi.modifyGoodsInfo(
                token, taskID,
                imagesToDelete, picSetKeys,
                goodsID, title, content,
                if (AppConfig.IS_DEBUG) AppConfig.DEBUG_LONGITUDE else longitude,
                if (AppConfig.IS_DEBUG) AppConfig.DEBUG_LATITUDE else latitude,
                if (isQuotable) 1 else 0, if (!isBrandNew) 1 else 0,
                price.toString()).apply {
            enqueue(CallbackResultOrNull(this, onResult))
        }
    }

    /**
     * 上传求购
     *
     * 仅包含与服务器文字交互部分，同[submitItem]
     */
    fun submitNewIWant(
            token: String, tagID: Int, color: Int,
            picSetKeys: String, content: String,
            longitude: Double, latitude: Double,
            onResult: (response: DomainResult?) -> Unit) {
        RetrofitClient.iWantApi.submitIWant(
                token, tagID, color,
                picSetKeys, content,
                longitude, latitude).apply {
            enqueue(CallbackResultOrNull(this, onResult))
        }
    }

    /**
     * 修改求购
     *
     * 仅包含与服务器文字交互部分，同上
     */
    fun modifyIWant(
            token: String, tagID: Int, color: Int,
            deleteImages: String, picSetKeys: String,
            content: String, longitude: Double, latitude: Double,
            onResult: (response: DomainResult?) -> Unit) {
        RetrofitClient.iWantApi.modifyIWant(
                token, tagID, color,
                deleteImages, picSetKeys,
                content, longitude, latitude).apply {
            enqueue(CallbackResultOrNull(this, onResult))
        }
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
    fun savePostDraft(
            picSet: List<LocalMedia>,
            tag: DomainIWantTags.Data,
            content: String,
            cardColor: Int) {
        var draft = mJsonCacheUtil.getCache(NewIWantDraftCache.KEY, NewIWantDraftCache::class.java)
        if (draft == null) draft = NewIWantDraftCache()
        draft.picSet = picSet
        draft.tag = tag
        draft.content = content
        draft.cardColor = cardColor
        mJsonCacheUtil.saveCache(NewIWantDraftCache.KEY, draft)
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
        mJsonCacheUtil.saveCache(NewIWantDraftCache.KEY, null)
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
    fun restoreInquiryDraft(): NewIWantDraftCache? {
        return mJsonCacheUtil.getCache(NewIWantDraftCache.KEY, NewIWantDraftCache::class.java)
    }
}