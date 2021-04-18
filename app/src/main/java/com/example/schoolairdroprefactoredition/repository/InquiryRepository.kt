package com.example.schoolairdroprefactoredition.repository

import com.example.schoolairdroprefactoredition.ui.adapter.IDesireRecyclerAdapter
import com.example.schoolairdroprefactoredition.ui.components.BaseIDesireEntity
import kotlin.collections.ArrayList

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
    fun getInquiry(page: Int, onResult: (List<BaseIDesireEntity>) -> Unit) {
        val data: ArrayList<BaseIDesireEntity> = ArrayList(12)
        for (i in 0..11) {
            val item = BaseIDesireEntity()
            item.setType(IDesireRecyclerAdapter.TYPE_ONE)
            when {
                i % 4 == 0 -> {
                    item.content = "求一个ipad air4价格希望在3000左右，几新都没有关系，只要不影响使用就ok"
                }
                i % 5 == 0 -> {
                    item.content = "有没有小伙伴有配ipad air4的笔，期望价格在500左右，有意向的dd"
                }
                else ->{
                    item.content = "收一本数据结构与算法分析 Java语言分析 第四版 主编马克艾伦维斯"
                }
            }
            data.add(item)
        }
        onResult(data)
    }
}