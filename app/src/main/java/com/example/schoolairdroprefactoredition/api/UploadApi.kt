package com.example.schoolairdroprefactoredition.api

import com.example.schoolairdroprefactoredition.domain.DomainIMPath
import com.example.schoolairdroprefactoredition.domain.DomainUploadPath
import com.example.schoolairdroprefactoredition.domain.DomainUploadToken
import retrofit2.Call
import retrofit2.http.*

/**
 * 上传图片单独api
 */
interface UploadApi {

    /**
     * 获取七牛云上传凭证
     */
    @POST("appapi/ajax/uploadToken")
    fun getUploadToken(@Header("Authorization") token: String): Call<DomainUploadToken>

    /**
     * 获取上传图片的文件名和taskId，文件路径前缀固定为tmp
     *
     * @param type 图片类型 one of below
     * [com.example.schoolairdroprefactoredition.utils.ConstantUtil.UPLOAD_TYPE_AVATAR] 头像
     * [com.example.schoolairdroprefactoredition.utils.ConstantUtil.UPLOAD_TYPE_GOODS] 物品
     * [com.example.schoolairdroprefactoredition.utils.ConstantUtil.UPLOAD_TYPE_IM] 聊天
     * [com.example.schoolairdroprefactoredition.utils.ConstantUtil.UPLOAD_TYPE_IWANT] 帖子
     * @param amount 需要上传的图片数量
     */
    @FormUrlEncoded
    @POST("appapi/ajax/dispatchUploadTask")
    fun getImagePath(@Header("Authorization") token: String, @Field("img_type") type: String, @Field("amount") amount: Int): Call<DomainUploadPath>

    /**
     * 转移im图片 多图
     *
     * @param taskID 上传任务分配到的id
     * @param keys 上传任务分配的key
     */
    @FormUrlEncoded
    @POST("appapi/ajax/moveIMFile")
    fun moveIMImages(
            @Header("Authorization") token: String,
            @Field("task_id") taskID: String,
            @Field("im_file_keys") keys: String
    ): Call<DomainIMPath>

}