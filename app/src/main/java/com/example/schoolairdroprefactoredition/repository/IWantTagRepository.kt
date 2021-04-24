package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.api.base.CallbackResultOrNull
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainIWantTags
import com.example.schoolairdroprefactoredition.utils.ConstantUtil

class IWantTagRepository private constructor() {

    companion object {
        private var INSTANCE: IWantTagRepository? = null
        fun getInstance() = INSTANCE
                ?: IWantTagRepository().also {
                    INSTANCE = it
                }
    }

    /**
     * 获取所有求购标签
     */
    fun getIWantTags(onResult: (DomainIWantTags?) -> Unit) {
        val list = ArrayList<DomainIWantTags.Data>()
        list.add(DomainIWantTags.Data(0, "书本"))
        list.add(DomainIWantTags.Data(1, "收藏品"))
        list.add(DomainIWantTags.Data(2, "门票"))
        list.add(DomainIWantTags.Data(3, "运动用具"))
        list.add(DomainIWantTags.Data(4, "资料"))
        list.add(DomainIWantTags.Data(5, "鞋子"))
        list.add(DomainIWantTags.Data(6, "食物"))
        list.add(DomainIWantTags.Data(7, "其他"))
        list.add(DomainIWantTags.Data(8, "电子产品"))
        list.add(DomainIWantTags.Data(9, "电子配件"))
        onResult(DomainIWantTags(200, "", list))
//        RetrofitClient.iWantApi.getIWantTags(
//                ConstantUtil.CLIENT_ID,
//                ConstantUtil.CLIENT_SECRET).apply {
//            enqueue(CallbackResultOrNull(this, onResult))
//        }
    }
}