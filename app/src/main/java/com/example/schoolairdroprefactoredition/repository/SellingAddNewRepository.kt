package com.example.schoolairdroprefactoredition.repository

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.example.schoolairdroprefactoredition.api.base.CallBackWithRetry
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.cache.NewItemDraftCache
import com.example.schoolairdroprefactoredition.cache.NewPostDraftCache
import com.example.schoolairdroprefactoredition.domain.DomainResult
import com.example.schoolairdroprefactoredition.utils.JsonCacheUtil
import com.example.schoolairdroprefactoredition.utils.MyUtil
import com.luck.picture.lib.entity.LocalMedia
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*

class SellingAddNewRepository {

    private val mJsonCacheUtil = JsonCacheUtil.newInstance()

    companion object {
        private var INSTANCE: SellingAddNewRepository? = null
        fun getInstance() = INSTANCE
                ?: SellingAddNewRepository().also {
                    INSTANCE = it
                }
    }

    fun submitNewItem(token: String, cover: String, picSet: String,
                      title: String, content: String,
                      longitude: Double, latitude: Double,
                      isBrandNew: Boolean, isQuotable: Boolean, price: Float,
                      onResult: (success: Boolean) -> Unit) {

        val goodsName = MultipartBody.Part.createFormData("goods_name", title)
        val goodsCover = MultipartBody.Part.createFormData("goods_cover_image", cover)
        val goodsSet = MultipartBody.Part.createFormData("goods_images", picSet)
        val goodsContent = MultipartBody.Part.createFormData("goods_content", content)
        val goodsLongitude = MultipartBody.Part.createFormData("goods_longitude", longitude.toString())
        val goodsLatitude = MultipartBody.Part.createFormData("goods_latitude", latitude.toString())
        val goodsType = MultipartBody.Part.createFormData("goods_type", MyUtil.getGoodsType(isQuotable, !isBrandNew))
        val goodsPrice = MultipartBody.Part.createFormData("goods_price", price.toString())

        RetrofitClient.goodsApi.postNewItem(
                token,
                goodsCover, goodsSet,
                goodsName, goodsContent,
                goodsLongitude, goodsLatitude,
                goodsType, goodsPrice).apply {
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

    fun submitNewPost(
            token: String, cover: String,
            hwRatio: Float, picSet: List<String>,
            title: String, content: String,
            longitude: Double, latitude: Double,
            context: Context,
            onResult: (success: Boolean) -> Unit) {

    }

    fun saveItemDraft(cover: String, picSet: List<LocalMedia>, title: String, description: String, price: String, isQuotable: Boolean, isSecondHand: Boolean) {
        var draft = mJsonCacheUtil.getValue(NewItemDraftCache.KEY, NewItemDraftCache::class.java)
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

    fun savePostDraft(cover: String, hwRatio: Float, picSet: List<LocalMedia>, tag: String, anonymous: Boolean, title: String, content: String) {
        var draft = mJsonCacheUtil.getValue(NewPostDraftCache.KEY, NewPostDraftCache::class.java)
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

    fun deleteItemDraft() {
        mJsonCacheUtil.saveCache(NewItemDraftCache.KEY, null)
    }

    fun deletePostDraft() {
        mJsonCacheUtil.saveCache(NewPostDraftCache.KEY, null)
    }

    fun restoreItemDraft(onResult: (response: NewItemDraftCache?) -> Unit) {
        val draft = mJsonCacheUtil.getValue(NewItemDraftCache.KEY, NewItemDraftCache::class.java)
        onResult(draft)
    }

    fun restorePostDraft(onResult: (response: NewPostDraftCache?) -> Unit) {
        val draft = mJsonCacheUtil.getValue(NewPostDraftCache.KEY, NewPostDraftCache::class.java)
        onResult(draft)
    }
}