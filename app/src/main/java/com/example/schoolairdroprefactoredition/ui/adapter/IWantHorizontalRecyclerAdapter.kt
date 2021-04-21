package com.example.schoolairdroprefactoredition.ui.adapter

import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.schoolairdroprefactoredition.R
import com.example.schoolairdroprefactoredition.utils.ImageUtil
import com.example.schoolairdroprefactoredition.utils.MyUtil


/**
 * @author kennen
 * @date 2021/4/19
 */
class IWantHorizontalRecyclerAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_iwant_image) {

    override fun convert(holder: BaseViewHolder, item: String) {
        ImageUtil.loadRoundedImage(holder.itemView.findViewById(R.id.item_iwant_image_root), "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=510950855,3510074241&fm=224&gp=0.jpg")
    }
}