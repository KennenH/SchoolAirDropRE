package com.example.schoolairdroprefactoredition.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.domain.BinData
import com.example.schoolairdroprefactoredition.domain.DomainTrashBin

class TrashBinRecyclerAdapter : BaseQuickAdapter<BinData, BaseViewHolder>(R.layout.item_trash) {

    private var index: Int = 0

    override fun convert(holder: BaseViewHolder, item: BinData) {

    }

    fun setIndex(index: Int?) {
        this.index = index ?: 0
    }

}