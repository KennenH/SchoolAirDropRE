package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.api.base.CallbackResultOrNull
import com.example.schoolairdroprefactoredition.api.base.RetrofitClient
import com.example.schoolairdroprefactoredition.domain.DomainIWant
import com.example.schoolairdroprefactoredition.utils.ConstantUtil

class IWantRepository {

    companion object {
        private var INSTANCE: IWantRepository? = null
        fun getInstance() = INSTANCE
                ?: IWantRepository().also {
                    INSTANCE = it
                }
    }

    /**
     * 获取附近求购
     */
    fun getNearByIWant(
            page: Int,
            longitude: Double, latitude: Double,
            onResult: (DomainIWant?) -> Unit) {
        RetrofitClient.iWantApi.getNearByIWant(
                ConstantUtil.CLIENT_ID,
                ConstantUtil.CLIENT_SECRET,
                longitude, latitude, page).apply {
            enqueue(CallbackResultOrNull(this, onResult))
        }

//        val data: ArrayList<BaseIWantEntity> = ArrayList(12)
//        for (i in 0..11) {
//            val item = BaseIWantEntity()
//            item.userID = -1
//            val tagsPool = arrayListOf("书本", "日用品", "电脑", "鞋子", "手机", "电子配件")
//            when {
//                i % 3 == 0 -> {
//                    item.content = "撒谎的纠结啊看来撒旦看来撒旦开发商爱上了打开啊撒旦艰苦撒旦"
//                    item.images = "1,1"
//                }
//                i % 4 == 0 -> {
//                    item.content = "求一个ipad air4价格希望在3000左右，几新都没有关系，只要不影响使用就ok"
//                }
//                i % 5 == 0 -> {
//                    item.images = "1,1,1"
//                    item.content = "有没有小伙伴有配ipad air4的笔，期望价格在500左右，有意向的dd"
//                }
//                else -> {
//                    item.content = "收一本数据结构与算法分析 Java语言分析 第四版 主编马克艾伦维斯收一本数据结构与算法分析 Java语言分析 第四版 主编马克艾伦维斯收一本数据结构与算法分析 Java语言分析 第四版 主编马克艾伦维斯收一本数据结构与算法分析 Java语言分析 第四版 主编马克艾伦维斯"
//                }
//            }
//            item.tag = tagsPool.random()
//            item.color = Random.nextInt(0, 5)
//            data.add(item)
//        }
//        onResult(data)
    }
}