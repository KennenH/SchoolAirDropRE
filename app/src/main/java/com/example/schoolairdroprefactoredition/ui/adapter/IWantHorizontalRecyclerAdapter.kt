package com.example.schoolairdroprefactoredition.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.utils.ConstantUtil
import com.example.schoolairdroprefactoredition.utils.ImageUtil


/**
 * @author kennen
 * @date 2021/4/19
 */
class IWantHorizontalRecyclerAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_iwant_image) {

    override fun convert(holder: BaseViewHolder, item: String) {
        ImageUtil.loadRoundedImage(
                holder.itemView.findViewById(R.id.item_iwant_image_root),
                ConstantUtil.QINIU_BASE_URL + item)
    }
}