package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.ui.adapter.IWantRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.components.BaseIWantEntity
import kotlin.collections.ArrayList
import kotlin.random.Random

class InquiryRepository {

    companion object {
        private var INSTANCE: InquiryRepository? = null
        fun getInstance() = INSTANCE
                ?: InquiryRepository().also {
                    INSTANCE = it
                }
    }

    /**
     * 获取求购
     */
    fun getInquiry(page: Int, onResult: (List<BaseIWantEntity>) -> Unit) {
        val data: ArrayList<BaseIWantEntity> = ArrayList(12)
        for (i in 0..11) {
            val item = BaseIWantEntity()
            item.setType(IWantRecyclerAdapter.TYPE_ONE)
            when {
                i % 4 == 0 -> {
                    item.content = "求一个ipad air4价格希望在3000左右，几新都没有关系，只要不影响使用就ok"
                }
                i % 5 == 0 -> {
                    item.content = "有没有小伙伴有配ipad air4的笔，期望价格在500左右，有意向的dd"
                }
                else -> {
                    item.content = "收一本数据结构与算法分析 Java语言分析 第四版 主编马克艾伦维斯收一本数据结构与算法分析 Java语言分析 第四版 主编马克艾伦维斯收一本数据结构与算法分析 Java语言分析 第四版 主编马克艾伦维斯收一本数据结构与算法分析 Java语言分析 第四版 主编马克艾伦维斯"
                }
            }
            item.color = Random.nextInt(0, 3)
            data.add(item)
        }
        onResult(data)
    }
}